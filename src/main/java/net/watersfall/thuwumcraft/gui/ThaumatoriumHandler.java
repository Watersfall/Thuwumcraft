package net.watersfall.thuwumcraft.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.watersfall.thuwumcraft.block.entity.ThaumatoriumBlockEntity;
import net.watersfall.thuwumcraft.registry.ThuwumcraftScreenHandlers;

public class ThaumatoriumHandler extends ScreenHandler
{
	private ThaumatoriumBlockEntity entity;

	public ThaumatoriumHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf)
	{
		this(syncId, playerInventory, (ThaumatoriumBlockEntity)playerInventory.player.world.getBlockEntity(buf.readBlockPos()));
	}

	public ThaumatoriumHandler(int syncId, PlayerInventory playerInventory, ThaumatoriumBlockEntity entity)
	{
		super(ThuwumcraftScreenHandlers.THAUMATORIUM, syncId);
		this.entity = entity;
		this.addSlot(new Slot(entity, 0, 14, 37));
		this.addSlot(new Slot(entity, 1, 144, 37));
		this.addSlot(new Slot(entity, 2, 89, 37));

		//Player Inventory
		int m;
		int l;
		for (m = 0; m < 3; ++m)
		{
			for (l = 0; l < 9; ++l)
			{
				this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 98 + m * 18));
			}
		}
		for (m = 0; m < 9; ++m)
		{
			this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 156));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return true;
	}

	public ThaumatoriumBlockEntity getEntity()
	{
		return entity;
	}

	@Override
	public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player)
	{
		if(slotIndex != 2)
		{
			super.onSlotClick(slotIndex, button, actionType, player);
		}
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index)
	{
		if(index != 1)
		{
			Slot slot = slots.get(index);
			if(index < 3)
			{
				if (!this.insertItem(slot.getStack(), 18, this.slots.size(), true))
				{
					return ItemStack.EMPTY;
				}
			}
			else
			{
				Slot input = slots.get(0);
				input.insertStack(slot.getStack());
				return ItemStack.EMPTY;
			}
		}
		return ItemStack.EMPTY;
	}
}
