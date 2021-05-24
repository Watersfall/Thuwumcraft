package net.watersfall.thuwumcraft.multiblock.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.watersfall.thuwumcraft.api.multiblock.MultiBlock;
import org.jetbrains.annotations.Nullable;

public class AlchemicalFurnaceFuel implements SidedInventory
{
	private static final int[] AVAILABLE_SLOTS = new int[]{0};
	private final DefaultedList<ItemStack> contents;
	private final MultiBlock<?> multiBlock;

	public AlchemicalFurnaceFuel(MultiBlock<?> multiBlock)
	{
		this.contents = DefaultedList.ofSize(1, ItemStack.EMPTY);
		this.multiBlock = multiBlock;
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

	@Override
	public int size()
	{
		return 1;
	}

	@Override
	public boolean isEmpty()
	{
		return getStack(0).isEmpty();
	}

	@Override
	public ItemStack getStack(int slot)
	{
		return contents.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount)
	{
		ItemStack stack = contents.get(slot);
		if(amount >= stack.getCount())
		{
			return removeStack(slot);
		}
		else
		{
			stack.decrement(amount);
			return new ItemStack(stack.getItem(), amount);
		}
	}

	@Override
	public ItemStack removeStack(int slot)
	{
		ItemStack stack = contents.get(slot);
		contents.set(slot, ItemStack.EMPTY);
		return stack;
	}

	@Override
	public void setStack(int slot, ItemStack stack)
	{
		this.contents.set(slot, stack);
	}

	@Override
	public void markDirty()
	{
		this.multiBlock.markDirty();
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player)
	{
		return true;
	}

	@Override
	public void clear()
	{
		this.contents.clear();
	}

	public DefaultedList<ItemStack> getContents()
	{
		return this.contents;
	}
}
