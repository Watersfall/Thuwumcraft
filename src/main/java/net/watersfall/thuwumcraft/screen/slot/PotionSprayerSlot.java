package net.watersfall.thuwumcraft.screen.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class PotionSprayerSlot extends Slot
{
	public PotionSprayerSlot(Inventory inventory, int index, int x, int y)
	{
		super(inventory, index, x, y);
	}

	@Override
	public boolean canInsert(ItemStack stack)
	{
		return true;
	}

	@Override
	public void onTakeItem(PlayerEntity player, ItemStack stack)
	{
		stack.setCount(0);
		super.onTakeItem(player, stack);
	}
}
