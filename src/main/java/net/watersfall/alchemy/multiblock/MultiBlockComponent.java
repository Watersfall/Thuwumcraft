package net.watersfall.alchemy.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

public interface MultiBlockComponent
{
	default void onBreak()
	{
		getMultiBlock().markInvalid();
	}

	void onUse(PlayerEntity player);

	void tick();

	BlockPos getPos();

	World getWorld();

	void setWorld(World world);

	MultiBlock<? extends MultiBlockComponent> getMultiBlock();

	VoxelShape getOutline();

	void read(CompoundTag tag);

	CompoundTag write(CompoundTag tag);
}
