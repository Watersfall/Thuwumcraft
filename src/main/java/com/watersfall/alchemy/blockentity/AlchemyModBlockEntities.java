package com.watersfall.alchemy.blockentity;

import com.watersfall.alchemy.block.AlchemyModBlocks;
import net.minecraft.block.entity.BlockEntityType;

public class AlchemyModBlockEntities
{
	public static final BlockEntityType<BrewingCauldronEntity> BREWING_CAULDRON_ENTITY;
	public static final BlockEntityType<PedestalEntity> PEDESTAL_ENTITY;

	static
	{
		BREWING_CAULDRON_ENTITY = BlockEntityType.Builder.create(BrewingCauldronEntity::new, AlchemyModBlocks.BREWING_CAULDRON_BLOCK).build(null);
		PEDESTAL_ENTITY = BlockEntityType.Builder.create(PedestalEntity::new, AlchemyModBlocks.PEDESTAL_BLOCK).build(null);
	}
}
