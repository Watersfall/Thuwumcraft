package net.watersfall.thuwumcraft.inventory;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import net.watersfall.thuwumcraft.api.aspect.Aspect;
import net.watersfall.thuwumcraft.api.aspect.Aspects;

public class AspectCraftingInventory extends CraftingInventory
{
	public static final int[] CRAFTING_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
	public static final int[] CRYSTAL_SLOTS = new int[]{0, 1, 2, 3, 4, 5};

	public CraftingInventory crystals;
	public CraftingResultInventory output;

	public AspectCraftingInventory(ScreenHandler handler)
	{
		super(handler, 3, 3);
		this.crystals = new CraftingInventory(handler, 3, 2);
		this.output = new CraftingResultInventory();
	}

	public void load(DefaultedList<ItemStack> contents)
	{
		for(int i = 0; i < 9; i++)
		{
			this.stacks.set(i, contents.get(i));
		}
		for(int i = 0; i < 6; i++)
		{
			this.crystals.stacks.set(i, contents.get(i + 9));
		}
		this.output.setStack(0, contents.get(15));
	}

	public void save(DefaultedList<ItemStack> contents)
	{
		for(int i = 0; i < this.stacks.size(); i++)
		{
			contents.set(i, this.stacks.get(i));
		}
		for(int i = 9; i < this.crystals.size() + 9; i++)
		{
			contents.set(i, this.crystals.getStack(i - 9));
		}
		contents.set(15, this.output.getStack(0));
	}

	public ItemStack getStack(Aspect aspect)
	{
		if(aspect == Aspects.AIR)
			return crystals.getStack(CRYSTAL_SLOTS[0]);
		if(aspect == Aspects.WATER)
			return crystals.getStack(CRYSTAL_SLOTS[1]);
		if(aspect == Aspects.EARTH)
			return crystals.getStack(CRYSTAL_SLOTS[2]);
		if(aspect == Aspects.FIRE)
			return crystals.getStack(CRYSTAL_SLOTS[3]);
		if(aspect == Aspects.ORDER)
			return crystals.getStack(CRYSTAL_SLOTS[4]);
		else
			return crystals.getStack(CRYSTAL_SLOTS[5]);
	}
}