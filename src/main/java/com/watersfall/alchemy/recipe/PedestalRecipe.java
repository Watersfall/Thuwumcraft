package com.watersfall.alchemy.recipe;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.watersfall.alchemy.block.AlchemyModBlocks;
import com.watersfall.alchemy.blockentity.PedestalEntity;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PedestalRecipe implements Recipe<PedestalEntity>
{
	private final Identifier id;
	private final List<Ingredient> inputs;
	private final Ingredient catalyst;
	private final ItemStack output;
	private static final byte HORIZONTAL_RANGE = 5;
	private static final byte VERTICAL_RANGE = 1;

	private static HashMap<BlockPos, List<PedestalEntity>> NEARBY_PEDESTALS_CACHE = new HashMap<>();

	private static List<PedestalEntity> getNearbyPedestals(BlockPos pos, World world)
	{
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		BlockState loopState = null;
		List<PedestalEntity> entities = new ArrayList<>();
		for(int x = pos.getX() - HORIZONTAL_RANGE; x < pos.getX() + HORIZONTAL_RANGE; x++)
		{
			for(int y = pos.getY() - VERTICAL_RANGE; y < pos.getY() + VERTICAL_RANGE; y++)
			{
				for(int z = pos.getZ() - HORIZONTAL_RANGE; z < pos.getZ() + HORIZONTAL_RANGE; z++)
				{
					mutablePos.set(x, y, z);
					if(!mutablePos.equals(pos))
					{
						loopState = world.getBlockState(mutablePos);
						if(loopState.getBlock() == AlchemyModBlocks.PEDESTAL_BLOCK)
						{
							entities.add((PedestalEntity)world.getBlockEntity(mutablePos));
						}
					}
				}
			}
		}
		return entities;
	}

	public PedestalRecipe(Identifier id, List<Ingredient> inputs, Ingredient catalyst, ItemStack output)
	{
		this.id = id;
		this.inputs = inputs;
		this.catalyst = catalyst;
		this.output = output;
	}

	@Override
	public boolean matches(PedestalEntity inv, World world)
	{
		if(catalyst.test(inv.getStack()))
		{
			BlockPos pos = inv.getPos();
			List<PedestalEntity> entities = getNearbyPedestals(pos, world);
			if(entities.size() >= inputs.size())
			{
				for(int i = 0; i < inputs.size(); i++)
				{
					boolean found = false;
					for(int o = 0; o < entities.size(); o++)
					{
						if(inputs.get(i).test(entities.get(o).getStack()))
						{
							found = true;
							break;
						}
					}
					if(!found)
					{
						return false;
					}
				}
				NEARBY_PEDESTALS_CACHE.put(pos, entities);
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack craft(PedestalEntity inv)
	{
		List<PedestalEntity> entities = NEARBY_PEDESTALS_CACHE.get(inv.getPos());
		NEARBY_PEDESTALS_CACHE.remove(inv.getPos());
		if(entities == null)
		{
			entities = getNearbyPedestals(inv.getPos(), inv.getWorld());
		}
		for(int i = 0; i < inputs.size(); i++)
		{
			for(int o = 0; o < entities.size(); o++)
			{
				if(inputs.get(i).test(entities.get(o).getStack()))
				{
					entities.get(o).setStack(ItemStack.EMPTY);
					entities.get(o).sync();
				}
			}
		}
		inv.setStack(this.output.copy());
		inv.sync();
		return output.copy();
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
		return this.output.copy();
	}

	@Override
	public Identifier getId()
	{
		return this.id;
	}

	public List<Ingredient> getInputs()
	{
		return inputs;
	}

	public Ingredient getCatalyst()
	{
		return catalyst;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return AlchemyModRecipes.PEDESTAL_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return AlchemyModRecipes.PEDESTAL_RECIPE;
	}

	public static class Serializer implements RecipeSerializer<PedestalRecipe>
	{
		private final static String INPUTS = "inputs";
		private final static String CATALYST = "catalyst";
		private final static String OUTPUT = "output";

		private final PedestalRecipe.Serializer.RecipeFactory<PedestalRecipe> recipeFactory;

		public Serializer(PedestalRecipe.Serializer.RecipeFactory<PedestalRecipe> recipeFactory)
		{
			this.recipeFactory = recipeFactory;
		}

		@Override
		public PedestalRecipe read(Identifier id, JsonObject json)
		{
			JsonArray jsonList = json.getAsJsonArray(INPUTS);
			List<Ingredient> list = new ArrayList<>(jsonList.size());
			for(int i = 0; i < jsonList.size(); i++)
			{
				list.add(Ingredient.fromJson(jsonList.get(i)));
			}
			Ingredient catalyst = Ingredient.fromJson(json.getAsJsonObject(CATALYST));
			ItemStack output = new ItemStack(Registry.ITEM.get(Identifier.tryParse(json.get(OUTPUT).getAsString())));
			return new PedestalRecipe(id, ImmutableList.copyOf(list), catalyst, output);
		}

		@Override
		public PedestalRecipe read(Identifier id, PacketByteBuf buf)
		{
			int count = buf.readInt();
			List<Ingredient> list = new ArrayList<>(count);
			for(int i = 0; i < count; i++)
			{
				list.add(Ingredient.fromPacket(buf));
			}
			Ingredient catalyst = Ingredient.fromPacket(buf);
			ItemStack output = buf.readItemStack();
			return new PedestalRecipe(id, ImmutableList.copyOf(list), catalyst, output);
		}

		@Override
		public void write(PacketByteBuf buf, PedestalRecipe recipe)
		{
			buf.writeInt(recipe.getInputs().size());
			for(int i = 0; i < recipe.getInputs().size(); i++)
			{
				recipe.getInputs().get(i).write(buf);
			}
			recipe.getCatalyst().write(buf);
			buf.writeItemStack(recipe.getOutput());
		}

		public interface RecipeFactory<T extends Recipe<?>>
		{
			T create(Identifier id, List<Ingredient> inputs, Ingredient catalyst, ItemStack output);
		}
	}
}
