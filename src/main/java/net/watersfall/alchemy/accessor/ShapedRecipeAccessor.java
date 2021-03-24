package net.watersfall.alchemy.accessor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(ShapedRecipe.class)
public interface ShapedRecipeAccessor
{
	@Accessor
	DefaultedList<Ingredient> getInputs();

	@Accessor
	ItemStack getOutput();

	@Accessor
	String getGroup();

	@Accessor
	int getWidth();

	@Accessor
	int getHeight();
}
