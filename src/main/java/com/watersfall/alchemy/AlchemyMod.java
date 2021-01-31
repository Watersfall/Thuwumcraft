package com.watersfall.alchemy;

import com.watersfall.alchemy.block.AlchemyModBlocks;
import com.watersfall.alchemy.blockentity.AlchemyModBlockEntities;
import com.watersfall.alchemy.blockentity.PedestalEntity;
import com.watersfall.alchemy.effect.AlchemyModStatusEffects;
import com.watersfall.alchemy.event.ApplyAffectEvent;
import com.watersfall.alchemy.inventory.handler.ApothecaryGuideHandler;
import com.watersfall.alchemy.item.AlchemyModItems;
import com.watersfall.alchemy.multiblock.MultiBlockRegistry;
import com.watersfall.alchemy.recipe.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AlchemyMod implements ModInitializer
{
	public static final String MOD_ID = "waters_alchemy_mod";
	public static final ScreenHandlerType<ApothecaryGuideHandler> APOTHECARY_GUIDE_HANDLER;
	private static Tag<Item> INGREDIENT_TAG;

	static
	{
		APOTHECARY_GUIDE_HANDLER = ScreenHandlerRegistry.registerSimple(getId("apothecary_guide_handler"), ApothecaryGuideHandler::new);
	}

	private static Set<Item> getAllIngredients(MinecraftServer server)
	{
		Set<Item> set = new HashSet<>();
		server.getRecipeManager().listAllOfType(AlchemyModRecipes.CAULDRON_INGREDIENTS).forEach((item) -> set.add(item.getInput().getItem()));
		return set;
	}

	public static Identifier getId(String id)
	{
		return new Identifier(MOD_ID, id);
	}

	public static Tag<Item> getIngredientTag()
	{
		return INGREDIENT_TAG;
	}

	private static void setIngredientTag(Tag<Item> ingredientTag)
	{
		INGREDIENT_TAG = ingredientTag;
	}

	@Override
	public void onInitialize()
	{
		Registry.register(Registry.ITEM, getId("witchy_spoon"), AlchemyModItems.WITCHY_SPOON_ITEM);
		Registry.register(Registry.ITEM, getId("throw_bottle"), AlchemyModItems.THROW_BOTTLE);
		Registry.register(Registry.ITEM, getId("ladle"), AlchemyModItems.LADLE_ITEM);
		Registry.register(Registry.ITEM, getId("apothecary_guide_book"), AlchemyModItems.APOTHECARY_GUIDE);
		Registry.register(Registry.ITEM, getId("pedestal"), AlchemyModItems.PEDESTAL_ITEM);
		Registry.register(Registry.BLOCK, getId("brewing_cauldron"), AlchemyModBlocks.BREWING_CAULDRON_BLOCK);
		Registry.register(Registry.BLOCK, getId("pedestal"), AlchemyModBlocks.PEDESTAL_BLOCK);
		Registry.register(Registry.BLOCK, getId("alchemical_furnace"), AlchemyModBlocks.ALCHEMICAL_FURNACE_BLOCK);
		Registry.register(Registry.BLOCK, getId("child_block"), AlchemyModBlocks.CHILD_BLOCK);
		Registry.register(Registry.STATUS_EFFECT, getId("projectile_shield"), AlchemyModStatusEffects.PROJECTILE_SHIELD);
		Registry.register(Registry.STATUS_EFFECT, getId("projectile_attraction"), AlchemyModStatusEffects.PROJECTILE_ATTRACTION);
		Registry.register(Registry.STATUS_EFFECT, getId("projectile_weakness"), AlchemyModStatusEffects.PROJECTILE_WEAKNESS);
		Registry.register(Registry.STATUS_EFFECT, getId("projectile_resistance"), AlchemyModStatusEffects.PROJECTILE_RESISTANCE);
		Registry.register(Registry.RECIPE_TYPE, getId("cauldron_ingredient"), AlchemyModRecipes.CAULDRON_INGREDIENTS);
		Registry.register(Registry.RECIPE_TYPE, getId("cauldron_recipe"), AlchemyModRecipes.CAULDRON_INGREDIENT_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, getId("cauldron_item"), AlchemyModRecipes.CAULDRON_ITEM_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, getId("pedestal_crafting"), AlchemyModRecipes.PEDESTAL_RECIPE);
		Registry.register(Registry.RECIPE_SERIALIZER, getId("cauldron_ingredient"), AlchemyModRecipes.CAULDRON_INGREDIENTS_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, getId("cauldron_recipe"), AlchemyModRecipes.CAULDRON_INGREDIENT_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, getId("cauldron_item"), AlchemyModRecipes.CAULDRON_ITEM_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, getId("pedestal_crafting"), AlchemyModRecipes.PEDESTAL_RECIPE_SERIALIZER);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, getId("brewing_cauldron_entity"), AlchemyModBlockEntities.BREWING_CAULDRON_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, getId("pedestal_entity"), AlchemyModBlockEntities.PEDESTAL_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, getId("alchemical_furnace_entity"), AlchemyModBlockEntities.ALCHEMICAL_FURNACE_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, getId("child_block_entity"), AlchemyModBlockEntities.CHILD_BLOCK_ENTITY);
		AttackEntityCallback.EVENT.register(new ApplyAffectEvent());
		ServerLifecycleEvents.SERVER_STARTED.register((server -> setIngredientTag(Tag.of(getAllIngredients(server)))));
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> {
			if(success)
			{
				setIngredientTag(Tag.of(getAllIngredients(server)));
			}
		});
		ServerTickEvents.END_SERVER_TICK.register(server -> MultiBlockRegistry.INSTANCE.tick());
		DispenserBlock.registerBehavior(AlchemyModItems.WITCHY_SPOON_ITEM, ((pointer, stack) -> {
			Direction direction = pointer.getWorld().getBlockState(pointer.getBlockPos()).get(Properties.FACING);
			if(pointer.getWorld().getBlockEntity(pointer.getBlockPos().offset(direction)) instanceof PedestalEntity)
			{
				World world = pointer.getWorld();
				PedestalEntity entity = (PedestalEntity) world.getBlockEntity(pointer.getBlockPos().offset(direction));
				Optional<PedestalRecipe> recipeOptional = pointer.getWorld().getRecipeManager().getFirstMatch(AlchemyModRecipes.PEDESTAL_RECIPE, entity, world);
				recipeOptional.ifPresent(entity::beginCraft);
			}
			return stack;
		}));
	}
}
