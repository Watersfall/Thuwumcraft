package net.watersfall.alchemy.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public interface PedestalInventory extends SidedInventory
{
	public static final int[] ARRAY = new int[]{0};

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

	@Override
	default boolean isValid(int slot, ItemStack stack)
	{
		if(slot != 0)
		{
			return false;
		}
		else
		{
			return this.getStack().isEmpty();
		}
	}

	@Override
	default int[] getAvailableSlots(Direction side)
	{
		return ARRAY;
	}

	@Override
	default boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir)
	{
		if(slot != 0)
		{
			return false;
		}
		else
		{
			return this.getStack().isEmpty();
		}
	}
}
