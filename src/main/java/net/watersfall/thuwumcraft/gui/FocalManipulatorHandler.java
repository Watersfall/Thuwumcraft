package net.watersfall.thuwumcraft.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.watersfall.thuwumcraft.registry.ThuwumcraftScreenHandlers;

public class FocalManipulatorHandler extends ScreenHandler
{
	public FocalManipulatorHandler(int syncId, PlayerInventory playerInventory)
	{
		this(syncId, playerInventory, new SimpleInventory(1));
	}

	public FocalManipulatorHandler(int syncId, PlayerInventory playerInventory, Inventory inventory)
	{
		super(ThuwumcraftScreenHandlers.FOCAL_MANIPULATOR, syncId);

		this.addSlot(new Slot(inventory, 0, 8, 14));

		//Player Inventory
		for (int y = 0; y < 3; ++y)
		{
			for (int x = 0; x < 9; ++x)
			{
				this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 140 + y * 18));
			}
		}
		for (int y = 0; y < 9; ++y)
		{
			this.addSlot(new Slot(playerInventory, y, 8 + y * 18, 198));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return true;
	}
}
