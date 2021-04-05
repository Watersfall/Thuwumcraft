package net.watersfall.alchemy.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.chunk.VisAbility;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.block.entity.AspectCraftingEntity;
import net.watersfall.alchemy.inventory.AspectCraftingInventory;
import net.watersfall.alchemy.recipe.AspectCraftingShapedRecipe;
import net.watersfall.alchemy.screen.slot.AspectCraftingOutputSlot;
import net.watersfall.alchemy.screen.slot.CrystalSlot;

import java.util.Optional;

public class AspectCraftingHandler extends ScreenHandler
{
	private final ScreenHandlerContext context;
	public final AspectCraftingEntity entity;
	private AspectCraftingInventory inventory;
	public Recipe<?> currentRecipe = null;

	public AspectCraftingHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf)
	{
		this(syncId, inventory, null, ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos()), buf.readBlockPos());
	}

	public AspectCraftingHandler(int syncId, PlayerInventory playerInventory, AspectCraftingEntity inventory, ScreenHandlerContext context, BlockPos pos)
	{
		super(AlchemyScreenHandlers.ASPECT_CRAFTING_HANDLER, syncId);
		this.context = context;

		if(inventory != null)
		{
			this.entity = inventory;
		}
		else
		{
			this.entity = (AspectCraftingEntity)playerInventory.player.world.getBlockEntity(pos);
		}
		this.inventory = entity.getInventory(this);

		this.addSlot(new AspectCraftingOutputSlot(this, playerInventory.player, this.inventory, this.inventory.output, 0, 143, 53));

		for(int y = 0; y < 3; ++y)
		{
			for(int x = 0; x < 3; ++x)
			{
				this.addSlot(new Slot(this.inventory, x + y * 3, 30 + x * 18, 35 + y * 18));
			}
		}

		this.addSlot(new CrystalSlot(this.inventory.crystals, 0, 84, 35, Aspects.AIR));
		this.addSlot(new CrystalSlot(this.inventory.crystals, 1, 12, 35, Aspects.WATER));
		this.addSlot(new CrystalSlot(this.inventory.crystals, 2, 12, 71, Aspects.EARTH));
		this.addSlot(new CrystalSlot(this.inventory.crystals, 3, 84, 71, Aspects.FIRE));
		this.addSlot(new CrystalSlot(this.inventory.crystals, 4, 48, 17, Aspects.ORDER));
		this.addSlot(new CrystalSlot(this.inventory.crystals, 5, 48, 89, Aspects.DISORDER));

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
				this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 120 + y * 18));
			}
		}
		for (int y = 0; y < 9; ++y)
		{
			this.addSlot(new Slot(playerInventory, y, 8 + y * 18, 178));
		}
		this.context.run((world, blockPos) -> updateResult(world, this.inventory));
	}

	protected void updateResult(World world, AspectCraftingInventory inventory)
	{
		ItemStack output = ItemStack.EMPTY;
		Optional<CraftingRecipe> optional = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, inventory, world);
		if (optional.isPresent())
		{
			output = optional.get().craft(inventory);
			this.currentRecipe = optional.get();
		}
		else
		{
			this.currentRecipe = null;
		}
		if (!world.isClient)
		{
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

	public boolean canTakeOutput()
	{
		if(entity == null)
		{
			return false;
		}
		Optional<CraftingRecipe> optional = entity.getWorld().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, this.inventory, entity.getWorld());
		if(optional.isPresent() && optional.get() instanceof AspectCraftingShapedRecipe)
		{
			AspectCraftingShapedRecipe recipe = (AspectCraftingShapedRecipe)optional.get();
			AbilityProvider<Chunk> provider = AbilityProvider.getProvider(entity.getWorld().getWorldChunk(entity.getPos()));
			Optional<VisAbility> abilityOptional = provider.getAbility(VisAbility.ID, VisAbility.class);
			if(abilityOptional.isPresent())
			{
				return abilityOptional.get().getVis() >= recipe.getVis();
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return true;
	}
}
