package net.watersfall.thuwumcraft.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.block.*;
import net.watersfall.thuwumcraft.block.sapling.OpenSaplingBlock;
import net.watersfall.thuwumcraft.block.sapling.SilverwoodSaplingGenerator;

public class ThuwumcraftBlocks
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
	public static final SaplingBlock SILVERWOOD_SAPLING;
	public static final Block ARCANE_STONE;
	public static final SlabBlock ARCANE_STONE_SLAB;
	public static final ArcaneSealBlock ARCANE_SEAL;
	public static final ThaumatoriumBlock THAUMATORIUM;
	public static final PillarBlock GREATWOOD_LOG;

	static
	{
		BREWING_CAULDRON_BLOCK = register(Thuwumcraft.getId("brewing_cauldron"), new BrewingCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON).ticksRandomly()));
		PEDESTAL_BLOCK = register(Thuwumcraft.getId("pedestal"), new PedestalBlock(FabricBlockSettings.copyOf(Blocks.STONE).luminance(7).nonOpaque()));
		ALCHEMICAL_FURNACE_BLOCK = register(Thuwumcraft.getId("alchemical_furnace"), new AlchemicalFurnaceBlock(FabricBlockSettings.copyOf(Blocks.STONE)));
		CHILD_BLOCK = register(Thuwumcraft.getId("child_block"), new ChildBlock(FabricBlockSettings.copyOf(Blocks.GLASS)));
		CRUCIBLE_BLOCK = register(Thuwumcraft.getId("brewing_crucible"), new CrucibleBlock(FabricBlockSettings.copyOf(BREWING_CAULDRON_BLOCK)));
		JAR_BLOCK = register(Thuwumcraft.getId("jar"), new JarBlock(FabricBlockSettings.of(Material.GLASS).nonOpaque().breakByHand(true)));
		PHIAL_SHELF_BLOCK = register(Thuwumcraft.getId("phial_shelf"), new PhialShelfBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS).nonOpaque()));
		ASPECT_PIPE_BLOCK = register(Thuwumcraft.getId("aspect_pipe"), new PipeBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()));
		SPAWNER_FRAME = register(Thuwumcraft.getId("spawner_frame"), new Block(FabricBlockSettings.copyOf(Blocks.SPAWNER)));
		CUSTOM_SPAWNER = register(Thuwumcraft.getId("custom_spawner"), new CustomSpawnerBlock(FabricBlockSettings.copyOf(Blocks.SPAWNER)));
		NEKOMANCY_TABLE = register(Thuwumcraft.getId("nekomancy_table"), new NekomancyBlock(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE)));
		CRAFTING_HOPPER = register(Thuwumcraft.getId("crafting_hopper"), new CraftingHopper(FabricBlockSettings.copyOf(Blocks.HOPPER)));
		VIS_LIQUIFIER = register(Thuwumcraft.getId("vis_liquifier"), new VisLiquifierBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));
		ASPECT_CRAFTING_BLOCK = register(Thuwumcraft.getId("aspect_crafting_block"), new AspectCraftingBlock(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE)));
		POTION_SPRAYER_BLOCK = register(Thuwumcraft.getId("potion_sprayer"), new PotionSprayerBlock(FabricBlockSettings.copyOf(Blocks.COBBLESTONE)));
		ESSENTIA_SMELTERY = register(Thuwumcraft.getId("essentia_smeltery"), new EssentiaSmeltery(FabricBlockSettings.copyOf(Blocks.FURNACE)));
		ESSENTIA_REFINERY_BLOCK = register(Thuwumcraft.getId("essentia_refinery"), new EssentiaRefineryBlock(FabricBlockSettings.copyOf(Blocks.COBBLESTONE)));
		ARCANE_LAMP_BLOCK = register(Thuwumcraft.getId("arcane_lamp"), new ArcaneLampBlock(FabricBlockSettings.copyOf(Blocks.LANTERN)));
		ARCANE_LIGHT_BLOCK = register(Thuwumcraft.getId("arcane_light"), new ArcaneLightBlock(FabricBlockSettings.of(Material.AIR).nonOpaque().collidable(false).dropsNothing().noCollision().luminance(15)));
		DIMENSIONAL_FLUID_BLOCK = register(Thuwumcraft.getId("dimensional_fluid"), new CustomFluidBlock(ThuwumcraftFluids.DIMENSIONAL_STILL, FabricBlockSettings.copyOf(Blocks.WATER)));
		DEEPSLATE_GRASS = register(Thuwumcraft.getId("deepslate_grass"), new Block(FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK)));
		GLOWING_DEEPSLATE = register(Thuwumcraft.getId("glowing_deepslate"), new Block(FabricBlockSettings.copyOf(Blocks.CHISELED_DEEPSLATE).luminance(15)));
		SILVERWOOD_LEAVES = register(Thuwumcraft.getId("silverwood_leaves"), new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)));
		SILVERWOOD_LOG = register(Thuwumcraft.getId("silverwood_log"), new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG)));
		PORTABLE_HOLE_BLOCK = register(Thuwumcraft.getId("portable_hole"), new PortableHoleBlock(FabricBlockSettings.copyOf(Blocks.AIR).luminance(5)));
		WAND_WORKBENCH = register(Thuwumcraft.getId("wand_workbench"), new WandWorkbenchBlock(FabricBlockSettings.of(Material.WOOD)));
		SILVERWOOD_SAPLING = register(Thuwumcraft.getId("silverwood_sapling"), new OpenSaplingBlock(new SilverwoodSaplingGenerator(), FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)));
		ARCANE_STONE = register(Thuwumcraft.getId("arcane_stone"), new Block(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).requiresTool()));
		ARCANE_STONE_SLAB = register(Thuwumcraft.getId("arcane_stone_slab"), new SlabBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).requiresTool()));
		ARCANE_SEAL = register(Thuwumcraft.getId("arcane_seal"), new ArcaneSealBlock(FabricBlockSettings.of(Material.STONE).breakInstantly().nonOpaque().emissiveLighting(ThuwumcraftBlocks::always).luminance(5)));
		THAUMATORIUM = register("thaumatorium", new ThaumatoriumBlock(FabricBlockSettings.of(Material.METAL)));
		GREATWOOD_LOG = register("greatwood_log", new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG)));
	}

	private static <T extends Block> T register(Identifier id, T block)
	{
		return Registry.register(Registry.BLOCK, id, block);
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
