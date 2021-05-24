package net.watersfall.thuwumcraft.recipe;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.watersfall.thuwumcraft.block.entity.BrewingCauldronEntity;
import net.watersfall.thuwumcraft.inventory.BrewingCauldronInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CauldronItemRecipe extends ResearchRequiredRecipe<BrewingCauldronInventory>
{
	private final Ingredient catalyst;
	private final int waterUse;
	private final List<Ingredient> inputs;
	private final ItemStack output;

	public CauldronItemRecipe(Identifier id, Ingredient catalyst, int waterUse, List<Ingredient> inputs, ItemStack output, List<Identifier> research)
	{
		super(id, research);
		this.catalyst = catalyst;
		this.waterUse = waterUse;
		this.inputs = ImmutableList.copyOf(inputs);
		this.output = output;
	}

	@Override
	public boolean matches(BrewingCauldronInventory inv, World world)
	{
		if(getCatalyst().test(inv.getInput().get(0)))
		{
			if(getInputs().size() > inv.getIngredientCount())
			{
				return false;
			}
			else
			{
				for(int i = 0; i < getInputs().size(); i++)
				{
					boolean hasInput = false;
					for(int o = 0; o < inv.getIngredientCount(); o++)
					{
						if(getInputs().get(i).test(inv.getStack(o)))
						{
							hasInput = true;
						}
					}
					if(!hasInput)
					{
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public ItemStack craft(BrewingCauldronInventory inv)
	{
		byte delete = 0;
		for(int i = 0; i < getInputs().size(); i++)
		{
			for(int o = 0; o < inv.getIngredientCount(); o++)
			{
				if(getInputs().get(i).test(inv.getStack(o)))
				{
					inv.getStack(o).decrement(1);
					if(inv.getStack(o).isEmpty())
					{
						inv.removeStack(o);
						delete++;
					}
				}
			}
		}
		((BrewingCauldronEntity)inv).setIngredientCount((byte) (inv.getIngredientCount() - delete));
		inv.markDirty();
		return getOutput().copy();
	}

	@Override
	public boolean fits(int width, int height)
	{
		return true;
	}

	@Override
	public boolean isIgnoredInRecipeBook()
	{
		return true;
	}

	@Override
	public ItemStack getOutput()
	{
		return this.output;
	}

	@Override
	public Identifier getId()
	{
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return ThuwumcraftRecipes.CAULDRON_ITEM_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return ThuwumcraftRecipes.CAULDRON_ITEM_RECIPE;
	}

	public Ingredient getCatalyst()
	{
		return catalyst;
	}

	public int getWaterUse()
	{
		return waterUse;
	}

	public List<Ingredient> getInputs()
	{
		return inputs;
	}

	public static class Serializer implements RecipeSerializer<CauldronItemRecipe>
	{
		private final CauldronItemRecipe.Serializer.RecipeFactory<CauldronItemRecipe> recipeFactory;

		public Serializer(CauldronItemRecipe.Serializer.RecipeFactory<CauldronItemRecipe> recipeFactory)
		{
			this.recipeFactory = recipeFactory;
		}

		@Override
		public CauldronItemRecipe read(Identifier id, JsonObject json)
		{
			Ingredient catalyst = Ingredient.fromJson(json.get("catalyst"));
			int waterUse = json.get("water_use").getAsInt();
			JsonArray array = json.getAsJsonArray("inputs");
			List<Ingredient> inputs = new ArrayList<>(array.size());
			for(int i = 0; i < array.size(); i++)
			{
				inputs.add(Ingredient.fromJson(array.get(i)));
			}
			ItemStack output = new ItemStack(Registry.ITEM.get(Identifier.tryParse(json.get("output").getAsString())));
			List<Identifier> research = new ArrayList<>();
			array = json.getAsJsonArray("research");
			if(array != null)
			{
				array.forEach((element) -> {
					research.add(Identifier.tryParse(element.getAsString()));
				});
			}
			return new CauldronItemRecipe(id, catalyst, waterUse, inputs, output, ImmutableList.copyOf(research));
		}

		@Override
		public CauldronItemRecipe read(Identifier id, PacketByteBuf buf)
		{
			Ingredient catalyst = Ingredient.fromPacket(buf);
			int waterUse = buf.readInt();
			int size = buf.readInt();
			List<Ingredient> inputs = new ArrayList<>(size);
			for(int i = 0; i < size; i++)
			{
				inputs.add(Ingredient.fromPacket(buf));
			}
			ItemStack output = buf.readItemStack();
			List<Identifier> research = new ArrayList<>();
			int count = buf.readInt();
			for(int i = 0; i < count; i++)
			{
				research.add(buf.readIdentifier());
			}
			return new CauldronItemRecipe(id, catalyst, waterUse, inputs, output, ImmutableList.copyOf(research));
		}

		@Override
		public void write(PacketByteBuf buf, CauldronItemRecipe recipe)
		{
			recipe.getCatalyst().write(buf);
			buf.writeInt(recipe.getWaterUse());
			buf.writeInt(recipe.getInputs().size());
			for(int i = 0; i < recipe.getInputs().size(); i++)
			{
				recipe.getInputs().get(i).write(buf);
			}
			buf.writeItemStack(recipe.getOutput());
			buf.writeInt(recipe.research.size());
			for(int i = 0; i < recipe.research.size(); i++)
			{
				buf.writeIdentifier(recipe.research.get(i));
			}
		}

		public interface RecipeFactory<T extends Recipe<?>>
		{
			T create(Identifier id, Ingredient catalyst, int waterUse, List<Ingredient> inputs, ItemStack output, List<Identifier> research);
		}
	}
}
