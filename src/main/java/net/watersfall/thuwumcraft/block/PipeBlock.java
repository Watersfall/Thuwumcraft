package net.watersfall.thuwumcraft.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
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
import net.watersfall.thuwumcraft.api.lookup.AspectContainer;
import net.watersfall.thuwumcraft.block.entity.PipeEntity;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.state.property.Properties.*;

public class PipeBlock extends Block implements BlockEntityProvider
{
	protected static final VoxelShape[] OUTLINE_SHAPES = new VoxelShape[64];

	static
	{
		VoxelShape center = createCuboidShape(6, 6, 6, 10, 10, 10);
		VoxelShape north = createCuboidShape(6, 6, 0, 10, 10, 6);
		VoxelShape south = createCuboidShape(6, 6, 10, 10, 10, 16);
		VoxelShape east = createCuboidShape(10, 6, 6, 16, 10, 10);
		VoxelShape west = createCuboidShape(0, 6, 6, 6, 10, 10);
		VoxelShape up = createCuboidShape(6, 10, 6, 10, 16, 10);
		VoxelShape down = createCuboidShape(6, 0, 6, 10, 6, 10);
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
		for(Direction direction : Direction.values())
		{
			if(AspectContainer.API.find(context.getWorld(), context.getBlockPos().offset(direction), direction.getOpposite()) != null)
			{
				state = state.with(getPropertyFromDirection(direction), true);
			}
		}
		return state;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos)
	{
		if(AspectContainer.API.find((World)world, neighborPos, direction.getOpposite()) != null)
		{
			return state.with(getPropertyFromDirection(direction), true);
		}
		else if(neighborState.getBlock() == Blocks.AIR)
		{
			return state.with(getPropertyFromDirection(direction), false);
		}
		return state;
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
