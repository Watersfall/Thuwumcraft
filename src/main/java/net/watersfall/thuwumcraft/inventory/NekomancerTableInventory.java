package net.watersfall.thuwumcraft.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.watersfall.thuwumcraft.api.aspect.Aspect;
import net.watersfall.thuwumcraft.api.aspect.Aspects;

public class NekomancerTableInventory implements BasicInventory
{
	private final DefaultedList<ItemStack> contents;

	public NekomancerTableInventory()
	{
		contents = DefaultedList.ofSize(14, ItemStack.EMPTY);
	}

	@Override
	public DefaultedList<ItemStack> getContents()
	{
		return this.contents;
	}

	@Override
	public void markDirty() { }

	public int[] getCraftingSlots()
	{
		return new int[]{0,1,2,3,4,5,6,7,8};
	}

	public int[] getCrystalSlots()
	{
		return new int[]{9,10,11,12};
	}

	public int[] getOutputSlots()
	{
		return new int[]{13};
	}

	public ItemStack getCrystalSlot(Aspect aspect)
	{
		if(aspect == Aspects.AIR)
			return this.getStack(9);
		else if(aspect == Aspects.WATER)
			return this.getStack(10);
		else if(aspect == Aspects.FIRE)
			return this.getStack(11);
		else
			return this.getStack(12);
	}

	public ItemStack getOutput()
	{
		return this.getStack(13);
	}
}
