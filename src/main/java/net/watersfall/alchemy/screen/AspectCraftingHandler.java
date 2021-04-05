package net.watersfall.alchemy.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;
import net.watersfall.alchemy.block.entity.AspectCraftingEntity;
import net.watersfall.alchemy.inventory.AspectCraftingInventory;
import net.watersfall.alchemy.screen.slot.AspectCraftingOutputSlot;

import java.util.Optional;

public class AspectCraftingHandler extends ScreenHandler
{
	private final ScreenHandlerContext context;
	private final AspectCraftingEntity entity;
	private AspectCraftingInventory inventory;

	public AspectCraftingHandler(int syncId, PlayerInventory inventory)
	{
		this(syncId, inventory, null, ScreenHandlerContext.EMPTY);
	}

	public AspectCraftingHandler(int syncId, PlayerInventory playerInventory, AspectCraftingEntity inventory, ScreenHandlerContext context)
	{
		super(AlchemyScreenHandlers.ASPECT_CRAFTING_HANDLER, syncId);
		this.context = context;
		this.entity = inventory;
		if(this.entity != null)
		{
			this.inventory = this.entity.getInventory(this);
		}
		else
		{
			this.inventory = new AspectCraftingInventory(this);
		}

		this.addSlot(new AspectCraftingOutputSlot(playerInventory.player, this.inventory, this.inventory.output, 0, 124, 35));

		for(int y = 0; y < 3; ++y)
		{
			for(int x = 0; x < 3; ++x)
			{
				this.addSlot(new Slot(this.inventory, x + y * 3, 30 + x * 18, 17 + y * 18));
			}
		}

		for(int y = 0; y < 2; y++)
		{
			for(int x = 0; x < 3; x++)
			{
				this.addSlot(new Slot(this.inventory.crystals, x + y * 3, 86 + x * 18, 17 + y * 18));
			}
		}

		if(entity != null)
		{
			this.inventory.load(entity.contents);
			this.slots.forEach(Slot::markDirty);
		}

		//Player Inventory
		for (int y = 0; y < 3; ++y)
		{
			for (int x = 0; x < 9; ++x)
			{
				this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 111 + y * 18));
			}
		}
		for (int y = 0; y < 9; ++y)
		{
			this.addSlot(new Slot(playerInventory, y, 8 + y * 18, 169));
		}
	}

	protected void updateResult(World world, AspectCraftingInventory inventory)
	{
		if (!world.isClient)
		{
			ItemStack output = ItemStack.EMPTY;
			Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, inventory, world);
			if (optional.isPresent())
			{
				output = optional.get().craft(inventory);
			}
			inventory.output.setStack(0, output);
			if(this.slots.size() > 0)
				this.slots.get(0).markDirty();
		}
	}

	public void onContentChanged(Inventory inventory)
	{
		if(this.entity != null)
		{
			this.inventory.save(this.entity.contents);
			this.entity.handlers.forEach(handler -> handler.onContentChanged(inventory, false));
		}
	}

	public void onContentChanged(Inventory inventory, boolean bool)
	{
		super.onContentChanged(inventory);
		this.inventory.load(entity.contents);
		this.context.run((world, blockPos) -> updateResult(world, this.inventory));
		this.slots.forEach(Slot::markDirty);
	}

	@Override
	public void close(PlayerEntity player)
	{
		if(!player.getEntityWorld().isClient)
		{
			this.entity.close(this);
		}
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return true;
	}
}
