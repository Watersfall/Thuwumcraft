package net.watersfall.alchemy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;

public class ArcaneLightBlock extends Block
{
	public ArcaneLightBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.INVISIBLE;
	}
}
