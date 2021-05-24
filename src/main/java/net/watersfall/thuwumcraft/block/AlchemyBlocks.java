package net.watersfall.thuwumcraft.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.AlchemyMod;
import net.watersfall.thuwumcraft.fluid.AlchemyFluids;

public class AlchemyBlocks
{
	public static final BrewingCauldronBlock BREWING_CAULDRON_BLOCK;
	public static final PedestalBlock PEDESTAL_BLOCK;
	public static final AlchemicalFurnaceBlock ALCHEMICAL_FURNACE_BLOCK;
	public static final ChildBlock CHILD_BLOCK;
	public static final CrucibleBlock CRUCIBLE_BLOCK;
	public static final JarBlock JAR_BLOCK;
	public static final PhialShelfBlock PHIAL_SHELF_BLOCK;
	public static final PipeBlock ASPECT_PIPE_BLOCK;
	public static final Block SPAWNER_FRAME;
	public static final CustomSpawnerBlock CUSTOM_SPAWNER;
	public static final NekomancyBlock NEKOMANCY_TABLE;
	public static final CraftingHopper CRAFTING_HOPPER;
	public static final VisLiquifierBlock VIS_LIQUIFIER;
	public static final AspectCraftingBlock ASPECT_CRAFTING_BLOCK;
	public static final PotionSprayerBlock POTION_SPRAYER_BLOCK;
	public static final EssentiaSmeltery ESSENTIA_SMELTERY;
	public static final EssentiaRefineryBlock ESSENTIA_REFINERY_BLOCK;
	public static final ArcaneLampBlock ARCANE_LAMP_BLOCK;
	public static final ArcaneLightBlock ARCANE_LIGHT_BLOCK;
	public static final FluidBlock DIMENSIONAL_FLUID_BLOCK;
	public static final Block DEEPSLATE_GRASS;
	public static final Block GLOWING_DEEPSLATE;
	public static final LeavesBlock SILVERWOOD_LEAVES;
	public static final PillarBlock SILVERWOOD_LOG;
	public static final PortableHoleBlock PORTABLE_HOLE_BLOCK;
	public static final WandWorkbenchBlock WAND_WORKBENCH;

	static
	{
		BREWING_CAULDRON_BLOCK = register(AlchemyMod.getId("brewing_cauldron"), new BrewingCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON).ticksRandomly()));
		PEDESTAL_BLOCK = register(AlchemyMod.getId("pedestal"), new PedestalBlock(FabricBlockSettings.copyOf(Blocks.STONE).luminance(7).nonOpaque()));
		ALCHEMICAL_FURNACE_BLOCK = register(AlchemyMod.getId("alchemical_furnace"), new AlchemicalFurnaceBlock(FabricBlockSettings.copyOf(Blocks.STONE)));
		CHILD_BLOCK = register(AlchemyMod.getId("child_block"), new ChildBlock(FabricBlockSettings.copyOf(Blocks.GLASS)));
		CRUCIBLE_BLOCK = register(AlchemyMod.getId("brewing_crucible"), new CrucibleBlock(FabricBlockSettings.copyOf(BREWING_CAULDRON_BLOCK)));
		JAR_BLOCK = register(AlchemyMod.getId("jar"), new JarBlock(FabricBlockSettings.of(Material.GLASS).nonOpaque().breakByHand(true)));
		PHIAL_SHELF_BLOCK = register(AlchemyMod.getId("phial_shelf"), new PhialShelfBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS).nonOpaque()));
		ASPECT_PIPE_BLOCK = register(AlchemyMod.getId("aspect_pipe"), new PipeBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()));
		SPAWNER_FRAME = register(AlchemyMod.getId("spawner_frame"), new Block(FabricBlockSettings.copyOf(Blocks.SPAWNER)));
		CUSTOM_SPAWNER = register(AlchemyMod.getId("custom_spawner"), new CustomSpawnerBlock(FabricBlockSettings.copyOf(Blocks.SPAWNER)));
		NEKOMANCY_TABLE = register(AlchemyMod.getId("nekomancy_table"), new NekomancyBlock(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE)));
		CRAFTING_HOPPER = register(AlchemyMod.getId("crafting_hopper"), new CraftingHopper(FabricBlockSettings.copyOf(Blocks.HOPPER)));
		VIS_LIQUIFIER = register(AlchemyMod.getId("vis_liquifier"), new VisLiquifierBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));
		ASPECT_CRAFTING_BLOCK = register(AlchemyMod.getId("aspect_crafting_block"), new AspectCraftingBlock(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE)));
		POTION_SPRAYER_BLOCK = register(AlchemyMod.getId("potion_sprayer"), new PotionSprayerBlock(FabricBlockSettings.copyOf(Blocks.COBBLESTONE)));
		ESSENTIA_SMELTERY = register(AlchemyMod.getId("essentia_smeltery"), new EssentiaSmeltery(FabricBlockSettings.copyOf(Blocks.FURNACE)));
		ESSENTIA_REFINERY_BLOCK = register(AlchemyMod.getId("essentia_refinery"), new EssentiaRefineryBlock(FabricBlockSettings.copyOf(Blocks.COBBLESTONE)));
		ARCANE_LAMP_BLOCK = register(AlchemyMod.getId("arcane_lamp"), new ArcaneLampBlock(FabricBlockSettings.copyOf(Blocks.LANTERN)));
		ARCANE_LIGHT_BLOCK = register(AlchemyMod.getId("arcane_light"), new ArcaneLightBlock(FabricBlockSettings.of(Material.AIR).nonOpaque().collidable(false).dropsNothing().noCollision().luminance(15)));
		DIMENSIONAL_FLUID_BLOCK = register(AlchemyMod.getId("dimensional_fluid"), new CustomFluidBlock(AlchemyFluids.DIMENSIONAL_STILL, FabricBlockSettings.copyOf(Blocks.WATER)));
		DEEPSLATE_GRASS = register(AlchemyMod.getId("deepslate_grass"), new Block(FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK)));
		GLOWING_DEEPSLATE = register(AlchemyMod.getId("glowing_deepslate"), new Block(FabricBlockSettings.copyOf(Blocks.CHISELED_DEEPSLATE).luminance(15)));
		SILVERWOOD_LEAVES = register(AlchemyMod.getId("silverwood_leaves"), new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)));
		SILVERWOOD_LOG = register(AlchemyMod.getId("silverwood_log"), new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG)));
		PORTABLE_HOLE_BLOCK = register(AlchemyMod.getId("portable_hole"), new PortableHoleBlock(FabricBlockSettings.copyOf(Blocks.AIR).luminance(5)));
		WAND_WORKBENCH = register(AlchemyMod.getId("wand_workbench"), new WandWorkbenchBlock(FabricBlockSettings.of(Material.WOOD)));
	}

	private static <T extends Block> T register(Identifier id, T block)
	{
		return Registry.register(Registry.BLOCK, id, block);
	}
}
