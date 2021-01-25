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

public class CauldronRecipe implements Recipe<BrewingCauldronInventory>
{
	public final ItemStack input;
	public final ArrayList<StatusEffectInstance> effects;
	public final int color;

	public CauldronRecipe(Identifier id, ItemStack input, ArrayList<StatusEffectInstance> effects, int color)
	{
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
		ItemStack stack = inv.getInput().get(0);
		if(stack.getItem() instanceof LadleItem)
		{
			StatusEffectHelper.createLadle(stack, StatusEffectHelper.getEffects(inv));
			return stack;
		}
		else if(stack.getItem() == AlchemyModItems.THROW_BOTTLE || stack.getItem() == Items.GLASS_BOTTLE)
		{
			PotionUtil.setCustomPotionEffects(stack, StatusEffectHelper.getEffects(inv));
		}
		return null;
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
		return new Identifier(AlchemyMod.MOD_ID, "cauldron_recipe");
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
			int color = new Color(jsonColor.get("r").getAsInt(), jsonColor.get("g").getAsInt(), jsonColor.get("b").getAsInt()).hashCode();
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
