package com.watersfall.poisonedweapons;

import com.watersfall.poisonedweapons.block.AlchemyModBlocks;
import com.watersfall.poisonedweapons.blockentity.AlchemyModBlockEntities;
import com.watersfall.poisonedweapons.blockentity.BrewingCauldronEntity;
import com.watersfall.poisonedweapons.event.ApplyAffectEvent;
import com.watersfall.poisonedweapons.item.AlchemyModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PoisonedWeapons implements ModInitializer
{
	public static final String MOD_ID = "waters_alchemy_mod";

	@Override
	public void onInitialize()
	{
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "witchy_spoon"), AlchemyModItems.WITCHY_SPOON_ITEM);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "throw_bottle"), AlchemyModItems.THROW_BOTTLE);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "ladle"), AlchemyModItems.LADLE_ITEM);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "brewing_cauldron"), AlchemyModBlocks.BREWING_CAULDRON_BLOCK);
		AlchemyModBlockEntities.BREWING_CAULDRON_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "brewing_cauldron_entity"), BlockEntityType.Builder.create(BrewingCauldronEntity::new, AlchemyModBlocks.BREWING_CAULDRON_BLOCK).build(null));
		AttackEntityCallback.EVENT.register(new ApplyAffectEvent());
	}
}
