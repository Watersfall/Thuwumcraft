package net.watersfall.thuwumcraft.world.feature;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;
import net.watersfall.thuwumcraft.Thuwumcraft;

public class ThuwumcraftPlacedFeatures
{
	public static RegistryEntry<PlacedFeature> EARTH_CRYSTAL_GEODE_PLACED;
	public static RegistryEntry<PlacedFeature> NETHER_GEODE_PLACED;
	public static RegistryEntry<PlacedFeature> BASALT_DELTA_GEODE_PLACED;
	public static RegistryEntry<PlacedFeature> SILVERWOOD_TREE_PLACED;
	public static RegistryEntry<PlacedFeature> MOSSY_ASPECT_ROCKS_PLACED;
	public static RegistryEntry<PlacedFeature> MAGIC_FOREST_TREES_PLACED;
	public static RegistryEntry<PlacedFeature> THE_LOST_FOREST_TREES_PLACED;

	public static void register()
	{
		EARTH_CRYSTAL_GEODE_PLACED = PlacedFeatures.register(id("earth_crystal_geode"), ThuwumcraftConfiguredFeatures.EARTH_CRYSTAL_GEODE,
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(6), YOffset.fixed(46)),
				RandomOffsetPlacementModifier.horizontally(UniformIntProvider.create(0, 16)),
				RarityFilterPlacementModifier.of(45)
		);
		NETHER_GEODE_PLACED = PlacedFeatures.register(id("nether_geode"), ThuwumcraftConfiguredFeatures.NETHER_GEODE,
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(6), YOffset.fixed(46)),
				RandomOffsetPlacementModifier.horizontally(UniformIntProvider.create(0, 16)),
				RarityFilterPlacementModifier.of(45)
		);
		BASALT_DELTA_GEODE_PLACED = PlacedFeatures.register(id("basalt_delta_geode"), ThuwumcraftConfiguredFeatures.BASALT_DELTA_GEODE,
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(6), YOffset.fixed(46)),
				RandomOffsetPlacementModifier.horizontally(UniformIntProvider.create(0, 16)),
				RarityFilterPlacementModifier.of(45)
		);
		SILVERWOOD_TREE_PLACED = PlacedFeatures.register(id("silverwood_tree"), ThuwumcraftConfiguredFeatures.SILVERWOOD_TREE, PlacedFeatures.createCountExtraModifier(0, 0.05f, 1), SquarePlacementModifier.of(), VegetationPlacedFeatures.NOT_IN_SURFACE_WATER_MODIFIER, PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.getDefaultState(), BlockPos.ORIGIN)), BiomePlacementModifier.of());
		MAGIC_FOREST_TREES_PLACED = PlacedFeatures.register(id("magic_forest_trees"), ThuwumcraftConfiguredFeatures.MAGIC_FOREST_TREES, PlacedFeatures.createCountExtraModifier(10, 0.1f, 1), SquarePlacementModifier.of(), VegetationPlacedFeatures.NOT_IN_SURFACE_WATER_MODIFIER, PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, BiomePlacementModifier.of());
		MOSSY_ASPECT_ROCKS_PLACED = PlacedFeatures.register(id("mossy_aspect_rocks"), ThuwumcraftConfiguredFeatures.MOSSY_ASPECT_ROCKS, CountPlacementModifier.of(2), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of());
		THE_LOST_FOREST_TREES_PLACED = PlacedFeatures.register(id("the_lost_forest_trees"), ThuwumcraftConfiguredFeatures.THE_LOST_FOREST_TREES, PlacedFeatures.createCountExtraModifier(10, 0.1f, 1), SquarePlacementModifier.of(), VegetationPlacedFeatures.NOT_IN_SURFACE_WATER_MODIFIER, PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, BiomePlacementModifier.of());
	}

	private static String id(String id)
	{
		return Thuwumcraft.MOD_ID + ":" + id;
	}
}
