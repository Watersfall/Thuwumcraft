package net.watersfall.thuwumcraft.accessor;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShapedRecipe.class)
public interface ShapedRecipeAccessor
{
	@Accessor
	ItemStack getOutput();

	@Accessor
	String getGroup();

	@Accessor
	int getWidth();

	@Accessor
	int getHeight();
}
