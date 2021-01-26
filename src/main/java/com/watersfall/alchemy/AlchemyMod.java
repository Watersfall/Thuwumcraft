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
import com.watersfall.alchemy.util.StatusEffectHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
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
		APOTHECARY_GUIDE_HANDLER = ScreenHandlerRegistry.registerSimple(getId("apothecary_guide_handler"), ApothecaryGuideHandler::new);
		CAULDRON_RECIPE_TYPE = Registry.register(Registry.RECIPE_TYPE, getId("cauldron_ingredient"), new RecipeType<CauldronRecipe>() {
			@Override
			public String toString()
			{
				return "cauldron_ingredient";
			}
		});
		CAULDRON_TYPE_RECIPE_TYPE = Registry.register(Registry.RECIPE_TYPE, getId("cauldron_recipe"), new RecipeType<CauldronTypeRecipe>() {
			@Override
			public String toString()
			{
				return "cauldron_recipe";
			}
		});
		CAULDRON_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, getId("cauldron_ingredient"), new CauldronRecipe.Serializer(CauldronRecipe::new));
		CAULDRON_TYPE_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, getId("cauldron_recipe"), new CauldronTypeRecipe.Serializer(CauldronTypeRecipe::new));
	}

	public static Identifier getId(String id)
	{
		return new Identifier(MOD_ID, id);
	}

	@Override
	public void onInitialize()
	{
		Registry.register(Registry.ITEM, getId("witchy_spoon"), AlchemyModItems.WITCHY_SPOON_ITEM);
		Registry.register(Registry.ITEM, getId("throw_bottle"), AlchemyModItems.THROW_BOTTLE);
		Registry.register(Registry.ITEM, getId("ladle"), AlchemyModItems.LADLE_ITEM);
		Registry.register(Registry.ITEM, getId("apothecary_guide_book"), AlchemyModItems.APOTHECARY_GUIDE);
		Registry.register(Registry.BLOCK, getId("brewing_cauldron"), AlchemyModBlocks.BREWING_CAULDRON_BLOCK);
		Registry.register(Registry.STATUS_EFFECT, getId("projectile_shield"), AlchemyModStatusEffects.PROJECTILE_SHIELD);
		Registry.register(Registry.STATUS_EFFECT, getId("projectile_attraction"), AlchemyModStatusEffects.PROJECTILE_ATTRACTION);
		Registry.register(Registry.STATUS_EFFECT, getId("projectile_weakness"), AlchemyModStatusEffects.PROJECTILE_WEAKNESS);
		Registry.register(Registry.STATUS_EFFECT, getId("projectile_resistance"), AlchemyModStatusEffects.PROJECTILE_RESISTANCE);
		AlchemyModBlockEntities.BREWING_CAULDRON_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, getId("brewing_cauldron_entity"), BlockEntityType.Builder.create(BrewingCauldronEntity::new, AlchemyModBlocks.BREWING_CAULDRON_BLOCK).build(null));
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> {
			if(success)
			{
				BrewingCauldronBlock.loadIngredients(manager.getRecipeManager());
			}
		});
		ServerLifecycleEvents.SERVER_STARTED.register((server -> BrewingCauldronBlock.loadIngredients(server.getRecipeManager())));
		AttackEntityCallback.EVENT.register(new ApplyAffectEvent());
		ItemTooltipCallback.EVENT.register(((stack, context, tooltip) -> {
			if(stack.getTag() != null && !stack.getTag().isEmpty())
			{
				CompoundTag tag = stack.getTag();
				if(tag.contains(StatusEffectHelper.EFFECTS_LIST))
				{
					ListTag list = tag.getList(StatusEffectHelper.EFFECTS_LIST, NbtType.COMPOUND);
					if(list.size() > 0)
					{
						tooltip.add(StatusEffectHelper.APPLIED_EFFECTS);
						list.forEach((effect) -> {
							tooltip.add(StatusEffectHelper.getEffectText(StatusEffectHelper.getEffectFromTag((CompoundTag) effect), true));
						});
					}
					else
					{
						tooltip.add(StatusEffectHelper.NO_EFFECT);
					}
				}
			}
		}));
	}
}
