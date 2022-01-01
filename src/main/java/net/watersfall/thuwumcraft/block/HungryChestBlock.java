package net.watersfall.thuwumcraft.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.watersfall.thuwumcraft.block.entity.HungryChestBlockEntity;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;
import net.watersfall.thuwumcraft.util.BlockUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class HungryChestBlock extends AbstractChestBlock<HungryChestBlockEntity> implements Waterloggable
{
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final VoxelShape OUTLINE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);

	public HungryChestBlock(Settings settings)
	{
		super(settings, () -> ThuwumcraftBlockEntities.HUNGRY_CHEST);
		this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if (!world.isClient)
		{
			NamedScreenHandlerFactory namedScreenHandlerFactory = this.createScreenHandlerFactory(state, world, pos);
			if (namedScreenHandlerFactory != null)
			{
				player.openHandledScreen(namedScreenHandlerFactory);
			}
		}
		return ActionResult.success(world.isClient);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return this.getDefaultState()
				.with(FACING, ctx.getPlayerFacing().getOpposite())
				.with(WATERLOGGED, fluidState.isEqualAndStill(Fluids.WATER));
	}

	@Override
	public DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> getBlockEntitySource(BlockState state, World world, BlockPos pos, boolean ignoreBlocked)
	{
		return DoubleBlockProperties.PropertyRetriever::getFallback;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new HungryChestBlockEntity(pos, state);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return OUTLINE;
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		super.scheduledTick(state, world, pos, random);
		BlockEntity test = world.getBlockEntity(pos);
		if(test instanceof HungryChestBlockEntity chest)
		{
			chest.close();
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock()))
		{
			BlockEntity test = world.getBlockEntity(pos);
			if (test instanceof Inventory inventory)
			{
				ItemScatterer.spawn(world, pos, inventory);
				world.updateComparators(pos, this);
			}
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos)
	{
		if(state.get(WATERLOGGED))
		{
			world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public FluidState getFluidState(BlockState state)
	{
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type)
	{
		return BlockUtils.checkType(type, ThuwumcraftBlockEntities.HUNGRY_CHEST, world.isClient ? HungryChestBlockEntity::clientTick : HungryChestBlockEntity::serverTick);
	}
}
