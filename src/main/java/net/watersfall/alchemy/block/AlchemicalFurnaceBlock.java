package net.watersfall.alchemy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.watersfall.alchemy.block.entity.AlchemicalFurnaceEntity;
import org.jetbrains.annotations.Nullable;

public class AlchemicalFurnaceBlock extends ChildBlock implements BlockEntityProvider
{
	public static final DirectionProperty DIRECTION = Properties.FACING;
	protected static final VoxelShape[] OUTLINE_SHAPES = new VoxelShape[]{
			VoxelShapes.cuboid(0D, 0D, 0D, 2D, 2D, 1D),
			VoxelShapes.cuboid(-1D, 0D, 0D, 1D, 2D, 1D),
			VoxelShapes.cuboid(0D, 0D, -1D, 1D, 2D, 1D),
			VoxelShapes.cuboid(0D, 0D, 0D, 1D, 2D, 2D),
	};

	public AlchemicalFurnaceBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState(this.getDefaultState().with(DIRECTION, Direction.SOUTH));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(DIRECTION);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new AlchemicalFurnaceEntity(pos, state);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		switch(state.get(DIRECTION))
		{
			case SOUTH:
				return OUTLINE_SHAPES[0];
			case NORTH:
				return OUTLINE_SHAPES[1];
			case EAST:
				return OUTLINE_SHAPES[2];
			default:
				return OUTLINE_SHAPES[3];
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return getOutlineShape(state, world, pos, context);
	}

	public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return getCollisionShape(state, world, pos, context);
	}

	@Override
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos)
	{
		return 0.2F;
	}

	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos)
	{
		return false;
	}
}
