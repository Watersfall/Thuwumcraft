package net.watersfall.thuwumcraft.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class NekomancyOutputSlot extends Slot
{
	private int amount;

	public NekomancyOutputSlot(Inventory inventory, int index, int x, int y)
	{
		super(inventory, index, x, y);
		amount = 0;
	}

	@Override
	public boolean canInsert(ItemStack stack)
	{
		return false;
	}

	public ItemStack takeStack(int amount)
	{
		if (this.hasStack())
		{
			this.amount += Math.min(amount, this.getStack().getCount());
		}
		return super.takeStack(amount);
	}

	protected void onCrafted(ItemStack stack, int amount)
	{
		this.amount += amount;
		this.onCrafted(stack);
	}

	@Override
	protected void onCrafted(ItemStack stack)
	{
		super.onCrafted(stack);
		amount = 0;
	}
}
