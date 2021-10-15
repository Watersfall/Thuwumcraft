package net.watersfall.thuwumcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.watersfall.thuwumcraft.api.lookup.AspectContainer;
import net.watersfall.thuwumcraft.block.entity.JarEntity;
import net.watersfall.thuwumcraft.recipe.AspectIngredient;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRecipes;
import net.watersfall.thuwumcraft.util.BlockUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class JarBlock extends Block implements BlockEntityProvider
{
	protected static final VoxelShape OUTLINE_SHAPE, OUTLINE_SHAPE_CONNECTED;

	static
	{
		OUTLINE_SHAPE = VoxelShapes.union(
				createCuboidShape(3, 0, 3, 13, 12, 13),
				createCuboidShape(5, 12, 5, 11, 14, 11)
		);
		OUTLINE_SHAPE_CONNECTED = VoxelShapes.union(
				OUTLINE_SHAPE,
				createCuboidShape(5, 14, 5, 11, 15, 11),
				createCuboidShape(6, 15, 6, 10, 16, 10)
		);
	}

	public JarBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(Properties.UP);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context)
	{
		BlockState state = super.getPlacementState(context);
		if(AspectContainer.API.find(context.getWorld(), context.getBlockPos().offset(Direction.UP), Direction.DOWN) != null)
		{
			state = state.with(Properties.UP, true);
		}
		return state;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos)
	{
		if(direction == Direction.UP)
		{
			if(AspectContainer.API.find((World)world, neighborPos, Direction.DOWN) != null)
			{
				state = state.with(Properties.UP, true);
			}
			else
			{
				state = state.with(Properties.UP, false);
			}
		}
		return state;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		ItemStack stack = player.getStackInHand(hand);
		if(!stack.isEmpty())
		{
			JarEntity entity = (JarEntity) world.getBlockEntity(pos);
			if(entity != null)
			{
				entity.setCurrentInput(stack);
				Optional<AspectIngredient> ingredientOptional = world.getRecipeManager().getFirstMatch(ThuwumcraftRecipes.ASPECT_INGREDIENTS, entity, world);
				if(ingredientOptional.isPresent())
				{
					if(!world.isClient)
					{
						AspectIngredient ingredient = ingredientOptional.get();
						ingredient.craft(entity);
						stack.decrement(1);
						entity.markDirty();
						entity.sync();
					}
					return ActionResult.success(world.isClient);
				}
			}
		}
		return ActionResult.PASS;
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos)
	{
		return true;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		if(state.get(Properties.UP))
		{
			return OUTLINE_SHAPE_CONNECTED;
		}
		return OUTLINE_SHAPE;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new JarEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type)
	{
		return world.isClient ? null : BlockUtils.checkType(type, ThuwumcraftBlockEntities.JAR_ENTITY, JarEntity::tick);
	}
}
