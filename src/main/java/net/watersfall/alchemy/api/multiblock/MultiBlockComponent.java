package net.watersfall.alchemy.api.multiblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

public interface MultiBlockComponent
{
	/**
	 * Called when the block containing this component gets broken.
	 * Default implementation is to mark the MultiBlock this is a
	 * part of as invalid
	 */
	default void onBreak()
	{
		getMultiBlock().markInvalid();
	}

	/**
	 * Called when a player interacts with this component
	 * @param player The player
	 */
	void onUse(PlayerEntity player);

	void tick();

	BlockPos getPos();

	World getWorld();

	void setWorld(World world);

	/**
	 * @return The MultiBlock this component is a part of
	 */
	MultiBlock<? extends MultiBlockComponent> getMultiBlock();

	/**
	 * @return The block outline shape for this component
	 */
	VoxelShape getOutline();

	/**
	 * Reads this component from the passed in tag
	 * @param tag The tag to read from
	 */
	void read(NbtCompound tag);

	/**
	 * Writes this component to the passed in tag
	 * @param tag The tag to write to
	 * @return The written tag
	 */
	NbtCompound write(NbtCompound tag);
}
