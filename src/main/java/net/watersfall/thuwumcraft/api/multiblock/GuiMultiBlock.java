package net.watersfall.thuwumcraft.api.multiblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A MultiBlock that has a GUI
 * @param <T> The MultiBlockComponent type this MultiBlock is made of
 */
public interface GuiMultiBlock<T extends MultiBlockComponent> extends MultiBlock<T>, NamedScreenHandlerFactory
{
	/**
	 * Opens the GUI for this MultiBlock
	 * @param world The world
	 * @param pos The pos of the component interacted with
	 * @param player The player interacting with this MultiBlock
	 */
	void openScreen(World world, BlockPos pos, PlayerEntity player);
}
