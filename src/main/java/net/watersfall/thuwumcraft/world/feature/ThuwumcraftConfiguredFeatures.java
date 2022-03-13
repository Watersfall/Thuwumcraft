package net.watersfall.thuwumcraft.world.feature;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.RandomSpreadFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.BendingTrunkPlacer;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.aspect.Aspects;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlocks;
import net.watersfall.thuwumcraft.world.config.DecoratedRockConfig;
import net.watersfall.thuwumcraft.world.config.NetherGeodeConfig;
import net.watersfall.thuwumcraft.world.config.NetherGeodeLayersConfig;
import net.watersfall.thuwumcraft.world.config.NetherGeodeSizeConfig;
import net.watersfall.thuwumcraft.world.tree.trunk.SilverwoodTrunkPlacer;

import java.util.ArrayList;

public class ThuwumcraftConfiguredFeatures
{
	public static RegistryEntry<ConfiguredFeature<GeodeFeatureConfig, ?>> EARTH_CRYSTAL_GEODE;
	public static RegistryEntry<ConfiguredFeature<GeodeFeatureConfig, ?>> NETHER_GEODE;
	public static RegistryEntry<ConfiguredFeature<NetherGeodeConfig, ?>> BASALT_DELTA_GEODE;
	public static RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> MAGIC_FOREST_TREES;
	public static RegistryEntry<ConfiguredFeature<DecoratedRockConfig, ?>> MOSSY_ASPECT_ROCKS;
	public static RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> LOST_TREE;
	public static RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> THE_LOST_FOREST_TREES;
	public static RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> SILVERWOOD_TREE;
	public static RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> SILVERWOOD_TREE_SAPLING;

	public static void register()
	{
		EARTH_CRYSTAL_GEODE = ConfiguredFeatures.register(id("earth_crystal_geode"), Feature.GEODE, new GeodeFeatureConfig(
				new GeodeLayerConfig(
						SimpleBlockStateProvider.of(Blocks.CAVE_AIR.getDefaultState()),
						SimpleBlockStateProvider.of(Blocks.CALCITE.getDefaultState()),
						SimpleBlockStateProvider.of(Blocks.CALCITE.getDefaultState()),
						SimpleBlockStateProvider.of(Blocks.TUFF.getDefaultState()),
						SimpleBlockStateProvider.of(Blocks.SMOOTH_BASALT.getDefaultState()),
						ImmutableList.of(
								Aspects.ASPECT_TO_CLUSTER.get(Aspects.EARTH).getDefaultState(),
								Aspects.ASPECT_TO_LARGE_CLUSTER.get(Aspects.EARTH).getDefaultState(),
								Aspects.ASPECT_TO_MEDIUM_CLUSTER.get(Aspects.EARTH).getDefaultState(),
								Aspects.ASPECT_TO_SMALL_CLUSTER.get(Aspects.EARTH).getDefaultState()
						),
						BlockTags.FEATURES_CANNOT_REPLACE,
						BlockTags.GEODE_INVALID_BLOCKS
				),
				new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
				new GeodeCrackConfig(0.95D, 2.0D, 2), 0.35D, 0.083D, true, UniformIntProvider.create(4, 6), UniformIntProvider.create(3, 4), UniformIntProvider.create(1, 2), -16, 16, 0.05D, 1)
		);
		NETHER_GEODE = ConfiguredFeatures.register(id("nether_geode"), Feature.GEODE, new GeodeFeatureConfig(
				new GeodeLayerConfig(
						SimpleBlockStateProvider.of(Blocks.CAVE_AIR.getDefaultState()),
						SimpleBlockStateProvider.of(Aspects.ASPECT_TO_CLUSTER_BLOCK.get(Aspects.FIRE).getDefaultState()),
						SimpleBlockStateProvider.of(Aspects.ASPECT_TO_BUDDING_CLUSTER.get(Aspects.FIRE).getDefaultState()),
						SimpleBlockStateProvider.of(Blocks.BASALT.getDefaultState()),
						SimpleBlockStateProvider.of(Blocks.BLACKSTONE.getDefaultState()),
						ImmutableList.of(
								Aspects.ASPECT_TO_CLUSTER.get(Aspects.FIRE).getDefaultState(),
								Aspects.ASPECT_TO_LARGE_CLUSTER.get(Aspects.FIRE).getDefaultState(),
								Aspects.ASPECT_TO_MEDIUM_CLUSTER.get(Aspects.FIRE).getDefaultState(),
								Aspects.ASPECT_TO_SMALL_CLUSTER.get(Aspects.FIRE).getDefaultState()
						),
						BlockTags.FEATURES_CANNOT_REPLACE,
						BlockTags.GEODE_INVALID_BLOCKS
				),
				new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
				new GeodeCrackConfig(0.95D, 2.0D, 2), 0.35D, 0.083D, true, UniformIntProvider.create(4, 6), UniformIntProvider.create(3, 4), UniformIntProvider.create(1, 2), -16, 16, 0.05D, 1)
		);
		BASALT_DELTA_GEODE = ConfiguredFeatures.register(id("basalt_delta_geode"), ThuwumcraftFeatures.NETHER_GEODE_FEATURE, new NetherGeodeConfig(
				new NetherGeodeLayersConfig(
						new GeodeLayerConfig(
								SimpleBlockStateProvider.of(Blocks.CAVE_AIR.getDefaultState()),
								SimpleBlockStateProvider.of(Aspects.ASPECT_TO_CLUSTER_BLOCK.get(Aspects.FIRE).getDefaultState()),
								SimpleBlockStateProvider.of(Aspects.ASPECT_TO_BUDDING_CLUSTER.get(Aspects.FIRE).getDefaultState()),
								SimpleBlockStateProvider.of(Blocks.OBSIDIAN.getDefaultState()),
								SimpleBlockStateProvider.of(Blocks.BLACKSTONE.getDefaultState()),
								ImmutableList.of(
										Aspects.ASPECT_TO_CLUSTER.get(Aspects.FIRE).getDefaultState(),
										Aspects.ASPECT_TO_LARGE_CLUSTER.get(Aspects.FIRE).getDefaultState(),
										Aspects.ASPECT_TO_MEDIUM_CLUSTER.get(Aspects.FIRE).getDefaultState(),
										Aspects.ASPECT_TO_SMALL_CLUSTER.get(Aspects.FIRE).getDefaultState()
								),
								BlockTags.FEATURES_CANNOT_REPLACE,
								BlockTags.GEODE_INVALID_BLOCKS
						),
						SimpleBlockStateProvider.of(Blocks.BLACKSTONE.getDefaultState()),
						16
				),
				new NetherGeodeLayersConfig(
						new GeodeLayerConfig(
								SimpleBlockStateProvider.of(Blocks.CAVE_AIR.getDefaultState()),
								SimpleBlockStateProvider.of(Aspects.ASPECT_TO_CLUSTER_BLOCK.get(Aspects.FIRE).getDefaultState()),
								SimpleBlockStateProvider.of(Aspects.ASPECT_TO_BUDDING_CLUSTER.get(Aspects.FIRE).getDefaultState()),
								SimpleBlockStateProvider.of(Blocks.BLACKSTONE.getDefaultState()),
								SimpleBlockStateProvider.of(Blocks.NETHER_WART_BLOCK.getDefaultState()),
								ImmutableList.of(
										Aspects.ASPECT_TO_CLUSTER.get(Aspects.FIRE).getDefaultState(),
										Aspects.ASPECT_TO_LARGE_CLUSTER.get(Aspects.FIRE).getDefaultState(),
										Aspects.ASPECT_TO_MEDIUM_CLUSTER.get(Aspects.FIRE).getDefaultState(),
										Aspects.ASPECT_TO_SMALL_CLUSTER.get(Aspects.FIRE).getDefaultState()
								),
								BlockTags.FEATURES_CANNOT_REPLACE,
								BlockTags.GEODE_INVALID_BLOCKS
						),
						SimpleBlockStateProvider.of(Blocks.NETHERRACK.getDefaultState()),
						1
				),
				new NetherGeodeLayersConfig(
						new GeodeLayerConfig(
								SimpleBlockStateProvider.of(Blocks.CAVE_AIR.getDefaultState()),
								SimpleBlockStateProvider.of(Aspects.ASPECT_TO_CLUSTER_BLOCK.get(Aspects.FIRE).getDefaultState()),
								SimpleBlockStateProvider.of(Aspects.ASPECT_TO_BUDDING_CLUSTER.get(Aspects.FIRE).getDefaultState()),
								SimpleBlockStateProvider.of(Blocks.BASALT.getDefaultState()),
								SimpleBlockStateProvider.of(Blocks.MAGMA_BLOCK.getDefaultState()),
								ImmutableList.of(
										Aspects.ASPECT_TO_CLUSTER.get(Aspects.FIRE).getDefaultState(),
										Aspects.ASPECT_TO_LARGE_CLUSTER.get(Aspects.FIRE).getDefaultState(),
										Aspects.ASPECT_TO_MEDIUM_CLUSTER.get(Aspects.FIRE).getDefaultState(),
										Aspects.ASPECT_TO_SMALL_CLUSTER.get(Aspects.FIRE).getDefaultState()
								),
								BlockTags.FEATURES_CANNOT_REPLACE,
								BlockTags.GEODE_INVALID_BLOCKS
						),
						SimpleBlockStateProvider.of(Blocks.LAVA.getDefaultState()),
						16
				),
				new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
				new GeodeCrackConfig(0.95D, 2.0D, 2),
				new NetherGeodeSizeConfig(4, 7, 3, 5, 1, 3, -16, 16),
				0.35D, 0.083D, true, 0.05D, 1
		));
		SILVERWOOD_TREE = ConfiguredFeatures.register(id("silverwood_tree"), Feature.TREE, new OpenTreeFeatureConfig(
				SimpleBlockStateProvider.of(ThuwumcraftBlocks.SILVERWOOD_LOG.getDefaultState()),
				new SilverwoodTrunkPlacer(7, 2, 2),
				SimpleBlockStateProvider.of(ThuwumcraftBlocks.SILVERWOOD_LEAVES.getDefaultState()),
				new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 3), UniformIntProvider.create(0, 0), UniformIntProvider.create(2, 2), 100),
				SimpleBlockStateProvider.of(ThuwumcraftBlocks.DEEPSLATE_GRASS.getDefaultState()),
				new TwoLayersFeatureSize(1, 0, 1),
				new ArrayList<>(),
				false,
				false
		));
		SILVERWOOD_TREE_SAPLING = ConfiguredFeatures.register(id("silverwood_tree_sapling"), Feature.TREE, new OpenTreeFeatureConfig(
				SimpleBlockStateProvider.of(ThuwumcraftBlocks.SILVERWOOD_LOG.getDefaultState()),
				new SilverwoodTrunkPlacer(7, 2, 2),
				SimpleBlockStateProvider.of(ThuwumcraftBlocks.SILVERWOOD_LEAVES.getDefaultState()),
				new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 3), UniformIntProvider.create(0, 0), UniformIntProvider.create(2, 2), 100),
				SimpleBlockStateProvider.of(ThuwumcraftBlocks.DEEPSLATE_GRASS.getDefaultState()),
				new TwoLayersFeatureSize(1, 0, 1),
				new ArrayList<>(),
				false,
				false
		));
		MAGIC_FOREST_TREES = ConfiguredFeatures.register(id("magic_forest_trees"), Feature.RANDOM_SELECTOR, new RandomFeatureConfig(
						ImmutableList.of(
								new RandomFeatureEntry(PlacedFeatures.createEntry(TreeConfiguredFeatures.HUGE_RED_MUSHROOM), 0.03F),
								new RandomFeatureEntry(PlacedFeatures.createEntry(TreeConfiguredFeatures.HUGE_BROWN_MUSHROOM), 0.025F),
								new RandomFeatureEntry(PlacedFeatures.createEntry(SILVERWOOD_TREE, PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP), 0.015f)
						),
						TreePlacedFeatures.OAK_CHECKED
				));
		MOSSY_ASPECT_ROCKS = ConfiguredFeatures.register(id("mossy_aspect_rocks"), ThuwumcraftFeatures.DECORATED_ROCK_FEATURE, new DecoratedRockConfig(
				SimpleBlockStateProvider.of(Blocks.MOSSY_COBBLESTONE.getDefaultState())
		));
		LOST_TREE = ConfiguredFeatures.register(id("lost_tree"), Feature.TREE, new OpenTreeFeatureConfig(
				SimpleBlockStateProvider.of(Blocks.OAK_LOG.getDefaultState()),
				new BendingTrunkPlacer(4, 2, 0, 3, UniformIntProvider.create(1, 1)),
				SimpleBlockStateProvider.of(Blocks.OAK_LEAVES.getDefaultState()),
				new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 3), UniformIntProvider.create(0, 0), UniformIntProvider.create(2, 2), 50),
				SimpleBlockStateProvider.of(ThuwumcraftBlocks.DEEPSLATE_GRASS.getDefaultState()),
				new TwoLayersFeatureSize(1, 0, 1),
				new ArrayList<>(),
				false,
				false
		));
		THE_LOST_FOREST_TREES = ConfiguredFeatures.register(id("the_lost_forest_trees"), Feature.RANDOM_SELECTOR,
				new RandomFeatureConfig(
						ImmutableList.of(
								new RandomFeatureEntry(PlacedFeatures.createEntry(TreeConfiguredFeatures.HUGE_RED_MUSHROOM), 0.05f),
								new RandomFeatureEntry(PlacedFeatures.createEntry(TreeConfiguredFeatures.HUGE_BROWN_MUSHROOM), 0.05f)
						),
						PlacedFeatures.createEntry(LOST_TREE, PlacedFeatures.wouldSurvive(Blocks.DARK_OAK_SAPLING))
				)
		);
	}

	private static String id(String id)
	{
		return Thuwumcraft.MOD_ID + ":" + id;
	}
}
