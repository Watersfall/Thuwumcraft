package net.watersfall.alchemy.api.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * The type of a MultiBlock.
 * Handles checking if a structure is a valid MultiBlock of this type,
 * and creating MultiBlocks of this type
 * @param <T> the MultiBlock class matched and created
 */
public interface MultiBlockType<T extends MultiBlock<? extends MultiBlockComponent>>
{
	/**
	 * An empty BlockPos array representing an invalid match
	 */
	BlockPos[] MISSING = new BlockPos[0];

	/**
	 * @return A List of all valid BlockStates that can be
	 * interacted with to create this MultiBlock
	 */
	List<BlockState> getStartingPoints();

	/**
	 * Checks if the position is the start of a valid MultiBlock of this type
	 * @param player The player starting the check
	 * @param world The world
	 * @param pos The BlockPos the player interacted with
	 * @return An Array of all BlockPos that will make up this MultiBlock if
	 * it's a match, or the MISSING BlockPos array if it is invalid
	 */
	BlockPos[] matches(PlayerEntity player, World world, BlockPos pos);

	/**
	 * Creates a MultiBlock of this type in the location of the BlockPos array
	 * created from the matches method
	 * @param player The player who formed this MultiBlock
	 * @param world The world
	 * @param pos The interacted with BlockPos
	 * @param poses The BlockPos array containing all block positions this MultiBlock will fill
	 * @return The created MultiBlock
	 */
	MultiBlock<? extends MultiBlockComponent> create(PlayerEntity player, World world, BlockPos pos, BlockPos[] poses);
}
