package net.watersfall.thuwumcraft.recipe;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.watersfall.wet.api.abilities.Ability;
import net.watersfall.wet.api.abilities.AbilityClientSerializable;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.util.ArrayList;
import java.util.List;

public class AbilityRecipe<T extends CraftingRecipe> implements CraftingRecipe
{
	private final Recipe<Inventory> recipe;
	private final List<Ability<ItemStack>> abilities;
	private final Serializer serializer;

	public AbilityRecipe(Serializer serializer, Recipe<Inventory> recipe, List<Ability<ItemStack>> abilities)
	{
		this.recipe = recipe;
		this.abilities = ImmutableList.copyOf(abilities);
		this.serializer = serializer;
	}

	@Override
	public boolean matches(CraftingInventory inventory, World world)
	{
		return recipe.matches(inventory, world);
	}

	@Override
	public ItemStack craft(CraftingInventory inventory)
	{
		ItemStack stack =  recipe.craft(inventory);
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		this.abilities.forEach(ability -> {
			provider.addAbility(AbilityProvider.ITEM_REGISTRY.create(ability.getId(), ability.toNbt(new NbtCompound(), stack), stack));
		});
		return stack;
	}

	@Override
	public boolean fits(int width, int height)
	{
		return recipe.fits(width, height);
	}

	@Override
	public ItemStack getOutput()
	{
		ItemStack stack =  recipe.getOutput();
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		this.abilities.forEach(ability -> {
			provider.addAbility(AbilityProvider.ITEM_REGISTRY.create(ability.getId(), ability.toNbt(new NbtCompound(), stack), stack));
		});
		return stack;
	}

	@Override
	public Identifier getId()
	{
		return recipe.getId();
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return serializer;
	}

	@Override
	public DefaultedList<ItemStack> getRemainder(CraftingInventory inventory)
	{
		return recipe.getRemainder(inventory);
	}

	@Override
	public DefaultedList<Ingredient> getIngredients()
	{
		return recipe.getIngredients();
	}

	@Override
	public boolean isIgnoredInRecipeBook()
	{
		return recipe.isIgnoredInRecipeBook();
	}

	@Override
	public String getGroup()
	{
		return recipe.getGroup();
	}

	@Override
	public ItemStack createIcon()
	{
		return recipe.createIcon();
	}

	@Override
	public boolean isEmpty()
	{
		return recipe.isEmpty();
	}

	@Override
	public RecipeType<?> getType()
	{
		return recipe.getType();
	}

	public static class Serializer<T extends CraftingRecipe> implements RecipeSerializer<AbilityRecipe<T>>
	{
		private final RecipeSerializer<Recipe<Inventory>> serializer;

		public Serializer(RecipeSerializer<Recipe<Inventory>> serializer)
		{
			this.serializer = serializer;
		}

		@Override
		public AbilityRecipe<T> read(Identifier id, JsonObject json)
		{
			Recipe<Inventory> recipe = serializer.read(id, json);
			List<Ability<ItemStack>> abilities = new ArrayList<>();
			JsonArray array = JsonHelper.getArray(json, "abilities");
			array.forEach(element -> {
				JsonObject object = element.getAsJsonObject();
				Identifier abilityId = Identifier.tryParse(object.get("id").getAsString());
				String nbtString = object.get("nbt").getAsString();
				try
				{
					NbtCompound nbt = StringNbtReader.parse(nbtString);
					abilities.add(AbilityProvider.ITEM_REGISTRY.create(abilityId, nbt, ItemStack.EMPTY));
				}
				catch(CommandSyntaxException e)
				{
					e.printStackTrace();
				}
			});
			return new AbilityRecipe(this, recipe, abilities);
		}

		@Override
		public AbilityRecipe<T> read(Identifier id, PacketByteBuf buf)
		{
			Recipe<Inventory> recipe = serializer.read(id, buf);
			int size = buf.readInt();
			List<Ability<ItemStack>> abilities = new ArrayList<>();
			for(int i = 0; i < size; i++)
			{
				Identifier abilityId = buf.readIdentifier();
				abilities.add(AbilityProvider.ITEM_REGISTRY.create(abilityId, buf));
			}
			return new AbilityRecipe(this, recipe, abilities);
		}

		@Override
		public void write(PacketByteBuf buf, AbilityRecipe<T> recipe)
		{
			serializer.write(buf, recipe.recipe);
			buf.writeInt(recipe.abilities.size());
			recipe.abilities.forEach(ability -> {
				buf.writeIdentifier(ability.getId());
				if(ability instanceof AbilityClientSerializable client)
				{
					client.toPacket(buf);
				}
			});
		}
	}
}
