package net.watersfall.alchemy.block;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.watersfall.alchemy.blockentity.ChildBlockEntity;
import org.jetbrains.annotations.Nullable;

public class AlchemicalFurnaceBlock extends ChildBlock implements BlockEntityProvider
{
	protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.cuboid(0D, 0D, 0D, 2D, 2D, 1D);

	public AlchemicalFurnaceBlock(Settings settings)
	{
		super(settings);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView world)
	{
		return new ChildBlockEntity();
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return OUTLINE_SHAPE;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return OUTLINE_SHAPE;
	}

	@Override
	public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos)
	{
		return OUTLINE_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return OUTLINE_SHAPE;
	}
}
