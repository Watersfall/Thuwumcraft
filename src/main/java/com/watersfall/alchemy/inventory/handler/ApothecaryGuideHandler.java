package com.watersfall.alchemy.inventory.handler;

import com.watersfall.alchemy.AlchemyMod;
import com.watersfall.alchemy.item.ApothecaryGuideItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class ApothecaryGuideHandler extends ScreenHandler
{
	Inventory inventory;

	public ApothecaryGuideHandler(int syncId, PlayerInventory playerInventory)
	{
		this(syncId, playerInventory, new SimpleInventory(1));
	}

	public ApothecaryGuideHandler(int syncId, PlayerInventory playerInventory, Inventory inventory)
	{
		super(AlchemyMod.APOTHECARY_GUIDE_HANDLER, syncId);
		checkSize(inventory, 1);
		this.inventory = inventory;
		this.addSlot(new Slot(inventory, 0, 26, 35)
		{
			public int getMaxItemCount()
			{
				return 1;
			}
		});

		//Player Inventory
		int m;
		int l;
		for (m = 0; m < 3; ++m)
		{
			for (l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
			}
		}
		for (m = 0; m < 9; ++m)
		{
			this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
		}
	}

	@Override
	public ScreenHandlerType<?> getType()
	{
		return AlchemyMod.APOTHECARY_GUIDE_HANDLER;
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return true;
	}

	@Override
	public void close(PlayerEntity player)
	{
		super.close(player);
		this.inventory.onClose(player);
	}

	@Override
	public ItemStack onSlotClick(int slotId, int clickData, SlotActionType actionType, PlayerEntity playerEntity)
	{
		if(slotId >= 0)
		{
			ItemStack stack = getSlot(slotId).getStack();
			if (stack.getItem() instanceof ApothecaryGuideItem)
			{
				return stack;
			}
		}
		return super.onSlotClick(slotId, clickData, actionType, playerEntity);
	}
}
