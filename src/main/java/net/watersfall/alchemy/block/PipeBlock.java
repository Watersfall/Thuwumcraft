package net.watersfall.alchemy.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.watersfall.alchemy.block.entity.AlchemyBlockEntities;
import net.watersfall.alchemy.block.entity.PipeEntity;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.state.property.Properties.*;

public class PipeBlock extends Block implements BlockEntityProvider
{
	protected static final VoxelShape[] OUTLINE_SHAPES = new VoxelShape[64];

	static
	{
		VoxelShape center = createCuboidShape(6, 6, 6, 10, 10, 10);
		VoxelShape north = createCuboidShape(7, 7, 0, 9, 9, 6);
		VoxelShape south = createCuboidShape(7, 7, 10, 9, 9, 16);
		VoxelShape east = createCuboidShape(10, 7, 7, 16, 9, 9);
		VoxelShape west = createCuboidShape(0, 7, 7, 6, 9, 9);
		VoxelShape up = createCuboidShape(7, 10, 7, 9, 16, 9);
		VoxelShape down = createCuboidShape(7, 0, 7, 9, 6, 9);
		for(int i = 0; i < 64; i++)
		{
			VoxelShape shape = center;
			if((i & 1) > 0)
				shape = VoxelShapes.union(shape, north);
			if((i & 2) > 1)
				shape = VoxelShapes.union(shape, south);
			if((i & 4) > 1)
				shape = VoxelShapes.union(shape, east);
			if((i & 8) > 1)
				shape = VoxelShapes.union(shape, west);
			if((i & 16) > 1)
				shape = VoxelShapes.union(shape, up);
			if((i & 32) > 1)
				shape = VoxelShapes.union(shape, down);
			OUTLINE_SHAPES[i] = shape;
		}
	}

	public PipeBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState(this.getDefaultState()
				.with(NORTH, false)
				.with(SOUTH, false)
				.with(EAST, false)
				.with(WEST, false)
				.with(UP, false)
				.with(DOWN, false)
		);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new PipeEntity(pos, state);
	}

	public static BooleanProperty getPropertyFromDirection(Direction direction)
	{
		switch(direction)
		{
			case NORTH:
				return NORTH;
			case SOUTH:
				return SOUTH;
			case EAST:
				return EAST;
			case WEST:
				return WEST;
			case UP:
				return UP;
			default:
				return DOWN;
		}
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context)
	{
		BlockState state = super.getPlacementState(context);
		BlockState up = context.getWorld().getBlockState(context.getBlockPos().up());
		BlockState down = context.getWorld().getBlockState(context.getBlockPos().down());
		BlockState east = context.getWorld().getBlockState(context.getBlockPos().east());
		BlockState west = context.getWorld().getBlockState(context.getBlockPos().west());
		BlockState north = context.getWorld().getBlockState(context.getBlockPos().north());
		BlockState south = context.getWorld().getBlockState(context.getBlockPos().south());
		if(up.getBlock() == AlchemyBlocks.ASPECT_PIPE_BLOCK)
			state = state.with(UP, true);
		if(down.getBlock() == AlchemyBlocks.ASPECT_PIPE_BLOCK || down.getBlock() == AlchemyBlocks.JAR_BLOCK)
			state = state.with(DOWN, true);
		if(east.getBlock() == AlchemyBlocks.ASPECT_PIPE_BLOCK)
			state = state.with(EAST, true);
		if(west.getBlock() == AlchemyBlocks.ASPECT_PIPE_BLOCK)
			state = state.with(WEST, true);
		if(north.getBlock() == AlchemyBlocks.ASPECT_PIPE_BLOCK)
			state = state.with(NORTH, true);
		if(south.getBlock() == AlchemyBlocks.ASPECT_PIPE_BLOCK)
			state = state.with(SOUTH, true);
		return state;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos)
	{
		if(neighborState.getBlock() == AlchemyBlocks.ASPECT_PIPE_BLOCK || (neighborState.getBlock() == AlchemyBlocks.JAR_BLOCK && direction == Direction.DOWN))
		{
			return state.with(getPropertyFromDirection(direction), true);
		}
		else if(neighborState.getBlock() == Blocks.AIR)
		{
			return state.with(getPropertyFromDirection(direction), false);
		}
		return state;
	}

	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
		return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type)
	{
		return world.isClient ? null : checkType(type, AlchemyBlockEntities.ASPECT_PIPE_ENTITY, PipeEntity::tick);
	}

	private byte generateIndex(BlockState state)
	{
		byte index = 0;
		if(state.get(NORTH))
			index += 1;
		if(state.get(SOUTH))
			index += (1 << 1);
		if(state.get(EAST))
			index += (1 << 2);
		if(state.get(WEST))
			index += (1 << 3);
		if(state.get(UP))
			index += (1 << 4);
		if(state.get(DOWN))
			index += (1 << 5);
		return index;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return OUTLINE_SHAPES[generateIndex(state)];
	}
}
