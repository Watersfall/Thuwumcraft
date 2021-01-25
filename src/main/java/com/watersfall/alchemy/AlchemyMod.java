package com.watersfall.alchemy;

import com.watersfall.alchemy.block.AlchemyModBlocks;
import com.watersfall.alchemy.block.BrewingCauldronBlock;
import com.watersfall.alchemy.blockentity.AlchemyModBlockEntities;
import com.watersfall.alchemy.blockentity.BrewingCauldronEntity;
import com.watersfall.alchemy.effect.AlchemyModStatusEffects;
import com.watersfall.alchemy.event.ApplyAffectEvent;
import com.watersfall.alchemy.inventory.handler.ApothecaryGuideHandler;
import com.watersfall.alchemy.item.AlchemyModItems;
import com.watersfall.alchemy.recipe.CauldronRecipe;
import com.watersfall.alchemy.recipe.CauldronTypeRecipe;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AlchemyMod implements ModInitializer
{
	public static final String MOD_ID = "waters_alchemy_mod";
	public static final ScreenHandlerType<ApothecaryGuideHandler> APOTHECARY_GUIDE_HANDLER;
	public static final RecipeType<CauldronRecipe> CAULDRON_RECIPE_TYPE;
	public static final RecipeType<CauldronTypeRecipe> CAULDRON_TYPE_RECIPE_TYPE;
	public static final RecipeSerializer<CauldronRecipe> CAULDRON_RECIPE_SERIALIZER;
	public static final RecipeSerializer<CauldronTypeRecipe> CAULDRON_TYPE_RECIPE_SERIALIZER;

	static
	{
		APOTHECARY_GUIDE_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID, "apothecary_guide_handler"), ApothecaryGuideHandler::new);
		CAULDRON_RECIPE_TYPE = Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_ID, "cauldron"), new RecipeType<CauldronRecipe>() {
			@Override
			public String toString()
			{
				return "cauldron";
			}
		});
		CAULDRON_TYPE_RECIPE_TYPE = Registry.register(Registry.RECIPE_TYPE, getId("cauldron_type"), new RecipeType<CauldronTypeRecipe>() {
			@Override
			public String toString()
			{
				return "cauldron_type";
			}
		});
		CAULDRON_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, "cauldron_recipe"), new CauldronRecipe.Serializer(CauldronRecipe::new));
		CAULDRON_TYPE_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, getId("cauldron_type"), new CauldronTypeRecipe.Serializer(CauldronTypeRecipe::new));
	}

	public static Identifier getId(String id)
	{
		return new Identifier(MOD_ID, id);
	}

	@Override
	public void onInitialize()
	{
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "witchy_spoon"), AlchemyModItems.WITCHY_SPOON_ITEM);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "throw_bottle"), AlchemyModItems.THROW_BOTTLE);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "ladle"), AlchemyModItems.LADLE_ITEM);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "apothecary_guide_book"), AlchemyModItems.APOTHECARY_GUIDE);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "brewing_cauldron"), AlchemyModBlocks.BREWING_CAULDRON_BLOCK);
		Registry.register(Registry.STATUS_EFFECT, new Identifier(MOD_ID, "projectile_shield"), AlchemyModStatusEffects.PROJECTILE_SHIELD);
		Registry.register(Registry.STATUS_EFFECT, new Identifier(MOD_ID, "projectile_attraction"), AlchemyModStatusEffects.PROJECTILE_ATTRACTION);
		Registry.register(Registry.STATUS_EFFECT, new Identifier(MOD_ID, "projectile_weakness"), AlchemyModStatusEffects.PROJECTILE_WEAKNESS);
		Registry.register(Registry.STATUS_EFFECT, new Identifier(MOD_ID, "projectile_resistance"), AlchemyModStatusEffects.PROJECTILE_RESISTANCE);
		AlchemyModBlockEntities.BREWING_CAULDRON_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "brewing_cauldron_entity"), BlockEntityType.Builder.create(BrewingCauldronEntity::new, AlchemyModBlocks.BREWING_CAULDRON_BLOCK).build(null));
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> {
			if(success)
			{
				BrewingCauldronBlock.loadIngredients(manager.getRecipeManager());
			}
		});
		ServerLifecycleEvents.SERVER_STARTED.register((server -> BrewingCauldronBlock.loadIngredients(server.getRecipeManager())));
		AttackEntityCallback.EVENT.register(new ApplyAffectEvent());
	}
}
