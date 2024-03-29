package net.watersfall.thuwumcraft.recipe;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.aspect.Aspect;
import net.watersfall.thuwumcraft.api.aspect.AspectInventory;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.api.aspect.Aspects;
import net.watersfall.thuwumcraft.api.client.gui.BookRecipeType;
import net.watersfall.thuwumcraft.api.client.gui.BookRecipeTypes;
import net.watersfall.thuwumcraft.api.client.recipe.BookRenderableRecipe;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRecipes;

import java.util.ArrayList;
import java.util.List;

public class CrucibleRecipe extends ResearchRequiredRecipe<AspectInventory> implements BookRenderableRecipe
{
	public final Ingredient catalyst;
	public final List<AspectStack> aspects;
	public final ItemStack output;

	public CrucibleRecipe(Identifier id, Ingredient catalyst, List<AspectStack> aspects, ItemStack output, List<Identifier> research)
	{
		super(id, research);
		this.catalyst = catalyst;
		this.aspects = aspects;
		this.output = output;
	}

	@Override
	public BookRecipeType getBookType()
	{
		return BookRecipeTypes.CAULDRON;
	}

	@Override
	public boolean matches(AspectInventory inv, World world)
	{
		if(catalyst.test(inv.getCurrentInput()))
		{
			for(int i = 0; i < this.aspects.size(); i++)
			{
				if(inv.getAspect(aspects.get(i).getAspect()).getCount() < aspects.get(i).getCount())
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public ItemStack craft(AspectInventory inv)
	{
		inv.setCurrentInput(ItemStack.EMPTY);
		for(int i = 0; i < aspects.size(); i++)
		{
			inv.getAspect(aspects.get(i).getAspect()).decrement(aspects.get(i).getCount());
			if(inv.getAspect(aspects.get(i).getAspect()).isEmpty())
			{
				inv.removeAspect(aspects.get(i).getAspect());
			}
		}
		return getOutput();
	}

	@Override
	public boolean fits(int width, int height)
	{
		return true;
	}

	@Override
	public ItemStack getOutput()
	{
		return this.output.copy();
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
		return ThuwumcraftRecipes.CRUCIBLE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return ThuwumcraftRecipes.CRUCIBLE;
	}

	public static class Serializer implements RecipeSerializer<CrucibleRecipe>
	{
		@Override
		public CrucibleRecipe read(Identifier id, JsonObject json)
		{
			Ingredient catalyst = Ingredient.fromJson(json.getAsJsonObject("catalyst"));
			JsonArray list = json.getAsJsonArray("aspects");
			List<AspectStack> aspects = new ArrayList<>(list.size());
			for(int i = 0; i < list.size(); i++)
			{
				JsonObject object = list.get(i).getAsJsonObject();
				Aspect aspect = Aspects.ASPECTS.get(Identifier.tryParse(object.get("aspect").getAsString()));
				int amount = object.get("count").getAsInt();
				aspects.add(new AspectStack(aspect, amount));
			}
			ItemStack output = new ItemStack(Registry.ITEM.get(Identifier.tryParse(json.get("output").getAsString())));
			List<Identifier> research = new ArrayList<>();
			JsonArray array = json.getAsJsonArray("research");
			if(array != null)
			{
				array.forEach((element) -> {
					research.add(Identifier.tryParse(element.getAsString()));
				});
			}
			return new CrucibleRecipe(id, catalyst, aspects, output, ImmutableList.copyOf(research));
		}

		@Override
		public CrucibleRecipe read(Identifier id, PacketByteBuf buf)
		{
			Ingredient input = Ingredient.fromPacket(buf);
			int size = buf.readInt();
			List<AspectStack> list = new ArrayList<>(size);
			for(int i = 0; i < size; i++)
			{
				Aspect aspect = Aspects.ASPECTS.get(buf.readIdentifier());
				list.add(new AspectStack(aspect, buf.readInt()));
			}
			ItemStack output = buf.readItemStack();
			List<Identifier> research = new ArrayList<>();
			int count = buf.readInt();
			for(int i = 0; i < count; i++)
			{
				research.add(buf.readIdentifier());
			}
			return new CrucibleRecipe(id, input, list, output, ImmutableList.copyOf(research));
		}

		@Override
		public void write(PacketByteBuf buf, CrucibleRecipe recipe)
		{
			recipe.catalyst.write(buf);
			buf.writeInt(recipe.aspects.size());
			for(int i = 0; i < recipe.aspects.size(); i++)
			{
				buf.writeIdentifier(recipe.aspects.get(i).getAspect().getId());
				buf.writeInt(recipe.aspects.get(i).getCount());
			}
			buf.writeItemStack(recipe.output);
			buf.writeInt(recipe.research.size());
			for(int i = 0; i < recipe.research.size(); i++)
			{
				buf.writeIdentifier(recipe.research.get(i));
			}
		}
	}
}
