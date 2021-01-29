package com.watersfall.alchemy.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface PedestalInventory extends Inventory
{
	ItemStack getStack();

	void setStack(ItemStack stack);

	@Override
	default int size()
	{
		return 1;
	}

	@Override
	default boolean isEmpty()
	{
		return getStack().isEmpty();
	}

	@Override
	default ItemStack getStack(int slot)
	{
		if(slot == 0)
		{
			return getStack().copy();
		}
		else
		{
			return ItemStack.EMPTY;
		}
	}

	@Override
	default ItemStack removeStack(int slot, int amount)
	{
		return removeStack(slot);
	}

	@Override
	default ItemStack removeStack(int slot)
	{
		ItemStack returnStack = getStack().copy();
		this.setStack(slot, ItemStack.EMPTY);
		return returnStack;
	}

	@Override
	default void setStack(int slot, ItemStack stack)
	{
		if(slot == 0)
		{
			setStack(stack);
		}
	}

	@Override
	default int getMaxCountPerStack()
	{
		return 1;
	}

	@Override
	default boolean canPlayerUse(PlayerEntity player)
	{
		return true;
	}

	@Override
	default void clear()
	{
		setStack(ItemStack.EMPTY);
	}
}
