package net.watersfall.thuwumcraft.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.api.abilities.common.StatusEffectItem;
import net.watersfall.thuwumcraft.item.ThuwumcraftItems;

import java.util.Optional;

public class BindingRecipe implements CraftingRecipe
{
	private final Identifier id;
	private final Ingredient input;
	private final ItemStack output;
	private final Type type;

	public BindingRecipe(Identifier id, Ingredient input, ItemStack output, Type type)
	{
		this.id = id;
		this.input = input;
		this.output = output;
		this.type = type;
	}

	@Override
	public boolean matches(CraftingInventory inventory, World world)
	{
		int count = 0;
		boolean foundInput = false;
		for(int i = 0; i < inventory.size(); i++)
		{
			if(inventory.getStack(i).isOf(ThuwumcraftItems.ALCHEMY_BINDING))
			{
				if(count >= 1)
				{
					return false;
				}
				count++;
			}
			else if(input.test(inventory.getStack(i)))
			{
				if(foundInput)
				{
					return false;
				}
				foundInput = true;
			}
		}
		return count == 1 && foundInput;
	}

	@Override
	public ItemStack craft(CraftingInventory inventory)
	{
		ItemStack bindingStack = ItemStack.EMPTY;
		for(int i = 0; i < inventory.size(); i++)
		{
			if(inventory.getStack(i).isOf(ThuwumcraftItems.ALCHEMY_BINDING))
			{
				bindingStack = inventory.getStack(i);
				break;
			}
		}
		for(int i = 0; i < inventory.size(); i++)
		{
			if(input.test(inventory.getStack(i)))
			{
				ItemStack output = inventory.getStack(i).copy();
				AbilityProvider<ItemStack> binding = AbilityProvider.getProvider(bindingStack);
				Optional<StatusEffectItem> optional = binding.getAbility(StatusEffectItem.ID, StatusEffectItem.class);
				if(optional.isPresent())
				{
					if(type == Type.NBT)
					{
						PotionUtil.setCustomPotionEffects(output, optional.get().getEffects());
					}
					else if(type == Type.ABILITY)
					{
						AbilityProvider<ItemStack> outputProvider = AbilityProvider.getProvider(output);
						outputProvider.addAbility(optional.get());
					}
				}
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
		return this.output;
	}

	@Override
	public boolean isIgnoredInRecipeBook()
	{
		return true;
	}

	@Override
	public Identifier getId()
	{
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return ThuwumcraftRecipes.BINDING_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return RecipeType.CRAFTING;
	}

	public static class Serializer implements RecipeSerializer<BindingRecipe>
	{

		@Override
		public BindingRecipe read(Identifier id, JsonObject json)
		{
			Ingredient input = Ingredient.fromJson(json.getAsJsonObject("input"));
			ItemStack output = new ItemStack(JsonHelper.getItem(json, "output"));
			Type type = Type.valueOf(json.get("recipe_type").getAsString());
			return new BindingRecipe(id, input, output, type);
		}

		@Override
		public BindingRecipe read(Identifier id, PacketByteBuf buf)
		{
			Ingredient input = Ingredient.fromPacket(buf);
			ItemStack output = buf.readItemStack();
			Type type = Type.valueOf(buf.readString());
			return new BindingRecipe(id, input, output, type);
		}

		@Override
		public void write(PacketByteBuf buf, BindingRecipe recipe)
		{
			recipe.input.write(buf);
			buf.writeItemStack(recipe.output);
			buf.writeString(recipe.type.name());
		}
	}

	public enum Type
	{
		NBT,
		ABILITY
	}
}
