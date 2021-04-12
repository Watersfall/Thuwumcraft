package net.watersfall.alchemy.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.watersfall.alchemy.block.entity.EssentiaRefineryBlockEntity;
import org.jetbrains.annotations.Nullable;

public class EssentiaRefineryBlock extends BlockWithEntity
{
	public EssentiaRefineryBlock(Settings settings)
	{
		super(settings);
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
}
