package com.watersfall.poisonedweapons.client;

import com.watersfall.poisonedweapons.PoisonedWeapons;
import com.watersfall.poisonedweapons.block.AlchemyModBlocks;
import com.watersfall.poisonedweapons.blockentity.AlchemyModBlockEntities;
import com.watersfall.poisonedweapons.client.gui.ApothecaryGuideScreen;
import com.watersfall.poisonedweapons.client.renderer.BrewingCauldronEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.color.world.BiomeColors;

@Environment(EnvType.CLIENT)
public class PoisonedWeaponsClient implements ClientModInitializer
{
	@Override public void onInitializeClient()
	{
		ColorProviderRegistry.BLOCK.register(
				(state, view, pos, tintIndex) -> BiomeColors.getWaterColor(view, pos),
				AlchemyModBlocks.BREWING_CAULDRON_BLOCK
		);
		BlockEntityRendererRegistry.INSTANCE.register(AlchemyModBlockEntities.BREWING_CAULDRON_ENTITY, BrewingCauldronEntityRenderer::new);
		ScreenRegistry.register(PoisonedWeapons.APOTHECARY_GUIDE_HANDLER, ApothecaryGuideScreen::new);
	}
}
