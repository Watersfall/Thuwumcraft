package net.watersfall.thuwumcraft.compat.rei.client;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.OverlayDecider;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultShapedDisplay;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultShapelessDisplay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.ActionResult;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.client.gui.screen.ResearchBookScreen;
import net.watersfall.thuwumcraft.client.gui.screen.ResearchScreen;
import net.watersfall.thuwumcraft.client.gui.screen.WandWorkbenchScreen;
import net.watersfall.thuwumcraft.compat.rei.client.category.*;
import net.watersfall.thuwumcraft.compat.rei.client.display.*;
import net.watersfall.thuwumcraft.recipe.*;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRecipes;

import java.util.List;

public class ThuwumcraftReiCompatClient implements REIClientPlugin
{
	public static final CategoryIdentifier<CrystalDisplay> CRYSTAL = CategoryIdentifier.of(Thuwumcraft.getId("aspect_crafting"));
	public static final CategoryIdentifier<InfusionDisplay> INFUSION = CategoryIdentifier.of(Thuwumcraft.getId("infusion"));
	public static final CategoryIdentifier<CrucibleDisplay> CRUCIBLE = CategoryIdentifier.of(Thuwumcraft.getId("crucible"));
	public static final CategoryIdentifier<CauldronEffectDisplay> CAULDRON_EFFECT = CategoryIdentifier.of(Thuwumcraft.getId("cauldron_status_effect"));
	public static final CategoryIdentifier<CauldronCraftingDisplay> CAULDRON_CRAFTING = CategoryIdentifier.of(Thuwumcraft.getId("cauldron_crafting"));
	public static final CategoryIdentifier<CauldronItemCraftingDisplay> CAULDRON_ITEM = CategoryIdentifier.of(Thuwumcraft.getId("cauldron_item"));
	public static final CategoryIdentifier<AspectDisplay> ASPECT = CategoryIdentifier.of(Thuwumcraft.getId("aspect"));

	@Override
	public void registerExclusionZones(ExclusionZones zones)
	{
		zones.register(WandWorkbenchScreen.class, screen -> {
			return List.of(new Rectangle(screen.getX() + 176, screen.getY(), 80, 97));
		});
	}

	@Override
	public void registerCategories(CategoryRegistry registry)
	{
		registry.add(new CrystalDisplayCategory());
		registry.add(new InfusionDisplayCategory());
		registry.add(new CrucibleDisplayCategory());
		registry.add(new CauldronEffectDisplayCategory());
		registry.add(new CauldronCraftingDisplayCategory());
		registry.add(new CauldronItemDisplayCategory());
		registry.add(new AspectCategory());
		registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(ThuwumcraftItems.ASPECT_CRAFTING_TABLE_BLOCK));
		registry.addWorkstations(CRYSTAL, EntryStacks.of(ThuwumcraftItems.ASPECT_CRAFTING_TABLE_BLOCK));
		registry.addWorkstations(INFUSION, EntryStacks.of(ThuwumcraftItems.PEDESTAL));
		registry.addWorkstations(CRUCIBLE, EntryStacks.of(ThuwumcraftItems.CRUCIBLE_BLOCK));
		registry.addWorkstations(CAULDRON_EFFECT, EntryStacks.of(ThuwumcraftItems.BREWING_CAULDRON_BLOCK));
		registry.addWorkstations(CAULDRON_CRAFTING, EntryStacks.of(ThuwumcraftItems.BREWING_CAULDRON_BLOCK));
		registry.addWorkstations(CAULDRON_ITEM, EntryStacks.of(ThuwumcraftItems.BREWING_CAULDRON_BLOCK));
		registry.addWorkstations(ASPECT, EntryStacks.of(ThuwumcraftItems.CRUCIBLE_BLOCK), EntryStacks.of(ThuwumcraftItems.ESSENTIA_SMELTERY_BLOCK));
	}

	@Override
	public void registerDisplays(DisplayRegistry registry)
	{
		registry.registerRecipeFiller(AspectCraftingShapedRecipe.class, RecipeType.CRAFTING, CrystalDisplay::new);
		registry.registerRecipeFiller(PedestalRecipe.class, ThuwumcraftRecipes.PEDESTAL, InfusionDisplay::new);
		registry.registerRecipeFiller(CrucibleRecipe.class, ThuwumcraftRecipes.CRUCIBLE, CrucibleDisplay::new);
		registry.registerRecipeFiller(CauldronIngredient.class, ThuwumcraftRecipes.CAULDRON_INGREDIENTS, CauldronEffectDisplay::new);
		registry.registerRecipeFiller(CauldronIngredientRecipe.class, ThuwumcraftRecipes.CAULDRON_INGREDIENT, CauldronCraftingDisplay::new);
		registry.registerRecipeFiller(CauldronItemRecipe.class, ThuwumcraftRecipes.CAULDRON_ITEM, CauldronItemCraftingDisplay::new);
		registry.registerRecipeFiller(GogglesRecipe.class, RecipeType.CRAFTING, GogglesDisplay::new);
		registry.registerRecipeFiller(AspectIngredient.class, ThuwumcraftRecipes.ASPECT_INGREDIENTS, AspectDisplay::new);
		registry.registerFiller(ResearchUnlockedRecipe.class, (recipe) -> {
			if(recipe.recipe instanceof ShapedRecipe shapedRecipe)
			{
				return new DefaultShapedDisplay(shapedRecipe);
			}
			else if(recipe.recipe instanceof ShapelessRecipe shapelessRecipe)
			{
				return new DefaultShapelessDisplay(shapelessRecipe);
			}
			return null;
		});
	}

	@Override
	public void registerScreens(ScreenRegistry registry)
	{
		registry.registerDecider(new OverlayDecider()
		{
			@Override
			public <R extends Screen> boolean isHandingScreen(Class<R> screen)
			{
				return screen == ResearchScreen.class || screen == ResearchBookScreen.class;
			}

			@Override
			public ActionResult shouldScreenBeOverlaid(Class<?> screen)
			{
				return ActionResult.FAIL;
			}
		});
	}

}
