package net.watersfall.thuwumcraft.gui.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import net.watersfall.thuwumcraft.api.abilities.chunk.VisAbility;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.gui.AspectCraftingHandler;
import net.watersfall.thuwumcraft.inventory.AspectCraftingInventory;
import net.watersfall.thuwumcraft.item.CrystalItem;
import net.watersfall.thuwumcraft.recipe.AspectCraftingShapedRecipe;
import net.watersfall.thuwumcraft.recipe.ResearchRequiredCraftingRecipe;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.util.Optional;

public class AspectCraftingOutputSlot extends Slot
{
	private final AspectCraftingHandler handler;
	private final AspectCraftingInventory inventory;
	private final PlayerEntity player;
	private int amount;
	private final Inventory output;

	public AspectCraftingOutputSlot(AspectCraftingHandler handler, PlayerEntity player, AspectCraftingInventory inventory, Inventory output, int index, int x, int y)
	{
		super(output, index, x, y);
		this.inventory = inventory;
		this.player = player;
		this.output = output;
		this.handler = handler;
	}

	@Override
	public boolean canInsert(ItemStack stack)
	{
		return false;
	}

	public ItemStack takeStack(int amount)
	{
		if(this.hasStack())
		{
			this.amount += Math.min(amount, this.getStack().getCount());
		}
		return super.takeStack(amount);
	}

	protected void onCrafted(ItemStack stack, int amount)
	{
		this.amount += amount;
		this.onCrafted(stack);
	}

	protected void onTake(int amount)
	{
		this.amount += amount;
	}

	protected void onCrafted(ItemStack stack)
	{
		if(this.amount > 0)
		{
			stack.onCraft(this.player.world, this.player, this.amount);
		}
		this.amount = 0;
	}

	@Override
	public boolean canTakeItems(PlayerEntity playerEntity)
	{
		return this.handler.canTakeOutput();
	}

	@Override
	public ItemStack getStack()
	{
		PlayerResearchAbility ability = AbilityProvider.getProvider(player).getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).get();
		Optional<CraftingRecipe> optional = player.world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, this.inventory, player.world);
		if(optional.isPresent() && optional.get() instanceof ResearchRequiredCraftingRecipe)
		{
			ResearchRequiredCraftingRecipe recipe = (ResearchRequiredCraftingRecipe)optional.get();
			if(recipe.matches(inventory, this.handler.entity.getWorld(), ability))
			{
				return super.getStack();
			}
		}
		return ItemStack.EMPTY;
	}

	public void onTakeItem(PlayerEntity player, ItemStack stack)
	{
		this.onCrafted(stack);
		DefaultedList<ItemStack> remaining = player.world.getRecipeManager().getRemainingStacks(RecipeType.CRAFTING, this.inventory, player.world);
		Optional<CraftingRecipe> optional = player.world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, this.inventory, player.world);
		if(optional.isPresent() && optional.get() instanceof ResearchRequiredCraftingRecipe)
		{
			ResearchRequiredCraftingRecipe recipe = (ResearchRequiredCraftingRecipe)optional.get();
			if(!recipe.matches(this.inventory, player.world, AbilityProvider.getProvider(player).getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).get()))
			{
				return;
			}
		}
		for(int i = 0; i < remaining.size(); ++i)
		{
			ItemStack currentStack = this.inventory.getStack(i);
			ItemStack remainingStack = remaining.get(i);
			if(!currentStack.isEmpty())
			{
				this.inventory.removeStack(i, 1);
				currentStack = this.inventory.getStack(i);
			}
			if(!remainingStack.isEmpty())
			{
				if(currentStack.isEmpty())
				{
					this.inventory.setStack(i, remainingStack);
				}
				else if(ItemStack.areItemsEqualIgnoreDamage(currentStack, remainingStack) && ItemStack.areNbtEqual(currentStack, remainingStack))
				{
					remainingStack.increment(currentStack.getCount());
					this.inventory.setStack(i, remainingStack);
				}
				else if(!this.player.getInventory().insertStack(remainingStack))
				{
					this.player.dropItem(remainingStack, false);
				}
			}
		}
		if(optional.isPresent() && optional.get() instanceof AspectCraftingShapedRecipe)
		{
			AspectCraftingShapedRecipe recipe = (AspectCraftingShapedRecipe)optional.get();
			recipe.getAspects().forEach(aspect -> {
				CrystalItem item = (CrystalItem)aspect.getItem();
				inventory.getStack(item.getAspect()).decrement(aspect.getCount());
			});
			WorldChunk chunk = handler.entity.getWorld().getWorldChunk(handler.entity.getPos());
			AbilityProvider<Chunk> provider = AbilityProvider.getProvider(chunk);
			Optional<VisAbility> abilityOptional = provider.getAbility(VisAbility.ID, VisAbility.class);
			if(abilityOptional.isPresent() && !chunk.getWorld().isClient)
			{
				abilityOptional.get().setVis(abilityOptional.get().getVis() - recipe.getVis());
				abilityOptional.get().sync(chunk);
			}
		}
	}
}
