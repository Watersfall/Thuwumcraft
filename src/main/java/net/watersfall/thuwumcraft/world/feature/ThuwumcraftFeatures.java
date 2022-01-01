package net.watersfall.thuwumcraft.world.feature;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.decorator.*;
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

import static net.minecraft.world.gen.feature.VegetationPlacedFeatures.NOT_IN_SURFACE_WATER_MODIFIER;

public class ThuwumcraftFeatures
{
	public static final NetherGeodeFeature NETHER_GEODE_FEATURE;
	public static final DecoratedRockFeature DECORATED_ROCK_FEATURE;

	public static final ConfiguredFeature<?, ?> EARTH_CRYSTAL_GEODE;
	public static final ConfiguredFeature<?, ?> NETHER_GEODE;
	public static final ConfiguredFeature<?, ?> BASALT_DELTA_GEODE;
	public static final ConfiguredFeature<?, ?> MAGIC_FOREST_TREES;
	public static final ConfiguredFeature<?, ?> MOSSY_ASPECT_ROCKS;
	public static final ConfiguredFeature<?, ?> LOST_TREE;
	public static final ConfiguredFeature<?, ?> THE_LOST_FOREST_TREES;
	public static final ConfiguredFeature<?, ?> SILVERWOOD_TREE;
	public static final ConfiguredFeature<?, ?> SILVERWOOD_TREE_SAPLING;
	public static final PlacedFeature EARTH_CRYSTAL_GEODE_PLACED;
	public static final PlacedFeature NETHER_GEODE_PLACED;
	public static final PlacedFeature BASALT_DELTA_GEODE_PLACED;
	public static final PlacedFeature SILVERWOOD_TREE_PLACED;
	public static final PlacedFeature MOSSY_ASPECT_ROCKS_PLACED;
	public static final	PlacedFeature MAGIC_FOREST_TREES_PLACED;

	static
	{
		NETHER_GEODE_FEATURE = Registry.register(Registry.FEATURE, Thuwumcraft.getId("nether_geode"), new NetherGeodeFeature(NetherGeodeConfig.CODEC));
		DECORATED_ROCK_FEATURE = Registry.register(Registry.FEATURE, Thuwumcraft.getId("decorated_rock"), new DecoratedRockFeature(DecoratedRockConfig.CODEC));
		EARTH_CRYSTAL_GEODE = Feature.GEODE.configure(new GeodeFeatureConfig(
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
								BlockTags.FEATURES_CANNOT_REPLACE.getId(),
								BlockTags.GEODE_INVALID_BLOCKS.getId()
						),
						new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
						new GeodeCrackConfig(0.95D, 2.0D, 2), 0.35D, 0.083D, true, UniformIntProvider.create(4, 6), UniformIntProvider.create(3, 4), UniformIntProvider.create(1, 2), -16, 16, 0.05D, 1));
		BASALT_DELTA_GEODE = Feature.GEODE.configure(new GeodeFeatureConfig(
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
						BlockTags.FEATURES_CANNOT_REPLACE.getId(),
						BlockTags.GEODE_INVALID_BLOCKS.getId()
				),
				new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
				new GeodeCrackConfig(0.95D, 2.0D, 2), 0.35D, 0.083D, true, UniformIntProvider.create(4, 6), UniformIntProvider.create(3, 4), UniformIntProvider.create(1, 2), -16, 16, 0.05D, 1));
		NETHER_GEODE = NETHER_GEODE_FEATURE.configure(new NetherGeodeConfig(
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
								BlockTags.FEATURES_CANNOT_REPLACE.getId(),
								BlockTags.GEODE_INVALID_BLOCKS.getId()
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
								BlockTags.FEATURES_CANNOT_REPLACE.getId(),
								BlockTags.GEODE_INVALID_BLOCKS.getId()
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
								BlockTags.FEATURES_CANNOT_REPLACE.getId(),
								BlockTags.GEODE_INVALID_BLOCKS.getId()
						),
						SimpleBlockStateProvider.of(Blocks.LAVA.getDefaultState()),
						16
				),
				new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
				new GeodeCrackConfig(0.95D, 2.0D, 2),
				new NetherGeodeSizeConfig(4, 7, 3, 5, 1, 3, -16, 16),
				0.35D, 0.083D, true, 0.05D, 1
			));
		MOSSY_ASPECT_ROCKS = DECORATED_ROCK_FEATURE.configure(new DecoratedRockConfig(
				SimpleBlockStateProvider.of(Blocks.MOSSY_COBBLESTONE.getDefaultState())
		));
		LOST_TREE = Feature.TREE.configure(new OpenTreeFeatureConfig(
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
		SILVERWOOD_TREE_SAPLING = Feature.TREE.configure(new OpenTreeFeatureConfig(
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
		SILVERWOOD_TREE = Feature.TREE.configure(new OpenTreeFeatureConfig(
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
		THE_LOST_FOREST_TREES = Feature.RANDOM_SELECTOR.configure(
				new RandomFeatureConfig(
						ImmutableList.of(
								new RandomFeatureEntry(TreeConfiguredFeatures.HUGE_RED_MUSHROOM.withPlacement(), 0.05f),
								new RandomFeatureEntry(TreeConfiguredFeatures.HUGE_BROWN_MUSHROOM.withPlacement(), 0.05f)
						),
						LOST_TREE.withWouldSurviveFilter(Blocks.DARK_OAK_SAPLING)
				)
		);
		MAGIC_FOREST_TREES = Feature.RANDOM_SELECTOR.configure(
				new RandomFeatureConfig(
						ImmutableList.of(
								new RandomFeatureEntry(TreeConfiguredFeatures.HUGE_RED_MUSHROOM.withPlacement(), 0.03F),
								new RandomFeatureEntry(TreeConfiguredFeatures.HUGE_BROWN_MUSHROOM.withPlacement(), 0.025F),
								new RandomFeatureEntry(SILVERWOOD_TREE.withPlacement(PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP), 0.015f)
						),
						TreePlacedFeatures.OAK_CHECKED
				));
		EARTH_CRYSTAL_GEODE_PLACED = EARTH_CRYSTAL_GEODE.withPlacement(
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(6), YOffset.fixed(46)),
				RandomOffsetPlacementModifier.horizontally(UniformIntProvider.create(0, 16)),
				RarityFilterPlacementModifier.of(45)
		);
		NETHER_GEODE_PLACED = NETHER_GEODE.withPlacement(
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(6), YOffset.fixed(46)),
				RandomOffsetPlacementModifier.horizontally(UniformIntProvider.create(0, 16)),
				RarityFilterPlacementModifier.of(45)
		);
		BASALT_DELTA_GEODE_PLACED = BASALT_DELTA_GEODE.withPlacement(
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(6), YOffset.fixed(46)),
				RandomOffsetPlacementModifier.horizontally(UniformIntProvider.create(0, 16)),
				RarityFilterPlacementModifier.of(45)
		);
		SILVERWOOD_TREE_PLACED = SILVERWOOD_TREE.withPlacement(PlacedFeatures.createCountExtraModifier(0, 0.05f, 1), SquarePlacementModifier.of(), NOT_IN_SURFACE_WATER_MODIFIER, PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.getDefaultState(), BlockPos.ORIGIN)), BiomePlacementModifier.of());
		MAGIC_FOREST_TREES_PLACED = MAGIC_FOREST_TREES.withPlacement(VegetationPlacedFeatures.modifiers(PlacedFeatures.createCountExtraModifier(10, 0.1f, 1)));
		MOSSY_ASPECT_ROCKS_PLACED = MOSSY_ASPECT_ROCKS.withPlacement(CountPlacementModifier.of(2), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of());
	}

	public static void register()
	{
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("earth_crystal_geode"), EARTH_CRYSTAL_GEODE);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("basalt_delta_geode"), BASALT_DELTA_GEODE);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("nether_geode"), NETHER_GEODE);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("magic_forest_trees"), MAGIC_FOREST_TREES);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("mossy_aspect_rocks"), MOSSY_ASPECT_ROCKS);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("lost_tree"), LOST_TREE);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("the_lost_forest_trees"), THE_LOST_FOREST_TREES);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("silverwood_tree"), SILVERWOOD_TREE);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, Thuwumcraft.getId("the_lost_forest_trees"), THE_LOST_FOREST_TREES.withPlacement(VegetationPlacedFeatures.modifiers(PlacedFeatures.createCountExtraModifier(10, 0.1f, 1))));
		Registry.register(BuiltinRegistries.PLACED_FEATURE, Thuwumcraft.getId("earth_crystal_geode"), EARTH_CRYSTAL_GEODE_PLACED);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, Thuwumcraft.getId("basalt_delta_geode"), BASALT_DELTA_GEODE_PLACED);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, Thuwumcraft.getId("nether_geode"), NETHER_GEODE_PLACED);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, Thuwumcraft.getId("silverwood_tree"), SILVERWOOD_TREE_PLACED);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, Thuwumcraft.getId("magic_forest_trees"), MAGIC_FOREST_TREES_PLACED);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, Thuwumcraft.getId("mossy_aspect_rocks"), MOSSY_ASPECT_ROCKS_PLACED);
	}
}
