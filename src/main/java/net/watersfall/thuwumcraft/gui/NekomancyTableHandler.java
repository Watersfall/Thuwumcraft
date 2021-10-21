package net.watersfall.thuwumcraft.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.aspect.Aspect;
import net.watersfall.thuwumcraft.api.aspect.Aspects;
import net.watersfall.thuwumcraft.inventory.NekomancerTableInventory;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRecipes;
import net.watersfall.thuwumcraft.recipe.NekomancyRecipe;
import net.watersfall.thuwumcraft.gui.slot.CrystalSlot;
import net.watersfall.thuwumcraft.gui.slot.NekomancyOutputSlot;
import net.watersfall.thuwumcraft.registry.ThuwumcraftScreenHandlers;

import java.util.Optional;

public class NekomancyTableHandler extends ScreenHandler
{
	private static final int[] crystalX = new int[]{17, 53, 53, 89};
	private static final int[] crystalY = new int[]{51, 15, 87, 51};
	private static final Aspect[] crystalAspect = new Aspect[]{Aspects.AIR, Aspects.WATER, Aspects.FIRE, Aspects.EARTH};
	private final NekomancerTableInventory inventory;

	public NekomancyTableHandler(int syncId, PlayerInventory playerInventory)
	{
		this(syncId, playerInventory, new NekomancerTableInventory());
	}

	public NekomancyTableHandler(int syncId, PlayerInventory playerInventory, NekomancerTableInventory inventory)
	{
		super(ThuwumcraftScreenHandlers.NEKOMANCY_TABLE_HANDLER, syncId);
		this.inventory = inventory;

		//This inventory
		for(int i = 0; i < inventory.getOutputSlots().length; i++)
		{
			this.addSlot(new NekomancyOutputSlot(inventory, inventory.getOutputSlots()[i], 143, 51));
		}
		for(int i = 0; i < inventory.getCraftingSlots().length; i++)
		{
			this.addSlot(new Slot(inventory, inventory.getCraftingSlots()[i], 35 + (i % 3) * 18, 33 + (i / 3) * 18));
		}
		for(int i = 0; i < inventory.getCrystalSlots().length; i++)
		{
			this.addSlot(new CrystalSlot(inventory, inventory.getCrystalSlots()[i], crystalX[i], crystalY[i], crystalAspect[i]));
		}

		//Player Inventory
		int m;
		int l;
		for (m = 0; m < 3; ++m)
		{
			for (l = 0; l < 9; ++l)
			{
				this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 111 + m * 18));
			}
		}
		for (m = 0; m < 9; ++m)
		{
			this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 169));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return true;
	}

	private void updateOutput(PlayerEntity player)
	{
		World world = player.getEntityWorld();
		if(!world.isClient)
		{
			Optional<NekomancyRecipe> optional = world.getRecipeManager().getFirstMatch(ThuwumcraftRecipes.NEKOMANCY, inventory, world);
			if(optional.isPresent())
			{
				this.slots.get(0).setStack(optional.get().getOutput().copy());
			}
			else
			{
				this.slots.get(0).setStack(ItemStack.EMPTY);
			}
			this.slots.get(0).markDirty();
		}
	}

	private void craft(PlayerEntity player)
	{
		World world = player.getEntityWorld();
		if(!world.isClient)
		{
			Optional<NekomancyRecipe> optional = world.getRecipeManager().getFirstMatch(ThuwumcraftRecipes.NEKOMANCY, inventory, world);
			if(optional.isPresent())
			{
				optional.get().craft(this.inventory);
				for(int i = 0; i < 14; i++)
				{
					this.slots.get(i).markDirty();
				}
			}
			this.slots.get(0).markDirty();
		}
	}

	@Override
	public void onSlotClick(int slotIndex, int clickData, SlotActionType actionType, PlayerEntity player)
	{
		if(slotIndex == 0)
		{
			if(slots.get(0).hasStack())
			{
				craft(player);
			}
		}
		super.onSlotClick(slotIndex, clickData, actionType, player);
		this.updateOutput(player);
	}

	@Override
	public void close(PlayerEntity player)
	{
		super.close(player);
		if(!player.getEntityWorld().isClient)
		{
			for(int i = 0; i < inventory.size(); i++)
			{
				if(i != inventory.getOutputSlots()[0] && !inventory.getStack(i).isEmpty())
				{
					player.dropItem(inventory.getStack(i), true);
				}
			}
		}
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index)
	{
		return ItemStack.EMPTY;
	}
}
