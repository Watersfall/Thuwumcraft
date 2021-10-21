package net.watersfall.thuwumcraft.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.watersfall.thuwumcraft.item.wand.WandCapItem;
import net.watersfall.thuwumcraft.item.wand.WandCoreItem;
import net.watersfall.thuwumcraft.item.wand.WandFocusItem;
import net.watersfall.thuwumcraft.gui.slot.WandComponentSlot;
import net.watersfall.thuwumcraft.gui.slot.WandSlot;
import net.watersfall.thuwumcraft.item.wand.WandItem;
import net.watersfall.thuwumcraft.registry.ThuwumcraftScreenHandlers;

public class WandWorkbenchHandler extends ScreenHandler
{
	private final Inventory wandInventory = new SimpleInventory(4);
	private WandSlot slot;
	private WandComponentSlot core, cap, focus;

	public WandWorkbenchHandler(int syncId, PlayerInventory playerInventory)
	{
		super(ThuwumcraftScreenHandlers.WAND_WORKBENCH, syncId);

		slot = new WandSlot(wandInventory, 0, 34, 42, () -> core, () -> cap, () -> focus);
		core = new WandComponentSlot(wandInventory, 1, 9, 17, slot, WandCoreItem.class);
		cap = new WandComponentSlot(wandInventory, 2, 9, 67, slot, WandCapItem.class);
		focus = new WandComponentSlot(wandInventory, 3, 59, 17, slot, WandFocusItem.class);
		this.addSlot(slot);
		this.addSlot(core);
		this.addSlot(cap);
		this.addSlot(focus);

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

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index)
	{
		Slot slot = slots.get(index);
		if(index < 4)
		{
			ItemStack original = slot.getStack();
			if(!this.insertItem(slot.getStack(), 1, 37, true))
			{
				return ItemStack.EMPTY;
			}
			slot.onQuickTransfer(slot.getStack(), original);
		}
		else if(slot.hasStack())
		{
			if(slot.getStack().getItem() instanceof WandCoreItem && !core.hasStack())
			{
				core.insertStack(slot.getStack());
				return ItemStack.EMPTY;
			}
			else if(slot.getStack().getItem() instanceof WandCapItem && !cap.hasStack())
			{
				cap.insertStack(slot.getStack());
				return ItemStack.EMPTY;
			}
			else if(slot.getStack().getItem() instanceof WandFocusItem && !focus.hasStack())
			{
				focus.insertStack(slot.getStack());
				return ItemStack.EMPTY;
			}
			else if(slot.getStack().getItem() instanceof WandItem && !this.slot.hasStack())
			{
				this.slot.insertStack(slot.getStack());
				return ItemStack.EMPTY;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void close(PlayerEntity player)
	{
		super.close(player);
		this.dropInventory(player, new SimpleInventory(slot.getStack()));
	}
}
