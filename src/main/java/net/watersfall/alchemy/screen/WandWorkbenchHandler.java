package net.watersfall.alchemy.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.watersfall.alchemy.item.wand.WandCapItem;
import net.watersfall.alchemy.item.wand.WandCoreItem;
import net.watersfall.alchemy.item.wand.WandFocusItem;
import net.watersfall.alchemy.screen.slot.WandComponentSlot;
import net.watersfall.alchemy.screen.slot.WandSlot;

public class WandWorkbenchHandler extends ScreenHandler
{
	public WandWorkbenchHandler(int syncId, PlayerInventory inventory)
	{
		this(syncId, inventory, new SimpleInventory(4));
	}
	private WandSlot slot;
	private WandComponentSlot core, cap, focus;

	public WandWorkbenchHandler(int syncId, PlayerInventory playerInventory, Inventory wandInventory)
	{
		super(AlchemyScreenHandlers.WAND_WORKBENCH, syncId);

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
}
