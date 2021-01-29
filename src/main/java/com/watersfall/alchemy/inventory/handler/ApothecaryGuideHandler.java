package com.watersfall.alchemy.inventory.handler;

import com.watersfall.alchemy.AlchemyMod;
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
		this.addSlot(new IngredientSlot(inventory, 0, 26, 35));

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
		this.inventory.clear();
		this.inventory.markDirty();
		this.inventory.onClose(player);
	}

	@Override
	public ItemStack onSlotClick(int i, int j, SlotActionType actionType, PlayerEntity playerEntity)
	{
		if(i == 0)
		{
			Slot slot = this.slots.get(i);
			slot.setStack(playerEntity.inventory.getCursorStack());
			return ItemStack.EMPTY;
		}
		else
		{
			return super.onSlotClick(i, j, actionType, playerEntity);
		}
	}

	public static class IngredientSlot extends Slot
	{
		private final int index;

		public IngredientSlot(Inventory inventory, int index, int x, int y)
		{
			super(inventory, index, x, y);
			this.index = index;
		}

		@Override
		public boolean canInsert(ItemStack stack)
		{
			return super.canInsert(stack) && stack.getItem().isIn(AlchemyMod.getIngredientTag());
		}

		@Override
		public int getMaxItemCount()
		{
			return 1;
		}

		@Override
		public ItemStack takeStack(int amount)
		{
			this.inventory.clear();
			return ItemStack.EMPTY;
		}

		@Override
		public void onStackChanged(ItemStack originalItem, ItemStack itemStack)
		{

		}

		@Override
		public ItemStack onTakeItem(PlayerEntity player, ItemStack stack)
		{
			return super.onTakeItem(player, stack);
		}

		@Override
		public void setStack(ItemStack stack)
		{
			if(stack == ItemStack.EMPTY)
			{
				this.inventory.clear();
			}
			else
			{
				if(this.inventory.getStack(this.index).getItem() == stack.getItem())
				{
					this.inventory.clear();
				}
				else
				{
					this.inventory.setStack(this.index, new ItemStack(stack.getItem()));
				}
			}
			this.markDirty();
			this.inventory.markDirty();
		}
	}
}
