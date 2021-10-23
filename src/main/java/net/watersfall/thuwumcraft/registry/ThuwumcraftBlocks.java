package net.watersfall.thuwumcraft.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.block.*;
import net.watersfall.thuwumcraft.block.sapling.OpenSaplingBlock;
import net.watersfall.thuwumcraft.block.sapling.SilverwoodSaplingGenerator;

public class ThuwumcraftBlocks
{
	public static PillarBlock GREATWOOD_LOG;
	public static PillarBlock STRIPPED_GREATWOOD_LOG;
	public static PillarBlock GREATWOOD_WOOD;
	public static PillarBlock STRIPPED_GREATWOOD_WOOD;
	public static Block GREATWOOD_PLANKS;
	public static SlabBlock GREATWOOD_SLAB;
	public static StairsBlock GREATWOOD_STAIRS;
	public static LeavesBlock GREATWOOD_LEAVES;
	public static PillarBlock SILVERWOOD_LOG;
	public static PillarBlock STRIPPED_SILVERWOOD_LOG;
	public static PillarBlock SILVERWOOD_WOOD;
	public static PillarBlock STRIPPED_SILVERWOOD_WOOD;
	public static Block SILVERWOOD_PLANKS;
	public static SlabBlock SILVERWOOD_SLAB;
	public static StairsBlock SILVERWOOD_STAIRS;
	public static SilverwoodLeavesBlock SILVERWOOD_LEAVES;
	public static Block ARCANE_STONE;
	public static SlabBlock ARCANE_STONE_SLAB;
	public static StairsBlock ARCANE_STONE_STAIRS;
	public static BrewingCauldronBlock BREWING_CAULDRON;
	public static PedestalBlock PEDESTAL;
	public static AlchemicalFurnaceBlock ALCHEMICAL_FURNACE;
	public static ChildBlock CHILD_BLOCK;
	public static CrucibleBlock CRUCIBLE;
	public static JarBlock JAR;
	public static PhialShelfBlock PHIAL_SHELF;
	public static PipeBlock BRASS_PIPE;
	public static Block SPAWNER_FRAME;
	public static CustomSpawnerBlock CUSTOM_SPAWNER;
	public static NekomancyBlock NEKOMANCY_TABLE;
	public static CraftingHopper CRAFTING_HOPPER;
	public static VisLiquifierBlock VIS_LIQUIFIER;
	public static AspectCraftingBlock ASPECT_CRAFTING_TABLE;
	public static PotionSprayerBlock POTION_SPRAYER;
	public static EssentiaSmeltery ESSENTIA_SMELTERY;
	public static EssentiaRefineryBlock ESSENTIA_REFINERY;
	public static ArcaneLampBlock ARCANE_LAMP;
	public static ArcaneLightBlock ARCANE_LIGHT;
	public static FluidBlock DIMENSIONAL_FLUID;
	public static Block DEEPSLATE_GRASS;
	public static Block GLOWING_DEEPSLATE;
	public static PortableHoleBlock PORTABLE_HOLE;
	public static WandWorkbenchBlock WAND_WORKBENCH;
	public static SaplingBlock SILVERWOOD_SAPLING;
	public static ArcaneSealBlock ARCANE_SEAL;
	public static ThaumatoriumBlock THAUMATORIUM;
	public static HungryChestBlock HUNGRY_CHEST;

	public static void register()
	{
		GREATWOOD_LOG = register("greatwood_log", new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG)));
		STRIPPED_GREATWOOD_LOG = register("stripped_greatwood_log", new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_LOG)));
		GREATWOOD_WOOD = register("greatwood_wood", new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)));
		STRIPPED_GREATWOOD_WOOD = register("stripped_greatwood_wood", new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_WOOD)));
		GREATWOOD_PLANKS = register("greatwood_planks", new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)));
		GREATWOOD_SLAB = registerSlab("greatwood_planks_slab", GREATWOOD_PLANKS);
		GREATWOOD_STAIRS = registerStairs("greatwood_planks_stairs", GREATWOOD_PLANKS);
		GREATWOOD_LEAVES = register("greatwood_leaves", new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)));
		SILVERWOOD_LOG = register("silverwood_log", new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG)));
		STRIPPED_SILVERWOOD_LOG = register("stripped_silverwood_log", new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_LOG)));
		SILVERWOOD_WOOD = register("silverwood_wood", new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)));
		STRIPPED_SILVERWOOD_WOOD = register("stripped_silverwood_wood", new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_WOOD)));
		SILVERWOOD_PLANKS = register("silverwood_planks", new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)));
		SILVERWOOD_SLAB = registerSlab("silverwood_planks_slab", SILVERWOOD_PLANKS);
		SILVERWOOD_STAIRS = registerStairs("silverwood_planks_stairs", SILVERWOOD_PLANKS);
		SILVERWOOD_LEAVES = register("silverwood_leaves", new SilverwoodLeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)));
		ARCANE_STONE = register("arcane_stone", new Block(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).requiresTool()));
		ARCANE_STONE_SLAB = register("arcane_stone_slab", new SlabBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).requiresTool()));
		ARCANE_STONE_STAIRS = registerStairs("arcane_stone_stairs", ARCANE_STONE);
		BREWING_CAULDRON = register("brewing_cauldron", new BrewingCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON).ticksRandomly()));
		PEDESTAL = register("pedestal", new PedestalBlock(FabricBlockSettings.copyOf(Blocks.STONE).luminance(7).nonOpaque()));
		ALCHEMICAL_FURNACE = register("alchemical_furnace", new AlchemicalFurnaceBlock(FabricBlockSettings.copyOf(Blocks.STONE)));
		CHILD_BLOCK = register("child_block", new ChildBlock(FabricBlockSettings.copyOf(Blocks.GLASS)));
		CRUCIBLE = register("brewing_crucible", new CrucibleBlock(FabricBlockSettings.copyOf(BREWING_CAULDRON)));
		JAR = register("jar", new JarBlock(FabricBlockSettings.of(Material.GLASS).nonOpaque().breakByHand(true)));
		PHIAL_SHELF = register("phial_shelf", new PhialShelfBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS).nonOpaque()));
		BRASS_PIPE = register("brass_pipe", new PipeBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()));
		SPAWNER_FRAME = register("spawner_frame", new Block(FabricBlockSettings.copyOf(Blocks.SPAWNER)));
		CUSTOM_SPAWNER = register("custom_spawner", new CustomSpawnerBlock(FabricBlockSettings.copyOf(Blocks.SPAWNER)));
		NEKOMANCY_TABLE = register("nekomancy_table", new NekomancyBlock(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE)));
		CRAFTING_HOPPER = register("crafting_hopper", new CraftingHopper(FabricBlockSettings.copyOf(Blocks.HOPPER)));
		VIS_LIQUIFIER = register("vis_liquifier", new VisLiquifierBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));
		ASPECT_CRAFTING_TABLE = register("aspect_crafting_block", new AspectCraftingBlock(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE)));
		POTION_SPRAYER = register("potion_sprayer", new PotionSprayerBlock(FabricBlockSettings.copyOf(Blocks.COBBLESTONE)));
		ESSENTIA_SMELTERY = register("essentia_smeltery", new EssentiaSmeltery(FabricBlockSettings.copyOf(Blocks.FURNACE)));
		ESSENTIA_REFINERY = register("essentia_refinery", new EssentiaRefineryBlock(FabricBlockSettings.copyOf(Blocks.COBBLESTONE)));
		ARCANE_LAMP = register("arcane_lamp", new ArcaneLampBlock(FabricBlockSettings.copyOf(Blocks.LANTERN)));
		ARCANE_LIGHT = register("arcane_light", new ArcaneLightBlock(FabricBlockSettings.of(Material.AIR).nonOpaque().collidable(false).dropsNothing().noCollision().luminance(15)));
		DIMENSIONAL_FLUID = register("dimensional_fluid", new CustomFluidBlock(ThuwumcraftFluids.DIMENSIONAL_STILL, FabricBlockSettings.copyOf(Blocks.WATER)));
		DEEPSLATE_GRASS = register("deepslate_grass", new Block(FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK)));
		GLOWING_DEEPSLATE = register("glowing_deepslate", new Block(FabricBlockSettings.copyOf(Blocks.CHISELED_DEEPSLATE).luminance(15)));
		PORTABLE_HOLE = register("portable_hole", new PortableHoleBlock(FabricBlockSettings.copyOf(Blocks.AIR).luminance(5)));
		WAND_WORKBENCH = register("wand_workbench", new WandWorkbenchBlock(FabricBlockSettings.of(Material.WOOD)));
		SILVERWOOD_SAPLING = register("silverwood_sapling", new OpenSaplingBlock(new SilverwoodSaplingGenerator(), FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)));
		ARCANE_SEAL = register("arcane_seal", new ArcaneSealBlock(FabricBlockSettings.of(Material.STONE).breakInstantly().nonOpaque().emissiveLighting(ThuwumcraftBlocks::always).luminance(5)));
		THAUMATORIUM = register("thaumatorium", new ThaumatoriumBlock(FabricBlockSettings.of(Material.METAL)));
		HUNGRY_CHEST = register("hungry_chest", new HungryChestBlock(FabricBlockSettings.copyOf(Blocks.CHEST)));
	}

	private static StairsBlock registerStairs(String id, Block copy)
	{
		return register(id, new OpenStairsBlock(copy.getDefaultState(), FabricBlockSettings.copyOf(copy)));
	}

	private static SlabBlock registerSlab(String id, Block copy)
	{
		return register(id, new SlabBlock(FabricBlockSettings.copyOf(copy)));
	}

	private static <T extends Block> T register(String id, T block)
	{
		return Registry.register(Registry.BLOCK, Thuwumcraft.getId(id), block);
	}

	public static boolean always(BlockState state, BlockView world, BlockPos pos)
	{
		return true;
	}
}
