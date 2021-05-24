package net.watersfall.thuwumcraft.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.block.ThuwumcraftBlocks;

public class ThuwumcraftBlockEntities
{
	public static final BlockEntityType<BrewingCauldronEntity> BREWING_CAULDRON_ENTITY;
	public static final BlockEntityType<PedestalEntity> PEDESTAL_ENTITY;
	public static final BlockEntityType<AlchemicalFurnaceEntity> ALCHEMICAL_FURNACE_ENTITY;
	public static final BlockEntityType<ChildBlockEntity> CHILD_BLOCK_ENTITY;
	public static final BlockEntityType<CrucibleEntity> CRUCIBLE_ENTITY;
	public static final BlockEntityType<JarEntity> JAR_ENTITY;
	public static final BlockEntityType<PhialShelfEntity> PHIAL_SHELF_ENTITY;
	public static final BlockEntityType<PipeEntity> ASPECT_PIPE_ENTITY;
	public static final BlockEntityType<CustomSpawnerEntity> CUSTOM_SPAWNER_ENTITY;
	public static final BlockEntityType<CraftingHopperEntity> CRAFTING_HOPPER;
	public static final BlockEntityType<VisLiquifierEntity> VIS_LIQUIFIER_ENTITY;
	public static final BlockEntityType<AspectCraftingEntity> ASPECT_CRAFTING_ENTITY;
	public static final BlockEntityType<PotionSprayerEntity> POTION_SPRAYER;
	public static final BlockEntityType<EssentiaSmelteryEntity> ESSENTIA_SMELTERY_ENTITY;
	public static final BlockEntityType<EssentiaRefineryBlockEntity> ESSENTIA_REFINERY;
	public static final BlockEntityType<ArcaneLampEntity> ARCANE_LAMP_ENTITY;
	public static final BlockEntityType<PortableHoleBlockEntity> PORTABLE_HOLE_ENTITY;
	public static final BlockEntityType<WandWorkbenchEntity> WAND_WORKBENCH;

	static
	{
		BREWING_CAULDRON_ENTITY = FabricBlockEntityTypeBuilder.create(BrewingCauldronEntity::new, ThuwumcraftBlocks.BREWING_CAULDRON_BLOCK).build(null);
		PEDESTAL_ENTITY = FabricBlockEntityTypeBuilder.create(PedestalEntity::new, ThuwumcraftBlocks.PEDESTAL_BLOCK).build(null);
		ALCHEMICAL_FURNACE_ENTITY = FabricBlockEntityTypeBuilder.create(AlchemicalFurnaceEntity::new, ThuwumcraftBlocks.ALCHEMICAL_FURNACE_BLOCK).build(null);
		CHILD_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(ChildBlockEntity::new, ThuwumcraftBlocks.CHILD_BLOCK).build(null);
		CRUCIBLE_ENTITY = FabricBlockEntityTypeBuilder.create(CrucibleEntity::new, ThuwumcraftBlocks.CRUCIBLE_BLOCK).build(null);
		JAR_ENTITY = FabricBlockEntityTypeBuilder.create(JarEntity::new, ThuwumcraftBlocks.JAR_BLOCK).build(null);
		PHIAL_SHELF_ENTITY = FabricBlockEntityTypeBuilder.create(PhialShelfEntity::new, ThuwumcraftBlocks.PHIAL_SHELF_BLOCK).build(null);
		ASPECT_PIPE_ENTITY = FabricBlockEntityTypeBuilder.create(PipeEntity::new, ThuwumcraftBlocks.ASPECT_PIPE_BLOCK).build(null);
		CUSTOM_SPAWNER_ENTITY = FabricBlockEntityTypeBuilder.create(CustomSpawnerEntity::new, ThuwumcraftBlocks.CUSTOM_SPAWNER).build(null);
		CRAFTING_HOPPER = FabricBlockEntityTypeBuilder.create(CraftingHopperEntity::new, ThuwumcraftBlocks.CRAFTING_HOPPER).build(null);
		VIS_LIQUIFIER_ENTITY = FabricBlockEntityTypeBuilder.create(VisLiquifierEntity::new, ThuwumcraftBlocks.VIS_LIQUIFIER).build(null);
		ASPECT_CRAFTING_ENTITY = FabricBlockEntityTypeBuilder.create(AspectCraftingEntity::new, ThuwumcraftBlocks.ASPECT_CRAFTING_BLOCK).build(null);
		POTION_SPRAYER = FabricBlockEntityTypeBuilder.create(PotionSprayerEntity::new, ThuwumcraftBlocks.POTION_SPRAYER_BLOCK).build(null);
		ESSENTIA_SMELTERY_ENTITY = FabricBlockEntityTypeBuilder.create(EssentiaSmelteryEntity::new, ThuwumcraftBlocks.ESSENTIA_SMELTERY).build(null);
		ESSENTIA_REFINERY = register("essentia_refinery", EssentiaRefineryBlockEntity::new, ThuwumcraftBlocks.ESSENTIA_REFINERY_BLOCK);
		ARCANE_LAMP_ENTITY = register("arcane_lamp_entity", ArcaneLampEntity::new, ThuwumcraftBlocks.ARCANE_LAMP_BLOCK);
		PORTABLE_HOLE_ENTITY = register("portable_hole", PortableHoleBlockEntity::new, ThuwumcraftBlocks.PORTABLE_HOLE_BLOCK);
		WAND_WORKBENCH = register("wand_workbench", WandWorkbenchEntity::new, ThuwumcraftBlocks.WAND_WORKBENCH);
	}

	public static void register()
	{
		Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId("brewing_cauldron_entity"), ThuwumcraftBlockEntities.BREWING_CAULDRON_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId("pedestal_entity"), ThuwumcraftBlockEntities.PEDESTAL_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId("alchemical_furnace_entity"), ThuwumcraftBlockEntities.ALCHEMICAL_FURNACE_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId("child_block_entity"), ThuwumcraftBlockEntities.CHILD_BLOCK_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId("brewing_crucible_entity"), ThuwumcraftBlockEntities.CRUCIBLE_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId("jar_entity"), ThuwumcraftBlockEntities.JAR_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId("phial_shelf_entity"), PHIAL_SHELF_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId("aspect_pipe_entity"), ASPECT_PIPE_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId("custom_spawner_entity"), CUSTOM_SPAWNER_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId("crafting_hopper_entity"), CRAFTING_HOPPER);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId("vis_liquifier_entity"), VIS_LIQUIFIER_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId("aspect_crafting_entity"), ASPECT_CRAFTING_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId("potion_sprayer_entity"), POTION_SPRAYER);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId("essentia_smeltery_entity"), ESSENTIA_SMELTERY_ENTITY);
	}

	public static <T extends BlockEntity> BlockEntityType<T> register(String id, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks)
	{
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId(id), FabricBlockEntityTypeBuilder.create(factory, blocks).build(null));
	}
}
