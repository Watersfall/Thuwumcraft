package net.watersfall.thuwumcraft.multiblock.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.watersfall.thuwumcraft.api.multiblock.MultiBlock;

public interface AlchemicalFurnaceInventory extends SidedInventory
{
	DefaultedList<ItemStack> getContents();

	MultiBlock<?> getMultiBlock();

	@Override
	default int size()
	{
		return 9;
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
		return Inventories.splitStack(getContents(), slot, amount);
	}

	@Override
	default ItemStack removeStack(int slot)
	{
		return removeStack(slot, getStack(slot).getCount());
	}

	@Override
	default void setStack(int slot, ItemStack stack)
	{
		this.getContents().set(slot, stack);
	}

	@Override
	default void markDirty()
	{
		getMultiBlock().markDirty();
	}

	@Override
	default boolean canPlayerUse(PlayerEntity player)
	{
		return false;
	}

	@Override
	default void clear()
	{
		this.getContents().clear();
	}

	void sync();
}
