package com.watersfall.alchemy.multiblock.impl.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class AlchemicalFurnaceInput implements AlchemicalFurnaceInventory
{
	private static final int[] AVAILABLE_SLOTS = new int[]{0,1,2,3,4,5,6,7,8};
	private final DefaultedList<ItemStack> contents;

	public AlchemicalFurnaceInput()
	{
		contents = DefaultedList.ofSize(9, ItemStack.EMPTY);
	}

	@Override
	public DefaultedList<ItemStack> getContents()
	{
		return this.contents;
	}

	@Override
	public int[] getAvailableSlots(Direction side)
	{
		return AVAILABLE_SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir)
	{
		return true;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir)
	{
		return false;
	}
}
