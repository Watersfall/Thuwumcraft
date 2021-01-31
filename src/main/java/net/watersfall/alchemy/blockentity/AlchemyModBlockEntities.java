package net.watersfall.alchemy.blockentity;

import net.watersfall.alchemy.block.AlchemyModBlocks;
import net.minecraft.block.entity.BlockEntityType;

public class AlchemyModBlockEntities
{
	public static final BlockEntityType<BrewingCauldronEntity> BREWING_CAULDRON_ENTITY;
	public static final BlockEntityType<PedestalEntity> PEDESTAL_ENTITY;
	public static final BlockEntityType<AlchemicalFurnaceEntity> ALCHEMICAL_FURNACE_ENTITY;
	public static final BlockEntityType<ChildBlockEntity> CHILD_BLOCK_ENTITY;

	static
	{
		BREWING_CAULDRON_ENTITY = BlockEntityType.Builder.create(BrewingCauldronEntity::new, AlchemyModBlocks.BREWING_CAULDRON_BLOCK).build(null);
		PEDESTAL_ENTITY = BlockEntityType.Builder.create(PedestalEntity::new, AlchemyModBlocks.PEDESTAL_BLOCK).build(null);
		ALCHEMICAL_FURNACE_ENTITY = BlockEntityType.Builder.create(AlchemicalFurnaceEntity::new, AlchemyModBlocks.ALCHEMICAL_FURNACE_BLOCK).build(null);
		CHILD_BLOCK_ENTITY = BlockEntityType.Builder.create(ChildBlockEntity::new, AlchemyModBlocks.CHILD_BLOCK).build(null);
	}
}
