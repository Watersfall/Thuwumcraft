package net.watersfall.thuwumcraft.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class ApothecaryGuideInventory implements Inventory
{
	private final DefaultedList<ItemStack> contents = DefaultedList.ofSize(1, ItemStack.EMPTY);
	private final ItemStack stack;

	public ApothecaryGuideInventory(ItemStack stack)
	{
		this.stack = stack;
		NbtCompound tag = stack.getSubNbt("Items");

		if (tag != null) {
			Inventories.readNbt(tag, contents);
		}
	}

	public DefaultedList<ItemStack> getContents()
	{
		return contents;
	}

	@Override
	public int size()
	{
		return 1;
	}

	@Override
	public boolean isEmpty()
	{
		return getContents().get(0) == ItemStack.EMPTY;
	}

	@Override
	public ItemStack getStack(int slot)
	{
		return getContents().get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount)
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
	public ItemStack removeStack(int slot)
	{
		return getContents().set(slot, ItemStack.EMPTY);
	}

	@Override
	public void setStack(int slot, ItemStack stack)
	{
		if(stack.getCount() > getMaxCountPerStack())
		{
			getContents().set(slot, new ItemStack(stack.getItem()));
			stack.decrement(1);
		}
		else if(stack != ItemStack.EMPTY)
		{
			getContents().set(slot, stack);
		}
	}

	@Override
	public int getMaxCountPerStack()
	{
		return 1;
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player)
	{
		return true;
	}

	@Override
	public boolean isValid(int slot, ItemStack stack)
	{
		return false;
	}

	@Override
	public void clear()
	{
		getContents().set(0, ItemStack.EMPTY);
	}

	@Override
	public void markDirty()
	{
		NbtCompound tag = stack.getOrCreateSubNbt("Items");
		Inventories.writeNbt(tag, contents);
	}
}
