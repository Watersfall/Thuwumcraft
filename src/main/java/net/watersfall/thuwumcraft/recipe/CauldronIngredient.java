package net.watersfall.thuwumcraft.recipe;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.abilities.item.StatusEffectItemImpl;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.inventory.BrewingCauldronInventory;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRecipes;
import net.watersfall.thuwumcraft.util.StatusEffectHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CauldronIngredient implements Recipe<BrewingCauldronInventory>
{
	private final Identifier id;
	private final ItemStack input;
	private final List<StatusEffectInstance> effects;
	private final int color;

	public static final String ITEM = "item";
	public static final String COLOR = "color";
	public static final String R = "r";
	public static final String G = "g";
	public static final String B = "b";
	public static final String EFFECTS = "effects";
	public static final String EFFECT = "effect";
	public static final String DURATION = "duration";
	public static final String AMPLIFIER = "amplifier";

	public CauldronIngredient(Identifier id, ItemStack input, ArrayList<StatusEffectInstance> effects, int color)
	{
		this.id = id;
		this.input = input;
		this.effects = ImmutableList.copyOf(effects);
		this.color = color;
	}

	@Override
	public boolean matches(BrewingCauldronInventory inv, World world)
	{
		return inv.getInput().get(0).getItem() == this.getInput().getItem();
	}

	@Override
	public ItemStack craft(BrewingCauldronInventory inv)
	{
		return inv.getInput().get(0);
	}

	public ItemStack craft(BrewingCauldronInventory inventory, CauldronIngredientRecipe recipe, World world)
	{
		ItemStack stack = inventory.getInput().get(0).copy();
		Set<StatusEffectInstance> effects = StatusEffectHelper.getEffects(inventory, world.getRecipeManager(), world);
		if(effects != StatusEffectHelper.INVALID_RECIPE)
		{
			if(recipe.getCraftingAction() == CauldronIngredientRecipe.CraftingAction.ADD_EFFECTS)
			{
				StatusEffectHelper.createItem(stack, StatusEffectHelper.getEffects(inventory, world.getRecipeManager(), world));
				stack.getTag().putInt(StatusEffectHelper.USES, recipe.getUses());
			}
			else if(recipe.getCraftingAction() == CauldronIngredientRecipe.CraftingAction.CREATE_POTION)
			{
				stack = recipe.getOutput().copy();
				PotionUtil.setCustomPotionEffects(stack, StatusEffectHelper.getEffects(inventory, world.getRecipeManager(), world));
				return stack;
			}
			else if(recipe.getCraftingAction() == CauldronIngredientRecipe.CraftingAction.CREATE_ITEM_NO_EFFECT)
			{
				stack = recipe.getOutput().copy();
			}
			else if(recipe.getCraftingAction() == CauldronIngredientRecipe.CraftingAction.CREATE_ITEM_EFFECT)
			{
				stack = recipe.getOutput().copy();
				PotionUtil.setCustomPotionEffects(stack, StatusEffectHelper.getEffects(inventory, world.getRecipeManager(), world));
				stack.getTag().putInt(StatusEffectHelper.USES, recipe.getUses());
			}
			else if(recipe.getCraftingAction() == CauldronIngredientRecipe.CraftingAction.CREATE_BINDING)
			{
				stack = recipe.getOutput().copy();
				AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
				StatusEffectHelper.getEffects(inventory, world.getRecipeManager(), world);
				StatusEffectItemImpl ability = new StatusEffectItemImpl(StatusEffectHelper.getEffects(inventory, world.getRecipeManager(), world).stream().toList(), 5);
				provider.addAbility(ability);
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
		return ThuwumcraftRecipes.CAULDRON_INGREDIENTS_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return ThuwumcraftRecipes.CAULDRON_INGREDIENTS;
	}

	public ItemStack getInput()
	{
		return input;
	}

	public List<StatusEffectInstance> getEffects()
	{
		return effects;
	}

	public int getColor()
	{
		return color;
	}

	public static class Serializer implements RecipeSerializer<CauldronIngredient>
	{
		@Override
		public CauldronIngredient read(Identifier id, JsonObject json)
		{
			ItemStack stack = new ItemStack(Registry.ITEM.get(new Identifier(json.get(ITEM).getAsString())));
			JsonObject jsonColor = json.getAsJsonObject(COLOR);
			int color = new Color(jsonColor.get(R).getAsInt(), jsonColor.get(G).getAsInt(), jsonColor.get(B).getAsInt(), 0).hashCode();
			JsonArray array = json.getAsJsonArray(EFFECTS);
			ArrayList<StatusEffectInstance> effects = new ArrayList<>(array.size());
			array.forEach((object) -> {
				StatusEffect effect = Registry.STATUS_EFFECT.get(Identifier.tryParse(object.getAsJsonObject().get(EFFECT).getAsString()));
				int duration = object.getAsJsonObject().get(DURATION).getAsInt();
				int amplifier = object.getAsJsonObject().get(AMPLIFIER).getAsInt();
				effects.add(new StatusEffectInstance(effect, duration, amplifier));
			});
			return new CauldronIngredient(id, stack, effects, color);
		}

		@Override
		public CauldronIngredient read(Identifier id, PacketByteBuf buf)
		{
			ItemStack stack = buf.readItemStack();
			int size = buf.readByte();
			int color = buf.readInt();
			ArrayList<StatusEffectInstance> list = new ArrayList<>(size);
			for(int i = 0; i < size; i++)
			{
				list.add(StatusEffectInstance.fromNbt(buf.readNbt()));
			}
			return new CauldronIngredient(id, stack, list, color);
		}

		@Override
		public void write(PacketByteBuf buf, CauldronIngredient recipe)
		{
			buf.writeItemStack(recipe.getInput());
			buf.writeByte(recipe.getEffects().size());
			buf.writeInt(recipe.getColor());
			for(int i = 0; i < recipe.getEffects().size(); i++)
			{
				buf.writeNbt(recipe.getEffects().get(i).writeNbt(new NbtCompound()));
			}
		}
	}
}
