package net.watersfall.alchemy.inventory.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.watersfall.alchemy.AlchemyMod;
import org.jetbrains.annotations.Nullable;

public class AlchemicalFurnaceHandler extends ScreenHandler
{
	private Inventory input;
	private Inventory output;

	public AlchemicalFurnaceHandler(int syncId, PlayerInventory playerInventory)
	{
		this(syncId, playerInventory, new SimpleInventory(9), new SimpleInventory(9));
	}

	public AlchemicalFurnaceHandler(int syncId, PlayerInventory playerInventory, Inventory input, Inventory output)
	{
		super(AlchemyMod.ALCHEMICAL_FURNACE_HANDLER, syncId);
		this.input = input;
		this.output = output;

		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 3; x++)
			{
				this.addSlot(new Slot(input, y * 3 + x, 8 + x * 18, 18 + y * 18));
			}
		}
		for(int y = 0; y < 3; y++)
		{
			for(int x = 6; x < 9; x++)
			{
				this.addSlot(new Slot(output, y * 3 + x - 6, 8 + x * 18, 18 + y * 18));
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
}
