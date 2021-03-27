package net.watersfall.alchemy.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.item.CrystalItem;

public class CrystalSlot extends Slot
{
	private final Aspect aspect;

	public CrystalSlot(Inventory inventory, int index, int x, int y, Aspect aspect)
	{
		super(inventory, index, x, y);
		this.aspect = aspect;
	}

	@Override
	public boolean canInsert(ItemStack stack)
	{
		return (stack.getItem() instanceof CrystalItem) && ((CrystalItem)stack.getItem()).getAspect() == this.aspect;
	}
}
