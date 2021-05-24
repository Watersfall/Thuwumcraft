package net.watersfall.thuwumcraft.accessor;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShapelessRecipe.class)
public interface ShapelessRecipeAccessor
{
	@Accessor
	DefaultedList<Ingredient> getInput();

	@Accessor
	ItemStack getOutput();

	@Accessor
	String getGroup();
}
