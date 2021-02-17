package net.watersfall.alchemy.api.multiblock.component;

import net.minecraft.inventory.SidedInventory;

/**
 * An interface for all MultiBlockComponents that have an Inventory
 */
public interface ItemComponent
{
	SidedInventory getInventory();
}
