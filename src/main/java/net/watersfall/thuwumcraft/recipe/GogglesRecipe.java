package net.watersfall.thuwumcraft.recipe;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.impl.item.ItemExtensions;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRecipes;

public class GogglesRecipe implements CraftingRecipe
{
	private final Identifier id;

	public GogglesRecipe(Identifier id)
	{
		this.id = id;
	}

	private boolean isValidHelmet(ItemStack stack)
	{
		return stack.getItem() != ThuwumcraftItems.GOGGLES
				&& (stack.getItem() instanceof ArmorItem armor && armor.getSlotType() == EquipmentSlot.HEAD)
				|| stack.getItem() == Items.PUMPKIN ||
				(stack.getItem() instanceof ItemExtensions extension && extension.fabric_getEquipmentSlotProvider() != null && extension.fabric_getEquipmentSlotProvider().getPreferredEquipmentSlot(stack) == EquipmentSlot.HEAD);

	}

	@Override
	public boolean matches(CraftingInventory inventory, World world)
	{
		boolean foundHelmet = false, foundGoggles = false;
		for(int i = 0; i < inventory.size(); i++)
		{
			ItemStack stack = inventory.getStack(i);
			if(!stack.isEmpty())
			{
				if(stack.getItem() == ThuwumcraftItems.GOGGLES && !foundGoggles)
				{
					foundGoggles = true;
				}
				else if(!foundHelmet && isValidHelmet(stack))
				{
					foundHelmet = true;
				}
				else
				{
					return false;
				}
			}
		}
		return foundGoggles && foundHelmet;
	}

	@Override
	public ItemStack craft(CraftingInventory inventory)
	{
		for(int i = 0; i < inventory.size(); i++)
		{
			if(isValidHelmet(inventory.getStack(i)))
			{
				ItemStack output = inventory.getStack(i).copy();
				output.getOrCreateNbt().putBoolean("thuwumcraft$goggles", true);
				return output;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height)
	{
		return true;
	}

	@Override
	public ItemStack getOutput()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public Identifier getId()
	{
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return ThuwumcraftRecipes.GOGGLES_SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<GogglesRecipe>
	{

		@Override
		public GogglesRecipe read(Identifier id, JsonObject json)
		{
			return new GogglesRecipe(id);
		}

		@Override
		public GogglesRecipe read(Identifier id, PacketByteBuf buf)
		{
			return new GogglesRecipe(id);
		}

		@Override
		public void write(PacketByteBuf buf, GogglesRecipe recipe)
		{

		}
	}
}
