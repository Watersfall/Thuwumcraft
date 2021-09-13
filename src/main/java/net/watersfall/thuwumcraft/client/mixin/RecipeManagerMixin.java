package net.watersfall.thuwumcraft.client.mixin;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.watersfall.thuwumcraft.client.hooks.ClientHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin
{
	@Inject(method = "setRecipes", at = @At("TAIL"))
	public void thuwumcraft$onRecipeLoad(Iterable<Recipe<?>> recipes, CallbackInfo info)
	{
		ClientHooks.onSetRecipes();
	}
}
