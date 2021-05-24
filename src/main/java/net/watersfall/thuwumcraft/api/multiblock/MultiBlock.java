package net.watersfall.thuwumcraft.api.multiblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Primary MultiBlock class that handles reading, writing, ticking,
 * and interacting with a multiblock
 * @param <T> The base component type that makes up this multiblock
 */
public interface MultiBlock<T extends MultiBlockComponent>
{
	/**
	 * Adds this MultiBlock instance to the world
	 * @param world The world to create in
	 * @param pos The pos of the main component
	 */
	void add(World world, BlockPos pos);

	/**
	 * Removes this MultiBlock instance from the world and reverts
	 * all blocks part of it to their original states
	 */
	void remove();

	/**
	 * Called when a player attempts to interact with this multiblock
	 * @param world The world the interaction occurs in
	 * @param pos The position of the interacted component
	 * @param player The player
	 */
	void onUse(World world, BlockPos pos, PlayerEntity player);

	/**
	 * Checks if this multiblock is still valid and fully formed,
	 * returning true if it is, or false otherwise
	 * @return True if valid
	 */
	boolean isValid();

	/**
	 * Marks this multiblock as invalid, indicating that it should stop
	 * functioning and destroy itself
	 */
	void markInvalid();

	/**
	 * Ticks the multiblock and it's components
	 */
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

	/**
	 * Reads the MultiBlock, it's components, and all other data from the passed in tag
	 * @param tag The tag to read from
	 */
	void read(NbtCompound tag);

	/**
	 * Writes the MultiBlock, it's components, and all other data to the passed in tag
	 * @param tag The CompoundTag to write to
	 * @return The written to tag
	 */
	NbtCompound write(NbtCompound tag);

	/**
	 * Marks that this MultiBlock has been changed and needs to be saved
	 */
	void markDirty();

	/**
	 * @return The MultiBlockType of this MultiBlock
	 */
	MultiBlockType<? extends MultiBlock<? extends MultiBlockComponent>> getType();

	/**
	 * @return The pos of the main block
	 */
	BlockPos getPos();

	/**
	 * @return The world this MultiBlock is in
	 */
	World getWorld();

	/**
	 * @return An array containing all components
	 */
	T[] getComponents();
}
