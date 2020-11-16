package com.watersfall.poisonedweapons;

import com.watersfall.poisonedweapons.block.AlchemyModBlocks;
import com.watersfall.poisonedweapons.blockentity.AlchemyModBlockEntities;
import com.watersfall.poisonedweapons.blockentity.BrewingCauldronEntity;
import com.watersfall.poisonedweapons.effect.AlchemyModStatusEffects;
import com.watersfall.poisonedweapons.event.ApplyAffectEvent;
import com.watersfall.poisonedweapons.inventory.handler.ApothecaryGuideHandler;
import com.watersfall.poisonedweapons.item.AlchemyModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PoisonedWeapons implements ModInitializer
{
	public static final String MOD_ID = "waters_alchemy_mod";
	public static final ScreenHandlerType<ApothecaryGuideHandler> APOTHECARY_GUIDE_HANDLER;

	static
	{
		APOTHECARY_GUIDE_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID, "apothecary_guide_handler"), ApothecaryGuideHandler::new);
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
		AttackEntityCallback.EVENT.register(new ApplyAffectEvent());
	}
}
