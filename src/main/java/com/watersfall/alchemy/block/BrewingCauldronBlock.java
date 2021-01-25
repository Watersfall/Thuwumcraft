package com.watersfall.alchemy.block;

import com.watersfall.alchemy.AlchemyMod;
import com.watersfall.alchemy.blockentity.BrewingCauldronEntity;
import com.watersfall.alchemy.recipe.CauldronRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class BrewingCauldronBlock extends Block implements BlockEntityProvider
{
	private static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE), BooleanBiFunction.ONLY_FIRST);

	public static final HashMap<Item, CauldronRecipe> INGREDIENTS = new HashMap<>();

	public static boolean checkIfIngredient(Item item, World world)
	{
		if(!INGREDIENTS.containsKey(item))
		{
			List<CauldronRecipe> list = world.getServer().getRecipeManager().listAllOfType(AlchemyMod.CAULDRON_RECIPE_TYPE);
			for(int i = 0; i < list.size(); i++)
			{
				if(list.get(i).input.getItem() == item)
				{
					INGREDIENTS.put(item, list.get(i));
					return true;
				}
			}
			return false;
		}
		return true;
	}

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
			if(!world.isClient && checkIfIngredient(item, world))
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
			else if(entity.getIngredientCount() > 1)
			{
				if(!world.isClient)
				{
					Optional<CauldronRecipe> recipeOptional = world.getServer().getRecipeManager().getFirstMatch(AlchemyMod.CAULDRON_RECIPE_TYPE, entity, world);
					if(recipeOptional.isPresent())
					{
						entity.setInput(itemStack);
						CauldronRecipe recipe = recipeOptional.get();
						ItemStack stack = recipe.craft(entity);
						if(stack != null)
						{
							player.setStackInHand(hand, recipe.craft(entity));
							entity.setWaterLevel((short) (entity.getWaterLevel() - 333));
							entity.sync();
						}
					}
				}
				return ActionResult.success(world.isClient);
			}
			return ActionResult.success(world.isClient);
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
