package net.watersfall.thuwumcraft.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.abilities.item.WandAbilityImpl;
import net.watersfall.thuwumcraft.api.abilities.item.WandAbility;
import net.watersfall.thuwumcraft.item.wand.WandCapMaterial;
import net.watersfall.thuwumcraft.item.wand.WandCoreMaterial;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRecipes;
import net.watersfall.wet.api.abilities.AbilityProvider;

public class WandRecipe extends ShapelessRecipe
{
	private final Identifier id;
	private final ShapelessRecipe recipe;

	public WandRecipe(Identifier id, ShapelessRecipe recipe)
	{
		super(recipe.getId(), recipe.getGroup(), recipe.getOutput(), recipe.getIngredients());
		this.id = id;
		this.recipe = recipe;
	}

	@Override
	public boolean matches(CraftingInventory inventory, World world)
	{
		return recipe.matches(inventory, world);
	}

	@Override
	public ItemStack craft(CraftingInventory inventory)
	{
		ItemStack stack = recipe.craft(inventory);
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		WandAbility ability = new WandAbilityImpl();
		WandCapMaterial cap = null;
		WandCoreMaterial core = null;
		for(int i = 0; i < inventory.size(); i++)
		{
			if(cap == null && WandCapMaterial.REGISTRY.getByItem(inventory.getStack(i).getItem()) != null)
			{
				cap = WandCapMaterial.REGISTRY.getByItem(inventory.getStack(i).getItem());
			}
			if(core == null && WandCoreMaterial.REGISTRY.getByItem(inventory.getStack(i).getItem()) != null)
			{
				core = WandCoreMaterial.REGISTRY.getByItem(inventory.getStack(i).getItem());
			}
		}
		ability.setWandCore(core);
		ability.setWandCap(cap);
		provider.addAbility(ability);
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
		return recipe.getOutput();
	}

	@Override
	public Identifier getId()
	{
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return ThuwumcraftRecipes.WAND_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return RecipeType.CRAFTING;
	}

	public static class Serializer implements RecipeSerializer<WandRecipe>
	{
		@Override
		public WandRecipe read(Identifier id, JsonObject json)
		{
			return new WandRecipe(id, RecipeSerializer.SHAPELESS.read(id, json));
		}

		@Override
		public WandRecipe read(Identifier id, PacketByteBuf buf)
		{
			return new WandRecipe(id, RecipeSerializer.SHAPELESS.read(id, buf));
		}

		@Override
		public void write(PacketByteBuf buf, WandRecipe recipe)
		{
			RecipeSerializer.SHAPELESS.write(buf, recipe.recipe);
		}
	}
}
