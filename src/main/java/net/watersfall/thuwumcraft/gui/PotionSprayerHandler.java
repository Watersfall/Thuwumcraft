package net.watersfall.thuwumcraft.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.watersfall.thuwumcraft.gui.slot.PotionSprayerSlot;
import net.watersfall.thuwumcraft.registry.ThuwumcraftScreenHandlers;

public class PotionSprayerHandler extends ScreenHandler
{
	public PotionSprayerHandler(int syncId, PlayerInventory playerInventory)
	{
		this(syncId, playerInventory, new SimpleInventory(3), new SimpleInventory(1));
	}

	public PotionSprayerHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, Inventory potion)
	{
		super(ThuwumcraftScreenHandlers.POTION_SPRAYER_HANDLER, syncId);
		this.addSlot(new PotionSprayerSlot(potion, 0, 17, 17));
		for(int i = 0; i < 3; i++)
		{
			this.addSlot(new Slot(inventory, i, 53 + i * 18, 17));
		}

		//Player Inventory
		int m;
		int l;
		for (m = 0; m < 3; ++m)
		{
			for (l = 0; l < 9; ++l)
			{
				this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 48 + m * 18));
			}
		}
		for (m = 0; m < 9; ++m)
		{
			this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 106));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return true;
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index)
	{
		return ItemStack.EMPTY;
	}
}
