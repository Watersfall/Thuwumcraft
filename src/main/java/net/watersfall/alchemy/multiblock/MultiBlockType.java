package net.watersfall.alchemy.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface MultiBlockType<T extends MultiBlock<? extends MultiBlockComponent>>
{
	BlockPos[] MISSING = new BlockPos[0];

	BlockPos[] matches(PlayerEntity player, World world, BlockPos pos);

	MultiBlock<? extends MultiBlockComponent> create(PlayerEntity player, World world, BlockPos pos, BlockPos[] states);
}
