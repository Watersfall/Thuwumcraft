package com.watersfall.poisonedweapons.block;

import com.watersfall.poisonedweapons.api.Ingredient;
import com.watersfall.poisonedweapons.api.Ingredients;
import com.watersfall.poisonedweapons.api.Poisonable;
import com.watersfall.poisonedweapons.blockentity.BrewingCauldronEntity;
import com.watersfall.poisonedweapons.item.AlchemyModItems;
import com.watersfall.poisonedweapons.util.StatusEffectHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Set;

public class BrewingCauldronBlock extends Block implements BlockEntityProvider
{
	private static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE), BooleanBiFunction.ONLY_FIRST);

	public BrewingCauldronBlock(Settings settings)
	{
		super(settings);
		setDefaultState(getStateManager().getDefaultState());
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
		BrewingCauldronEntity cauldron = (BrewingCauldronEntity) world.getBlockEntity(pos);
		if(!world.isClient && entity.isOnFire() && cauldron.getWaterLevel() > 333)
		{
			entity.extinguish();
			cauldron.setWaterLevel((short) (cauldron.getWaterLevel() - 333));
			cauldron.sync();
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		ItemStack itemStack = player.getStackInHand(hand);
		if(itemStack.isEmpty())
		{
			return ActionResult.PASS;
		}
		BrewingCauldronEntity entity = (BrewingCauldronEntity) world.getBlockEntity(pos);
		assert entity != null;
		Item item = itemStack.getItem();
		if(item == Items.WATER_BUCKET)
		{
			if(!world.isClient)
			{
				if(!player.abilities.creativeMode)
				{
					player.setStackInHand(hand, new ItemStack(Items.BUCKET));
				}
				entity.clear();
				entity.setWaterLevel((short) 1000);
				entity.sync();
				world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
			return ActionResult.success(world.isClient);
		}
		else if(item == Items.BUCKET)
		{
			if(entity.getIngredientCount() == 0 && !world.isClient)
			{
				itemStack.decrement(1);
				if(itemStack.isEmpty())
				{
					player.setStackInHand(hand, new ItemStack(Items.WATER_BUCKET));
				}
				else if(!player.inventory.insertStack(new ItemStack(Items.WATER_BUCKET)))
				{
					player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
				}
				entity.setWaterLevel((short) 0);
				entity.sync();
				world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
			return ActionResult.success(world.isClient);
		}
		else if(entity.getWaterLevel() > 0)
		{
			if(Ingredients.ingredients.containsKey(item))
			{
				if(!world.isClient)
				{
					if(entity.count(item) <= 0)
					{
						if(entity.addStack(new ItemStack(itemStack.getItem())))
						{
							if(!player.abilities.creativeMode)
							{
								itemStack.decrement(1);
							}
							entity.setIngredientCount((byte) (entity.getIngredientCount() + 1));
							entity.sync();
						}
					}
				}
				return ActionResult.success(world.isClient);
			}
			else if(item instanceof Poisonable)
			{
				if(entity.getIngredientCount() > 1)
				{
					Poisonable poisonable = (Poisonable) item;
					if(poisonable.getEffect(itemStack) == null || poisonable.getUses(itemStack) <= 0)
					{
						if(!world.isClient)
						{
							Ingredient i1 = Ingredients.ingredients.get(entity.getStack(0).getItem());
							Ingredient i2 = Ingredients.ingredients.get(entity.getStack(1).getItem());
							Ingredient i3 = null;
							if(entity.getIngredientCount() > 2)
							{
								i3 = Ingredients.ingredients.get(entity.getStack(2).getItem());
							}
							StatusEffectInstance instance = Ingredient.getSingleEffectFromIngredients(i1, i2, i3);
							if(itemStack.getTag() == null)
							{
								itemStack.setTag(new CompoundTag());
							}
							StatusEffectHelper.set(itemStack, instance.getEffectType(), instance.getDuration(), instance.getAmplifier());
							entity.setWaterLevel((short) (entity.getWaterLevel() - 333));
							entity.sync();
						}
					}
				}
				return ActionResult.success(world.isClient);
			}
			else if(item == Items.GLASS_BOTTLE || item == AlchemyModItems.THROW_BOTTLE)
			{
				if(!world.isClient)
				{
					if(entity.getIngredientCount() > 1 && entity.getWaterLevel() >= 333)
					{
						Ingredient i1 = Ingredients.ingredients.get(entity.getStack(0).getItem());
						Ingredient i2 = Ingredients.ingredients.get(entity.getStack(1).getItem());
						Ingredient i3 = null;
						if(entity.getIngredientCount() > 2)
						{
							i3 = Ingredients.ingredients.get(entity.getStack(2).getItem());
						}
						Set<StatusEffectInstance> instance = Ingredient.getEffectsFromIngredients(i1, i2, i3);
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
						if(itemStack.isEmpty())
						{
							player.setStackInHand(hand, potion);
						}
						else if(!player.inventory.insertStack(potion))
						{
							player.dropItem(potion, false);
						}
						entity.setWaterLevel((short) (entity.getWaterLevel() - 333));
						entity.sync();
					}
				}
				return ActionResult.success(world.isClient);
			}
			else if(item == AlchemyModItems.LADLE_ITEM)
			{
				if(!world.isClient && entity.getIngredientCount() > 1)
				{
					Ingredient i1 = Ingredients.ingredients.get(entity.getStack(0).getItem());
					Ingredient i2 = Ingredients.ingredients.get(entity.getStack(1).getItem());
					Ingredient i3 = null;
					if(entity.getIngredientCount() > 2)
					{
						i3 = Ingredients.ingredients.get(entity.getStack(2).getItem());
					}
					Set<StatusEffectInstance> instance = Ingredient.getEffectsFromIngredients(i1, i2, i3);
					StatusEffectHelper.createLadle(itemStack, instance);
				}
				return ActionResult.success(world.isClient);
			}
			else if(item == Items.ARROW)
			{
				if(!world.isClient && entity.getIngredientCount() > 1)
				{
					Ingredient i1 = Ingredients.ingredients.get(entity.getStack(0).getItem());
					Ingredient i2 = Ingredients.ingredients.get(entity.getStack(1).getItem());
					Ingredient i3 = null;
					if(entity.getIngredientCount() > 2)
					{
						i3 = Ingredients.ingredients.get(entity.getStack(2).getItem());
					}
					Set<StatusEffectInstance> instance = Ingredient.getEffectsFromIngredients(i1, i2, i3);
					itemStack.decrement(1);
					ItemStack newStack = new ItemStack(Items.TIPPED_ARROW, 1);
					PotionUtil.setCustomPotionEffects(newStack, instance);
					if(!player.inventory.insertStack(newStack))
					{
						player.dropItem(newStack, false);
					}
					entity.setWaterLevel((short)(entity.getWaterLevel() - 100));
					entity.sync();
				}
				return ActionResult.success(world.isClient);
			}
		}
		if(entity.getWaterLevel() <= 5)
		{
			entity.setWaterLevel((short) 0);
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
