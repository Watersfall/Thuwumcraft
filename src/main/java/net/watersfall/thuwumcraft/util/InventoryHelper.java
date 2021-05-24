package net.watersfall.thuwumcraft.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class InventoryHelper
{
	public static boolean fit(Inventory inventory, ItemStack stack)
	{
		for(int i = 0; i < inventory.size(); i++)
		{
			ItemStack invStack = inventory.getStack(i);
			if(invStack.isEmpty())
			{
				inventory.setStack(i, stack);
				return true;
			}
			else if(invStack.getItem() == stack.getItem())
			{
				if(invStack.getCount() < invStack.getItem().getMaxCount())
				{
					if(invStack.getCount() + stack.getCount() > invStack.getItem().getMaxCount())
					{
						int decrement = invStack.getItem().getMaxCount() - stack.getCount();
						inventory.setStack(i, new ItemStack(invStack.getItem(), invStack.getItem().getMaxCount()));
						stack.decrement(decrement);
						return true;
					}
					else
					{
						inventory.setStack(i, new ItemStack(invStack.getItem(), invStack.getCount() + stack.getCount()));
						stack.decrement(stack.getCount());
						return true;
					}
				}
			}
		}
		return false;
	}
}
