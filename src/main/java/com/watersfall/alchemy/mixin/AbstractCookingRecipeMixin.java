package com.watersfall.alchemy.mixin;

import com.watersfall.alchemy.accessor.waters_AbstractCookingRecipeInputAccessor;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractCookingRecipe.class)
public class AbstractCookingRecipeMixin implements waters_AbstractCookingRecipeInputAccessor
{
	@Shadow @Final protected Ingredient input;

	@Override
	public Ingredient getInput()
	{
		return this.input;
	}
}
