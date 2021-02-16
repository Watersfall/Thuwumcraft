package net.watersfall.alchemy.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.block.AlchemyBlocks;
import net.minecraft.block.entity.BlockEntityType;

public class AlchemyBlockEntities
{
	public static final BlockEntityType<BrewingCauldronEntity> BREWING_CAULDRON_ENTITY;
	public static final BlockEntityType<PedestalEntity> PEDESTAL_ENTITY;
	public static final BlockEntityType<AlchemicalFurnaceEntity> ALCHEMICAL_FURNACE_ENTITY;
	public static final BlockEntityType<ChildBlockEntity> CHILD_BLOCK_ENTITY;
	public static final BlockEntityType<CrucibleEntity> CRUCIBLE_ENTITY;

	static
	{
		BREWING_CAULDRON_ENTITY = FabricBlockEntityTypeBuilder.create(BrewingCauldronEntity::new, AlchemyBlocks.BREWING_CAULDRON_BLOCK).build(null);
		PEDESTAL_ENTITY = FabricBlockEntityTypeBuilder.create(PedestalEntity::new, AlchemyBlocks.PEDESTAL_BLOCK).build(null);
		ALCHEMICAL_FURNACE_ENTITY = FabricBlockEntityTypeBuilder.create(AlchemicalFurnaceEntity::new, AlchemyBlocks.ALCHEMICAL_FURNACE_BLOCK).build(null);
		CHILD_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(ChildBlockEntity::new, AlchemyBlocks.CHILD_BLOCK).build(null);
		CRUCIBLE_ENTITY = FabricBlockEntityTypeBuilder.create(CrucibleEntity::new, AlchemyBlocks.CRUCIBLE_BLOCK).build(null);
	}

	public static void register()
	{
		Registry.register(Registry.BLOCK_ENTITY_TYPE, AlchemyMod.getId("brewing_cauldron_entity"), AlchemyBlockEntities.BREWING_CAULDRON_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, AlchemyMod.getId("pedestal_entity"), AlchemyBlockEntities.PEDESTAL_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, AlchemyMod.getId("alchemical_furnace_entity"), AlchemyBlockEntities.ALCHEMICAL_FURNACE_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, AlchemyMod.getId("child_block_entity"), AlchemyBlockEntities.CHILD_BLOCK_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, AlchemyMod.getId("brewing_crucible_entity"), AlchemyBlockEntities.CRUCIBLE_ENTITY);
	}
}
