package net.watersfall.alchemy.inventory.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.multiblock.impl.inventory.AlchemicalFurnaceInput;
import net.watersfall.alchemy.multiblock.impl.inventory.AlchemicalFurnaceOutput;
import org.jetbrains.annotations.Nullable;

public class AlchemicalFurnaceHandler extends ScreenHandler
{
	private Inventory input;
	private Inventory output;
	private PropertyDelegate properties;

	public AlchemicalFurnaceHandler(int syncId, PlayerInventory playerInventory)
	{
		this(syncId, playerInventory, new SimpleInventory(9), new SimpleInventory(9), new ArrayPropertyDelegate(4));
	}

	public AlchemicalFurnaceHandler(int syncId, PlayerInventory playerInventory, Inventory input, Inventory output, PropertyDelegate properties)
	{
		super(AlchemyMod.ALCHEMICAL_FURNACE_HANDLER, syncId);
		this.input = input;
		this.output = output;
		this.properties = properties;
		this.addProperties(properties);
		for(int y = 0; y < 3; y++)
		{
			for(int x = 2; x < 5; x++)
			{
				this.addSlot(new Slot(input, y * 3 + x - 2, 8 + x * 18, 18 + y * 18));
			}
		}
		for(int y = 0; y < 3; y++)
		{
			for(int x = 6; x < 9; x++)
			{
				this.addSlot(new FurnaceOutputSlot(playerInventory.player, output, y * 3 + x - 6, 8 + x * 18, 18 + y * 18));
			}
		}

		//Player Inventory
		int m;
		int l;
		for (m = 0; m < 3; ++m)
		{
			for (l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 86 + m * 18));
			}
		}
		for (m = 0; m < 9; ++m)
		{
			this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 144));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return true;
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index)
	{
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasStack())
		{
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index < 18)
			{
				if (!this.insertItem(itemStack2, 18, this.slots.size(), true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.insertItem(itemStack2, 0, 9, false))
			{
				return ItemStack.EMPTY;
			}
			if (itemStack2.isEmpty())
			{
				slot.setStack(ItemStack.EMPTY);
			}
			slot.markDirty();
		}
		return itemStack;
	}

	public int getSmeltingProgress()
	{
		return (int)((float)this.properties.get(0) / (float)this.properties.get(1) * 16F + 0.5F);
	}

	public int getFuel()
	{
		return (int)((float)this.properties.get(2) / (float)this.properties.get(3) * 12F);
	}
}
