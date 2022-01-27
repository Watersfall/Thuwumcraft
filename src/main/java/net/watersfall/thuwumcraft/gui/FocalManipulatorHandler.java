package net.watersfall.thuwumcraft.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.watersfall.thuwumcraft.api.abilities.item.WandFocusAbility;
import net.watersfall.thuwumcraft.block.entity.BlockEntityClientSerializable;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;
import net.watersfall.thuwumcraft.registry.ThuwumcraftScreenHandlers;
import net.watersfall.wet.api.abilities.AbilityProvider;

public class FocalManipulatorHandler extends ScreenHandler
{
	public FocalManipulatorHandler(int syncId, PlayerInventory playerInventory)
	{
		this(syncId, playerInventory, new SimpleInventory(1));
	}

	public FocalManipulatorHandler(int syncId, PlayerInventory playerInventory, Inventory inventory)
	{
		super(ThuwumcraftScreenHandlers.FOCAL_MANIPULATOR, syncId);

		this.addSlot(new Slot(inventory, 0, 8, 14){
			@Override
			public boolean canInsert(ItemStack stack)
			{
				if(stack.isOf(ThuwumcraftItems.WAND_FOCUS))
				{
					return !AbilityProvider.getAbility(stack, WandFocusAbility.ID, WandFocusAbility.class).isPresent();
				}
				return false;
			}

			@Override
			public void markDirty()
			{
				super.markDirty();
				if(this.inventory instanceof BlockEntityClientSerializable entity && !playerInventory.player.world.isClient)
				{
					entity.sync();
				}
			}
		});

		//Player Inventory
		for (int y = 0; y < 3; ++y)
		{
			for (int x = 0; x < 9; ++x)
			{
				this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 142 + y * 18));
			}
		}
		for (int y = 0; y < 9; ++y)
		{
			this.addSlot(new Slot(playerInventory, y, 8 + y * 18, 200));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return true;
	}
}
