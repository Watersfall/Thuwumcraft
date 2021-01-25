package com.watersfall.alchemy.client;

import com.watersfall.alchemy.AlchemyMod;
import com.watersfall.alchemy.block.AlchemyModBlocks;
import com.watersfall.alchemy.blockentity.AlchemyModBlockEntities;
import com.watersfall.alchemy.client.gui.ApothecaryGuideScreen;
import com.watersfall.alchemy.client.renderer.BrewingCauldronEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.color.world.BiomeColors;

@Environment(EnvType.CLIENT)
public class AlchemyModClient implements ClientModInitializer
{
	@Override public void onInitializeClient()
	{
		ColorProviderRegistry.BLOCK.register(
				(state, view, pos, tintIndex) -> BiomeColors.getWaterColor(view, pos),
				AlchemyModBlocks.BREWING_CAULDRON_BLOCK
		);
		BlockEntityRendererRegistry.INSTANCE.register(AlchemyModBlockEntities.BREWING_CAULDRON_ENTITY, BrewingCauldronEntityRenderer::new);
		ScreenRegistry.register(AlchemyMod.APOTHECARY_GUIDE_HANDLER, ApothecaryGuideScreen::new);
	}
}
