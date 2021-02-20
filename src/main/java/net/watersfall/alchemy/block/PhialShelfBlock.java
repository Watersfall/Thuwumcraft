package net.watersfall.alchemy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.watersfall.alchemy.block.entity.PhialShelfEntity;
import org.jetbrains.annotations.Nullable;

public class PhialShelfBlock extends Block implements BlockEntityProvider
{
	public static final DirectionProperty DIRECTION = Properties.HORIZONTAL_FACING;
	protected static final VoxelShape OUTLINE_SHAPE_SOUTH = VoxelShapes.cuboid(0, 0, 0, 1, 1, 0.5);
	protected static final VoxelShape OUTLINE_SHAPE_NORTH = VoxelShapes.cuboid(0, 0, 0.5, 1, 1, 1);
	protected static final VoxelShape OUTLINE_SHAPE_WEST = VoxelShapes.cuboid(0.5, 0, 0, 1, 1, 1);
	protected static final VoxelShape OUTLINE_SHAPE_EAST = VoxelShapes.cuboid(0, 0, 0, 0.5, 1, 1);

	public PhialShelfBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState(this.getDefaultState().with(DIRECTION, Direction.NORTH));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(DIRECTION);
	}

	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		return this.getDefaultState().with(DIRECTION, ctx.getPlayerFacing().getOpposite());
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new PhialShelfEntity(pos, state);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		switch(state.get(DIRECTION))
		{
			case NORTH:
				return OUTLINE_SHAPE_NORTH;
			case SOUTH:
				return OUTLINE_SHAPE_SOUTH;
			case EAST:
				return OUTLINE_SHAPE_EAST;
			default:
				return OUTLINE_SHAPE_WEST;
		}
	}
}
