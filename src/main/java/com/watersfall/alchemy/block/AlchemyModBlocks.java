package com.watersfall.alchemy.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;

public class AlchemyModBlocks
{
	public static final BrewingCauldronBlock BREWING_CAULDRON_BLOCK;
	public static final PedestalBlock PEDESTAL_BLOCK;

	static
	{
		BREWING_CAULDRON_BLOCK = new BrewingCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON).ticksRandomly());
		PEDESTAL_BLOCK = new PedestalBlock(FabricBlockSettings.copyOf(Blocks.STONE));
	}
}
