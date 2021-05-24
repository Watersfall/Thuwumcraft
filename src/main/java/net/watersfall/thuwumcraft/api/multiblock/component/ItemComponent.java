package net.watersfall.thuwumcraft.api.multiblock.component;

import net.minecraft.inventory.SidedInventory;

/**
 * An interface for all MultiBlockComponents that have an Inventory
 */
public interface ItemComponent
{
	SidedInventory getInventory();
}
