package com.watersfall.alchemy.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.watersfall.alchemy.AlchemyMod;
import com.watersfall.alchemy.inventory.BrewingCauldronInventory;
import com.watersfall.alchemy.item.AlchemyModItems;
import com.watersfall.alchemy.item.LadleItem;
import com.watersfall.alchemy.util.StatusEffectHelper;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public class CauldronRecipe implements Recipe<BrewingCauldronInventory>
{
	public final Identifier id;
	public final ItemStack input;
	public final ArrayList<StatusEffectInstance> effects;
	public final int color;

	public CauldronRecipe(Identifier id, ItemStack input, ArrayList<StatusEffectInstance> effects, int color)
	{
		this.id = id;
		this.input = input;
		this.effects = effects;
		this.color = color;
	}

	@Override
	public boolean matches(BrewingCauldronInventory inv, World world)
	{
		return inv.getContents().get(0).getItem() == this.input.getItem();
	}

	@Override
	public ItemStack craft(BrewingCauldronInventory inv)
	{
		return inv.getInput().get(0);
	}

	public ItemStack craft(BrewingCauldronInventory inventory, CauldronTypeRecipe recipe)
	{
		ItemStack stack = inventory.getInput().get(0).copy();
		Set<StatusEffectInstance> effects = StatusEffectHelper.getEffects(inventory);
		if(effects != StatusEffectHelper.INVALID_RECIPE)
		{
			if(recipe.craftingAction == CauldronTypeRecipe.CraftingAction.CREATE_LADLE)
			{
				StatusEffectHelper.createLadle(stack, StatusEffectHelper.getEffects(inventory));
			}
			else if(recipe.craftingAction == CauldronTypeRecipe.CraftingAction.CREATE_ITEM)
			{
				stack = recipe.getOutput().copy();
				PotionUtil.setCustomPotionEffects(stack, StatusEffectHelper.getEffects(inventory));
				return stack;
			}
			else if(recipe.craftingAction == CauldronTypeRecipe.CraftingAction.CREATE_WEAPON)
			{
				PotionUtil.setCustomPotionEffects(stack, StatusEffectHelper.getEffects(inventory));

				stack.getTag().putInt("uses", recipe.uses);
			}
			return stack;
		}
		return inventory.getInput().get(0);
	}

	@Override
	public boolean fits(int width, int height)
	{
		return true;
	}

	@Override
	public ItemStack getOutput()
	{
		return null;
	}

	@Override
	public boolean isIgnoredInRecipeBook()
	{
		return true;
	}

	@Override
	public String getGroup()
	{
		return "";
	}

	@Override
	public Identifier getId()
	{
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return AlchemyMod.CAULDRON_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return AlchemyMod.CAULDRON_RECIPE_TYPE;
	}

	public static class Serializer implements RecipeSerializer<CauldronRecipe>
	{
		private final CauldronRecipe.Serializer.RecipeFactory<CauldronRecipe> recipeFactory;
		public Serializer(CauldronRecipe.Serializer.RecipeFactory<CauldronRecipe> recipeFactory)
		{
			this.recipeFactory = recipeFactory;
		}

		@Override
		public CauldronRecipe read(Identifier id, JsonObject json)
		{
			ItemStack stack = new ItemStack(Registry.ITEM.get(new Identifier(json.get("item").getAsString())));
			JsonObject jsonColor = json.getAsJsonObject("color");
			int color = new Color(jsonColor.get("r").getAsInt(), jsonColor.get("g").getAsInt(), jsonColor.get("b").getAsInt(), 0).hashCode();
			JsonArray array = json.getAsJsonArray("effects");
			ArrayList<StatusEffectInstance> effects = new ArrayList<>(array.size());
			array.forEach((object) -> {

				StatusEffect effect = Registry.STATUS_EFFECT.get(Identifier.tryParse(object.getAsJsonObject().get("effect").getAsString()));
				int duration = object.getAsJsonObject().get("duration").getAsInt();
				int amplifier = object.getAsJsonObject().get("amplifier").getAsInt();
				effects.add(new StatusEffectInstance(effect, duration, amplifier));
			});
			return new CauldronRecipe(id, stack, effects, color);
		}

		@Override
		public CauldronRecipe read(Identifier id, PacketByteBuf buf)
		{
			ItemStack stack = buf.readItemStack();
			int size = buf.readByte();
			ArrayList<StatusEffectInstance> list = new ArrayList<>(size);
			for(int i = 0; i < size; i++)
			{
				list.add(StatusEffectInstance.fromTag(buf.readCompoundTag()));
			}
			return new CauldronRecipe(id, stack, list, 0);
		}

		@Override
		public void write(PacketByteBuf buf, CauldronRecipe recipe)
		{
			buf.writeItemStack(recipe.input);
			buf.writeByte(recipe.effects.size());
			for(int i = 0; i < recipe.effects.size(); i++)
			{
				buf.writeCompoundTag(recipe.effects.get(i).toTag(new CompoundTag()));
			}
		}

		public interface RecipeFactory<T extends Recipe<?>>
		{
			T create(Identifier id, ItemStack input, ArrayList<StatusEffectInstance> effects, int color);
		}
	}
}
