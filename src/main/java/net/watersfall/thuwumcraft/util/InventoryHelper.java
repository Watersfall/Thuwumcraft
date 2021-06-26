package net.watersfall.thuwumcraft.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

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

	public static void useItem(ItemStack stack, PlayerEntity player, Hand hand, int count)
	{
		useItem(stack, player, hand, count, ItemStack.EMPTY);
	}

	public static void useItem(ItemStack stack, PlayerEntity player, Hand hand, int count, ItemStack replacementStack)
	{
		stack.decrement(1);
		if(stack.isEmpty())
		{
			player.setStackInHand(hand, replacementStack);
		}
		else if(!player.getInventory().insertStack(replacementStack))
		{
			player.dropItem(replacementStack, false);
		}
	}
}
