package net.watersfall.thuwumcraft.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.block.entity.*;

public class ThuwumcraftBlockEntities
{
	public static BlockEntityType<BrewingCauldronEntity> BREWING_CAULDRON;
	public static BlockEntityType<PedestalEntity> PEDESTAL;
	public static BlockEntityType<AlchemicalFurnaceEntity> ALCHEMICAL_FURNACE;
	public static BlockEntityType<ChildBlockEntity> CHILD_BLOCK;
	public static BlockEntityType<CrucibleEntity> CRUCIBLE;
	public static BlockEntityType<JarEntity> JAR;
	public static BlockEntityType<PhialShelfEntity> PHIAL_SHELF;
	public static BlockEntityType<PipeEntity> ASPECT_PIPE;
	public static BlockEntityType<CustomSpawnerEntity> CUSTOM_SPAWNER;
	public static BlockEntityType<CraftingHopperEntity> CRAFTING_HOPPER;
	public static BlockEntityType<VisLiquifierEntity> VIS_LIQUIFIER;
	public static BlockEntityType<AspectCraftingEntity> ASPECT_CRAFTING_TABLE;
	public static BlockEntityType<PotionSprayerEntity> POTION_SPRAYER;
	public static BlockEntityType<EssentiaSmelteryEntity> ESSENTIA_SMELTERY;
	public static BlockEntityType<EssentiaRefineryBlockEntity> ESSENTIA_REFINERY;
	public static BlockEntityType<ArcaneLampEntity> ARCANE_LAMP;
	public static BlockEntityType<PortableHoleBlockEntity> PORTABLE_HOLE;
	public static BlockEntityType<WandWorkbenchEntity> WAND_WORKBENCH;
	public static BlockEntityType<ArcaneSealBlockEntity> ARCANE_SEAL;
	public static BlockEntityType<ThaumatoriumBlockEntity> THAUMATORIUM;
	public static BlockEntityType<HungryChestBlockEntity> HUNGRY_CHEST;

	public static void register()
	{
		BREWING_CAULDRON = register("brewing_cauldron", BrewingCauldronEntity::new, ThuwumcraftBlocks.BREWING_CAULDRON);
		PEDESTAL = register("pedestal", PedestalEntity::new, ThuwumcraftBlocks.PEDESTAL);
		ALCHEMICAL_FURNACE = register("alchemical_furnace", AlchemicalFurnaceEntity::new, ThuwumcraftBlocks.ALCHEMICAL_FURNACE);
		CHILD_BLOCK = register("child_block", ChildBlockEntity::new, ThuwumcraftBlocks.CHILD_BLOCK);
		CRUCIBLE = register("crucible", CrucibleEntity::new, ThuwumcraftBlocks.CRUCIBLE);
		JAR = register("jar", JarEntity::new, ThuwumcraftBlocks.JAR);
		PHIAL_SHELF = register("phial_shelf", PhialShelfEntity::new, ThuwumcraftBlocks.PHIAL_SHELF);
		ASPECT_PIPE = register("brass_pipe", PipeEntity::new, ThuwumcraftBlocks.BRASS_PIPE);
		CUSTOM_SPAWNER = register("custom_spawner", CustomSpawnerEntity::new, ThuwumcraftBlocks.CUSTOM_SPAWNER);
		CRAFTING_HOPPER = register("crafting_hopper", CraftingHopperEntity::new, ThuwumcraftBlocks.CRAFTING_HOPPER);
		VIS_LIQUIFIER = register("vis_liquifier", VisLiquifierEntity::new, ThuwumcraftBlocks.VIS_LIQUIFIER);
		ASPECT_CRAFTING_TABLE = register("aspect_crafting_table", AspectCraftingEntity::new, ThuwumcraftBlocks.ASPECT_CRAFTING_TABLE);
		POTION_SPRAYER = register("potion_sprayer", PotionSprayerEntity::new, ThuwumcraftBlocks.POTION_SPRAYER);
		ESSENTIA_SMELTERY = register("essentia_smeltery", EssentiaSmelteryEntity::new, ThuwumcraftBlocks.ESSENTIA_SMELTERY);
		ESSENTIA_REFINERY = register("essentia_refinery", EssentiaRefineryBlockEntity::new, ThuwumcraftBlocks.ESSENTIA_REFINERY);
		ARCANE_LAMP = register("arcane_lamp_entity", ArcaneLampEntity::new, ThuwumcraftBlocks.ARCANE_LAMP);
		PORTABLE_HOLE = register("portable_hole", PortableHoleBlockEntity::new, ThuwumcraftBlocks.PORTABLE_HOLE);
		WAND_WORKBENCH = register("wand_workbench", WandWorkbenchEntity::new, ThuwumcraftBlocks.WAND_WORKBENCH);
		ARCANE_SEAL = register("arcane_seal", ArcaneSealBlockEntity::new, ThuwumcraftBlocks.ARCANE_SEAL);
		THAUMATORIUM = register("thaumatorium", ThaumatoriumBlockEntity::new, ThuwumcraftBlocks.THAUMATORIUM);
		HUNGRY_CHEST = register("hungry_chest", HungryChestBlockEntity::new, ThuwumcraftBlocks.HUNGRY_CHEST);
	}

	public static <T extends BlockEntity> BlockEntityType<T> register(String id, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks)
	{
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, Thuwumcraft.getId(id), FabricBlockEntityTypeBuilder.create(factory, blocks).build(null));
	}
}
