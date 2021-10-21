package net.watersfall.thuwumcraft.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.watersfall.thuwumcraft.inventory.BetterAspectInventory;
import net.watersfall.thuwumcraft.registry.ThuwumcraftScreenHandlers;

public class EssentiaSmelteryHandler extends ScreenHandler
{
	private final PropertyDelegate properties;

	public EssentiaSmelteryHandler(int syncId, PlayerInventory playerInventory)
	{
		this(syncId, playerInventory, new SimpleInventory(2), null, new ArrayPropertyDelegate(2));
	}

	public EssentiaSmelteryHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, BetterAspectInventory aspectInventory, PropertyDelegate properties)
	{
		super(ThuwumcraftScreenHandlers.ESSENTIA_SMELTERY_HANDLER, syncId);
		this.addProperties(properties);
		this.properties = properties;
		this.addSlot(new Slot(inventory, 0, 56, 17));

		int l;
		for(l = 0; l < 3; ++l)
		{
			for(int k = 0; k < 9; ++k)
			{
				this.addSlot(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, 84 + l * 18));
			}
		}

		for(l = 0; l < 9; ++l)
		{
			this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return true;
	}

	public int getAspectCount()
	{
		return properties.get(0);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index)
	{
		Slot slot = slots.get(index);
		if(slot.hasStack())
		{
			if(index == 0)
			{
				if(!this.insertItem(slot.getStack(), 1, 37, true))
				{
					return ItemStack.EMPTY;
				}
			}
			else
			{
				Slot input = slots.get(0);
				ItemStack stack = slot.getStack();
				if(input.canInsert(stack))
				{
					input.insertStack(stack);
					return ItemStack.EMPTY;
				}
			}
		}
		return ItemStack.EMPTY;
	}
}
