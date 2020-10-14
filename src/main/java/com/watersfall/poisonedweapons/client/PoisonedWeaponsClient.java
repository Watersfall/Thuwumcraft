package com.watersfall.poisonedweapons.client;

import com.watersfall.poisonedweapons.block.AlchemyModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
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
	}
}
