package com.watersfall.poisonedweapons.block;

import com.watersfall.poisonedweapons.api.Ingredient;
import com.watersfall.poisonedweapons.api.Ingredients;
import com.watersfall.poisonedweapons.api.Poisonable;
import com.watersfall.poisonedweapons.blockentity.BrewingCauldronEntity;
import com.watersfall.poisonedweapons.item.AlchemyModItems;
import com.watersfall.poisonedweapons.util.StatusEffectHelper;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Set;

public class BrewingCauldronBlock extends Block implements BlockEntityProvider
{
    public static final IntProperty LEVEL = IntProperty.of("level", 0, 4);
    private static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE), BooleanBiFunction.ONLY_FIRST);

    public BrewingCauldronBlock(Settings settings)
    {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(LEVEL, 0));
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
    {
        return OUTLINE_SHAPE;
    }

    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos)
    {
        return RAY_TRACE_SHAPE;
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
    {
        int i = state.get(LEVEL);
        float f = (float)pos.getY() + (6.0F + (float)(3 * i)) / 16.0F;
        if (!world.isClient && entity.isOnFire() && i > 0 && entity.getY() <= (double)f)
        {
            entity.extinguish();
            this.setLevel(world, pos, state, i - 1);
        }

    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    public void setLevel(World world, BlockPos pos, BlockState state, int level)
    {
        world.setBlockState(pos, state.with(BrewingCauldronBlock.LEVEL, MathHelper.clamp(level, 0, 4)), 2);
        world.updateComparators(pos, this);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
    {
        ItemStack itemStack = player.getStackInHand(hand);
        if(itemStack.isEmpty())
        {
            return ActionResult.PASS;
        }
        int level = state.get(BrewingCauldronBlock.LEVEL);
        Item item = itemStack.getItem();
        if(item == Items.WATER_BUCKET)
        {
            if(level <= 0 && !world.isClient)
            {
                if (!player.abilities.creativeMode) {
                    player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                }
                this.setLevel(world, pos, state, 1);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return ActionResult.success(world.isClient);
        }
        else if(item == Items.BUCKET)
        {
            if(level == 1 && !world.isClient)
            {
                itemStack.decrement(1);
                if (itemStack.isEmpty())
                {
                    player.setStackInHand(hand, new ItemStack(Items.WATER_BUCKET));
                }
                else if (!player.inventory.insertStack(new ItemStack(Items.WATER_BUCKET)))
                {
                    player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                }
                this.setLevel(world, pos, state, 0);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return ActionResult.success(world.isClient);
        }
        else if(level >= 1)
        {
            if(Ingredients.ingredients.containsKey(item))
            {
                if(!world.isClient)
                {
                    BrewingCauldronEntity entity = (BrewingCauldronEntity)world.getBlockEntity(pos);
                    if(entity != null)
                    {
                        if(entity.count(item) <= 0)
                        {
                            if(entity.addStack(new ItemStack(itemStack.getItem())))
                            {
                                if(!player.abilities.creativeMode)
                                {
                                    itemStack.decrement(1);
                                }
                                this.setLevel(world, pos, state, level + 1);
                            }
                        }
                    }
                }
                return ActionResult.success(world.isClient);
            }
            else if(item instanceof Poisonable)
            {
                BrewingCauldronEntity entity = (BrewingCauldronEntity)world.getBlockEntity(pos);
                if(entity != null)
                {
                    if(entity.getCurrentIngredients() > 1)
                    {
                        Poisonable poisonable = (Poisonable)item;
                        if(poisonable.getEffect(itemStack) == null || poisonable.getUses(itemStack) <= 0)
                        {
                            if(!world.isClient)
                            {
                                Ingredient i1 = Ingredients.ingredients.get(entity.getStack(0).getItem());
                                Ingredient i2 = Ingredients.ingredients.get(entity.getStack(1).getItem());
                                Ingredient i3 = null;
                                if(entity.getCurrentIngredients() > 2)
                                {
                                    i3 = Ingredients.ingredients.get(entity.getStack(2).getItem());
                                }
                                StatusEffectInstance instance = Ingredient.getSingleEffectFromIngredients(i1, i2, i3);
                                if(itemStack.getTag() == null)
                                {
                                    itemStack.setTag(new CompoundTag());
                                }
                                StatusEffectHelper.set(itemStack, instance.getEffectType(), instance.getDuration(), instance.getAmplifier());
                            }
                        }
                    }
                }
                return ActionResult.success(world.isClient);
            }
            else if(item == Items.GLASS_BOTTLE || item == AlchemyModItems.THROW_BOTTLE)
            {
                BrewingCauldronEntity entity = (BrewingCauldronEntity)world.getBlockEntity(pos);
                if(entity != null && !world.isClient)
                {
                    if(entity.getCurrentIngredients() > 1)
                    {
                        Ingredient i1 = Ingredients.ingredients.get(entity.getStack(0).getItem());
                        Ingredient i2 = Ingredients.ingredients.get(entity.getStack(1).getItem());
                        Ingredient i3 = null;
                        if(entity.getCurrentIngredients() > 2)
                        {
                            i3 = Ingredients.ingredients.get(entity.getStack(2).getItem());
                        }
                        Set<StatusEffectInstance> instance = Ingredient.getEffectFromIngredients(i1, i2, i3);
                        itemStack.decrement(1);
                        ItemStack potion;
                        if(item == Items.GLASS_BOTTLE)
                        {
                            potion = new ItemStack(Items.POTION);
                        }
                        else
                        {
                            potion = new ItemStack(Items.SPLASH_POTION);
                        }
                        PotionUtil.setCustomPotionEffects(potion, instance);
                        if (itemStack.isEmpty())
                        {
                            player.setStackInHand(hand, potion);
                        }
                        else if (!player.inventory.insertStack(new ItemStack(Items.WATER_BUCKET)))
                        {
                            player.dropItem(potion, false);
                        }
                        entity.getContents().clear();
                        this.setLevel(world, pos, state, 0);
                    }
                }
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }

    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type)
    {
        return false;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world)
    {
        return new BrewingCauldronEntity();
    }
}
