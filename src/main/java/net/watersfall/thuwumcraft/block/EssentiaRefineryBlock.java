package net.watersfall.thuwumcraft.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.watersfall.thuwumcraft.api.lookup.AspectContainer;
import net.watersfall.thuwumcraft.block.entity.EssentiaRefineryBlockEntity;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlocks;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.state.property.Properties.*;

public class EssentiaRefineryBlock extends BlockWithEntity
{
	public static final BooleanProperty CONNECTED_TO_SMELTERY = BooleanProperty.of("connected_to_smeltery");

	protected static final VoxelShape[] OUTLINE_SHAPES = new VoxelShape[64];

	static
	{
		VoxelShape center = createCuboidShape(4, 4, 4, 12, 12, 12);
		VoxelShape north = createCuboidShape(5, 5, 0, 11, 11, 4);
		VoxelShape south = createCuboidShape(5, 5, 12, 11, 11, 16);
		VoxelShape east = createCuboidShape(12, 5, 5, 16, 11, 11);
		VoxelShape west = createCuboidShape(0, 5, 5, 4, 11, 11);
		VoxelShape up = createCuboidShape(5, 12, 5, 11, 16, 11);
		VoxelShape down = createCuboidShape(5, 0, 5, 11, 4, 11);
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

	public EssentiaRefineryBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState(this.getDefaultState()
				.with(NORTH, false)
				.with(SOUTH, false)
				.with(EAST, false)
				.with(WEST, false)
				.with(UP, false)
				.with(DOWN, false)
				.with(CONNECTED_TO_SMELTERY, false)
		);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(Properties.NORTH, Properties.SOUTH, Properties.EAST, Properties.WEST, Properties.UP, Properties.DOWN, CONNECTED_TO_SMELTERY);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new EssentiaRefineryBlockEntity(pos, state);
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
			if(direction == Direction.DOWN)
			{
				if(context.getWorld().getBlockState(context.getBlockPos().offset(direction)).isOf(ThuwumcraftBlocks.ESSENTIA_SMELTERY))
				{
					state = state.with(getPropertyFromDirection(direction), true).with(CONNECTED_TO_SMELTERY, true);
				}
			}
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
		if(direction == Direction.DOWN)
		{
			if(neighborState.isAir())
			{
				return state.with(DOWN, false).with(CONNECTED_TO_SMELTERY, false);
			}
			else if(neighborState.isOf(ThuwumcraftBlocks.ESSENTIA_SMELTERY))
			{
				return state.with(DOWN, true).with(CONNECTED_TO_SMELTERY, true);
			}
		}
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
