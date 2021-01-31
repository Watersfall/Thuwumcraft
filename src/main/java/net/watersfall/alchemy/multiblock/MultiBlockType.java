package net.watersfall.alchemy.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface MultiBlockType<T extends MultiBlock<? extends MultiBlockComponent>>
{
	boolean matches(World world, BlockPos pos);

	MultiBlock<? extends MultiBlockComponent> create(World world, BlockPos pos);
}
