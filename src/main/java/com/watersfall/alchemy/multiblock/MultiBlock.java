package com.watersfall.alchemy.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface MultiBlock<T extends MultiBlockComponent>
{
	void add();

	void remove();

	void onUse();

	boolean isValid();

	void markInvalid();

	default void tick()
	{
		if(!this.isValid())
		{
			this.remove();
		}
		for(int i = 0; i < this.getComponents().length; i++)
		{
			this.getComponents()[i].tick();
		}
	}

	MultiBlockType<? extends MultiBlock<? extends MultiBlockComponent>> getType();

	BlockPos getPos();

	World getWorld();

	T[] getComponents();
}
