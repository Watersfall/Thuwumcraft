package net.watersfall.alchemy.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface BrewingCauldronInventory extends Inventory
{
	DefaultedList<ItemStack> getContents();

	DefaultedList<ItemStack> getInput();

	@Override
	default int size()
	{
		return getContents().size();
	}

	@Override
	default boolean isEmpty()
	{
		for(int i = 0; i < getContents().size(); i++)
		{
			if(getContents().get(i) != ItemStack.EMPTY)
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
		ItemStack stack = getContents().get(slot);
		ItemStack returnStack = ItemStack.EMPTY;
		if(!stack.isEmpty())
		{
			if(stack.getCount() > amount)
			{
				amount = stack.getCount();
			}
			returnStack = new ItemStack(stack.getItem(), amount);
			stack.decrement(amount);
		}
		return returnStack;
	}

	@Override
	default ItemStack removeStack(int slot)
	{
		return getContents().set(slot, ItemStack.EMPTY);
	}

	@Override
	default void setStack(int slot, ItemStack stack)
	{
		getContents().set(slot, stack);
	}

	@Override
	default int getMaxCountPerStack()
	{
		return 1;
	}

	@Override
	default void markDirty()
	{
	}

	@Override
	default boolean canPlayerUse(PlayerEntity player)
	{
		return false;
	}

	@Override
	default boolean isValid(int slot, ItemStack stack)
	{
		return true;
	}

	@Override
	default void clear()
	{
		for(int i = 0; i < getContents().size(); i++)
		{
			getContents().set(i, ItemStack.EMPTY);
		}
	}

	default void setInput(ItemStack stack)
	{
		getInput().set(0, stack);
	}

	default boolean addStack(ItemStack stack)
	{
		for(int i = 0; i < getContents().size(); i++)
		{
			if(getContents().get(i) == ItemStack.EMPTY)
			{
				setStack(i, stack);
				return true;
			}
		}
		return false;
	}

	default boolean addItem(Item item)
	{
		for(int i = 0; i < getContents().size(); i++)
		{
			if(getStack(i).getItem() == item)
			{
				if(getStack(i).getCount() < item.getMaxCount())
				{
					getStack(i).increment(1);
					return true;
				}
			}
		}
		return false;
	}

	default byte getIngredientCount()
	{
		byte count = 0;
		for(int i = 0; i < this.size(); i++)
		{
			if(!getStack(i).isEmpty())
			{
				count++;
			}
		}
		return count;
	}

	default BrewingCauldronInventory withInput(int slot)
	{
		this.setInput(this.getStack(slot));
		return this;
	}
}
