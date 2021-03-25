package net.watersfall.alchemy.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;

public class AlchemyBlocks
{
	public static final BrewingCauldronBlock BREWING_CAULDRON_BLOCK;
	public static final PedestalBlock PEDESTAL_BLOCK;
	public static final AlchemicalFurnaceBlock ALCHEMICAL_FURNACE_BLOCK;
	public static final ChildBlock CHILD_BLOCK;
	public static final CrucibleBlock CRUCIBLE_BLOCK;
	public static final JarBlock JAR_BLOCK;
	public static final PhialShelfBlock PHIAL_SHELF_BLOCK;
	public static final AspectPipeBlock ASPECT_PIPE_BLOCK;
	public static final AmethystClusterBlock FIRE_CRYSTAL_CLUSTER;
	public static final AmethystClusterBlock FIRE_CRYSTAL_SMALL;
	public static final AmethystClusterBlock FIRE_CRYSTAL_MEDIUM;
	public static final AmethystClusterBlock FIRE_CRYSTAL_LARGE;
	public static final AmethystClusterBlock WATER_CRYSTAL_CLUSTER;
	public static final AmethystClusterBlock WATER_CRYSTAL_SMALL;
	public static final AmethystClusterBlock WATER_CRYSTAL_MEDIUM;
	public static final AmethystClusterBlock WATER_CRYSTAL_LARGE;
	public static final AmethystBlock EARTH_CRYSTAL_BLOCK;
	public static final BuddingAmethystBlock BUDDING_EARTH_CRYSTAL_BLOCK;
	public static final AmethystClusterBlock EARTH_CRYSTAL_CLUSTER;
	public static final AmethystClusterBlock EARTH_CRYSTAL_SMALL;
	public static final AmethystClusterBlock EARTH_CRYSTAL_MEDIUM;
	public static final AmethystClusterBlock EARTH_CRYSTAL_LARGE;

	static
	{
		BREWING_CAULDRON_BLOCK = new BrewingCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON).ticksRandomly());
		PEDESTAL_BLOCK = new PedestalBlock(FabricBlockSettings.copyOf(Blocks.STONE).luminance(7).nonOpaque());
		ALCHEMICAL_FURNACE_BLOCK = new AlchemicalFurnaceBlock(FabricBlockSettings.copyOf(Blocks.STONE));
		CHILD_BLOCK = new ChildBlock(FabricBlockSettings.copyOf(Blocks.GLASS));
		CRUCIBLE_BLOCK = new CrucibleBlock(FabricBlockSettings.copyOf(BREWING_CAULDRON_BLOCK));
		JAR_BLOCK = new JarBlock(FabricBlockSettings.of(Material.GLASS).nonOpaque().breakByHand(true));
		PHIAL_SHELF_BLOCK = new PhialShelfBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS).nonOpaque());
		ASPECT_PIPE_BLOCK = new AspectPipeBlock(FabricBlockSettings.of(Material.METAL).nonOpaque());
		FIRE_CRYSTAL_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.copyOf(Blocks.AMETHYST_CLUSTER));
		FIRE_CRYSTAL_LARGE = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(Blocks.LARGE_AMETHYST_BUD));
		FIRE_CRYSTAL_MEDIUM = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(Blocks.MEDIUM_AMETHYST_BUD));
		FIRE_CRYSTAL_SMALL = new AmethystClusterBlock(3, 3, FabricBlockSettings.copyOf(Blocks.SMALL_AMETHYST_BUD));
		WATER_CRYSTAL_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.copyOf(Blocks.AMETHYST_CLUSTER));
		WATER_CRYSTAL_LARGE = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(Blocks.LARGE_AMETHYST_BUD));
		WATER_CRYSTAL_MEDIUM = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(Blocks.MEDIUM_AMETHYST_BUD));
		WATER_CRYSTAL_SMALL = new AmethystClusterBlock(3, 3, FabricBlockSettings.copyOf(Blocks.SMALL_AMETHYST_BUD));
		EARTH_CRYSTAL_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.copyOf(Blocks.AMETHYST_CLUSTER));
		EARTH_CRYSTAL_LARGE = new AmethystClusterBlock(5, 3, FabricBlockSettings.copyOf(Blocks.LARGE_AMETHYST_BUD));
		EARTH_CRYSTAL_MEDIUM = new AmethystClusterBlock(4, 3, FabricBlockSettings.copyOf(Blocks.MEDIUM_AMETHYST_BUD));
		EARTH_CRYSTAL_SMALL = new AmethystClusterBlock(3, 3, FabricBlockSettings.copyOf(Blocks.SMALL_AMETHYST_BUD));
		EARTH_CRYSTAL_BLOCK = new AmethystBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK));
		BUDDING_EARTH_CRYSTAL_BLOCK = new BuddingAmethystBlock(FabricBlockSettings.copyOf(Blocks.BUDDING_AMETHYST));
	}

	public static void register()
	{
		Registry.register(Registry.BLOCK, AlchemyMod.getId("brewing_cauldron"), AlchemyBlocks.BREWING_CAULDRON_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("pedestal"), AlchemyBlocks.PEDESTAL_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("alchemical_furnace"), AlchemyBlocks.ALCHEMICAL_FURNACE_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("child_block"), AlchemyBlocks.CHILD_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("brewing_crucible"), CRUCIBLE_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("jar"), JAR_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("phial_shelf"), PHIAL_SHELF_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("aspect_pipe"), ASPECT_PIPE_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("fire_crystal_cluster"), FIRE_CRYSTAL_CLUSTER);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("fire_crystal_large"), FIRE_CRYSTAL_LARGE);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("fire_crystal_medium"), FIRE_CRYSTAL_MEDIUM);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("fire_crystal_small"), FIRE_CRYSTAL_SMALL);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("water_crystal_cluster"), WATER_CRYSTAL_CLUSTER);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("water_crystal_large"), WATER_CRYSTAL_LARGE);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("water_crystal_medium"), WATER_CRYSTAL_MEDIUM);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("water_crystal_small"), WATER_CRYSTAL_SMALL);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("earth_crystal_cluster"), EARTH_CRYSTAL_CLUSTER);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("earth_crystal_large"), EARTH_CRYSTAL_LARGE);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("earth_crystal_medium"), EARTH_CRYSTAL_MEDIUM);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("earth_crystal_small"), EARTH_CRYSTAL_SMALL);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("earth_crystal_block"), EARTH_CRYSTAL_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("earth_crystal_budding"), BUDDING_EARTH_CRYSTAL_BLOCK);
	}

	private static <T extends Block> T register(T block, Identifier id)
	{
		return Registry.register(Registry.BLOCK, id, block);
	}
}
