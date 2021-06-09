package net.watersfall.thuwumcraft.client.mixin;

import net.minecraft.recipe.Recipe;
import net.watersfall.thuwumcraft.api.client.recipe.BookRenderableRecipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Recipe.class)
public interface RecipeMixin extends BookRenderableRecipe
{
}
