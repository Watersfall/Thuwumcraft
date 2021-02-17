package net.watersfall.alchemy.api.multiblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface MultiBlock<T extends MultiBlockComponent>
{
	void add(World world, BlockPos pos);

	void remove();

	void onUse(World world, BlockPos pos, PlayerEntity player);

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

	void read(CompoundTag tag);

	CompoundTag write(CompoundTag tag);

	void markDirty();

	MultiBlockType<? extends MultiBlock<? extends MultiBlockComponent>> getType();

	BlockPos getPos();

	World getWorld();

	T[] getComponents();
}
