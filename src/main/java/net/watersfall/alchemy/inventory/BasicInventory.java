package net.watersfall.alchemy.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface BasicInventory extends Inventory
{
	DefaultedList<ItemStack> getContents();

	@Override
	default int size()
	{
		return getContents().size();
	}

	@Override
	default boolean isEmpty()
	{
		for(int i = 0; i < this.size(); i++)
		{
			if(!this.getStack(i).isEmpty())
			{
				return false;
			}
		}
		return true;
	}

	@Override
	default ItemStack getStack(int slot)
	{
		return getContents().get(slot);
	}

	@Override
	default ItemStack removeStack(int slot, int amount)
	{
		ItemStack currentStack = getContents().get(slot);
		ItemStack returnStack = new ItemStack(currentStack.getItem(), amount);
		currentStack.decrement(amount);
		return returnStack;
	}

	@Override
	default ItemStack removeStack(int slot)
	{
		ItemStack stack = getContents().get(slot);
		setStack(slot, ItemStack.EMPTY);
		return stack;
	}

	@Override
	default void setStack(int slot, ItemStack stack)
	{
		getContents().set(slot, stack);
	}

	@Override
	default int getMaxCountPerStack()
	{
		return 64;
	}

	@Override
	default boolean canPlayerUse(PlayerEntity player)
	{
		return true;
	}

	@Override
	default void clear()
	{
		getContents().clear();
	}
}
