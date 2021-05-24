package net.watersfall.thuwumcraft.world.feature;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.BiasedRangedDecoratorConfig;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.RandomSpreadFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.BendingTrunkPlacer;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.aspect.Aspects;
import net.watersfall.thuwumcraft.block.ThuwumcraftBlocks;
import net.watersfall.thuwumcraft.world.config.DecoratedRockConfig;
import net.watersfall.thuwumcraft.world.config.NetherGeodeConfig;
import net.watersfall.thuwumcraft.world.config.NetherGeodeLayersConfig;
import net.watersfall.thuwumcraft.world.config.NetherGeodeSizeConfig;
import net.watersfall.thuwumcraft.world.tree.trunk.SilverwoodTrunkPlacer;

import java.util.ArrayList;

public class ThuwumcraftFeatures
{
	public static final NetherGeodeFeature NETHER_GEODE_FEATURE;
	public static final DecoratedRockFeature DECORATED_ROCK_FEATURE;

	public static final ConfiguredFeature<?, ?> EARTH_CRYSTAL_GEODE;
	public static final ConfiguredFeature<?, ?> NETHER_GEODE;
	public static final ConfiguredFeature<?, ?> BASALT_DELTA_GEODE;
	public static final ConfiguredFeature<?, ?> MAGIC_FOREST_TREES;
	public static final ConfiguredFeature<?, ?> MOSSY_ASPECT_ROCKS;
	public static final ConfiguredFeature<?, ?> DIMENSIONAL_LAKE;
	public static final ConfiguredFeature<?, ?> LOST_TREE;
	public static final ConfiguredFeature<?, ?> THE_LOST_FOREST_TREES;
	public static final ConfiguredFeature<?, ?> SILVERWOOD_TREE;

	static
	{
		NETHER_GEODE_FEATURE = Registry.register(Registry.FEATURE, Thuwumcraft.getId("nether_geode"), new NetherGeodeFeature(NetherGeodeConfig.CODEC));
		DECORATED_ROCK_FEATURE = Registry.register(Registry.FEATURE, Thuwumcraft.getId("decorated_rock"), new DecoratedRockFeature(DecoratedRockConfig.CODEC));
		DIMENSIONAL_LAKE = Feature.LAKE.configure(new SingleStateFeatureConfig(ThuwumcraftBlocks.DIMENSIONAL_FLUID_BLOCK.getDefaultState())).decorate(Decorator.LAVA_LAKE.configure(new ChanceDecoratorConfig(10))).decorate(Decorator.RANGE_BIASED_TO_BOTTOM.configure(new BiasedRangedDecoratorConfig(YOffset.getBottom(), YOffset.aboveBottom(64), 8))).spreadHorizontally().applyChance(8);
		EARTH_CRYSTAL_GEODE = Feature.GEODE.configure(new GeodeFeatureConfig(
						new GeodeLayerConfig(
								new SimpleBlockStateProvider(Blocks.CAVE_AIR.getDefaultState()),
								new SimpleBlockStateProvider(Blocks.CALCITE.getDefaultState()),
								new SimpleBlockStateProvider(Blocks.CALCITE.getDefaultState()),
								new SimpleBlockStateProvider(Blocks.TUFF.getDefaultState()),
								new SimpleBlockStateProvider(Blocks.SMOOTH_BASALT.getDefaultState()),
								ImmutableList.of(
										Aspects.ASPECT_TO_CLUSTER.get(Aspects.EARTH).getDefaultState(),
										Aspects.ASPECT_TO_LARGE_CLUSTER.get(Aspects.EARTH).getDefaultState(),
										Aspects.ASPECT_TO_MEDIUM_CLUSTER.get(Aspects.EARTH).getDefaultState(),
										Aspects.ASPECT_TO_SMALL_CLUSTER.get(Aspects.EARTH).getDefaultState()
								)
						),
						new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
						new GeodeCrackConfig(0.95D, 2.0D, 2), 0.35D, 0.083D, true, 4, 7, 3, 5, 1, 3, -16, 16, 0.05D, 1)).rangeOf(YOffset.aboveBottom(6), YOffset.fixed(46)).spreadHorizontally().applyChance(45);
		BASALT_DELTA_GEODE = Feature.GEODE.configure(new GeodeFeatureConfig(
				new GeodeLayerConfig(
						new SimpleBlockStateProvider(Blocks.CAVE_AIR.getDefaultState()),
						new SimpleBlockStateProvider(Aspects.ASPECT_TO_CLUSTER_BLOCK.get(Aspects.FIRE).getDefaultState()),
						new SimpleBlockStateProvider(Aspects.ASPECT_TO_BUDDING_CLUSTER.get(Aspects.FIRE).getDefaultState()),
						new SimpleBlockStateProvider(Blocks.BASALT.getDefaultState()),
						new SimpleBlockStateProvider(Blocks.BLACKSTONE.getDefaultState()),
						ImmutableList.of(
								Aspects.ASPECT_TO_CLUSTER.get(Aspects.FIRE).getDefaultState(),
								Aspects.ASPECT_TO_LARGE_CLUSTER.get(Aspects.FIRE).getDefaultState(),
								Aspects.ASPECT_TO_MEDIUM_CLUSTER.get(Aspects.FIRE).getDefaultState(),
								Aspects.ASPECT_TO_SMALL_CLUSTER.get(Aspects.FIRE).getDefaultState()
						)
				),
				new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
				new GeodeCrackConfig(0.95D, 2.0D, 2), 0.35D, 0.083D, true, 4, 7, 3, 5, 1, 3, -16, 16, 0.05D, 1)).rangeOf(YOffset.aboveBottom(6), YOffset.fixed(46)).spreadHorizontally().applyChance(45);
		NETHER_GEODE = NETHER_GEODE_FEATURE.configure(new NetherGeodeConfig(
				new NetherGeodeLayersConfig(
						new GeodeLayerConfig(
								new SimpleBlockStateProvider(Blocks.CAVE_AIR.getDefaultState()),
								new SimpleBlockStateProvider(Aspects.ASPECT_TO_CLUSTER_BLOCK.get(Aspects.FIRE).getDefaultState()),
								new SimpleBlockStateProvider(Aspects.ASPECT_TO_BUDDING_CLUSTER.get(Aspects.FIRE).getDefaultState()),
								new SimpleBlockStateProvider(Blocks.OBSIDIAN.getDefaultState()),
								new SimpleBlockStateProvider(Blocks.BLACKSTONE.getDefaultState()),
								ImmutableList.of(
										Aspects.ASPECT_TO_CLUSTER.get(Aspects.FIRE).getDefaultState(),
										Aspects.ASPECT_TO_LARGE_CLUSTER.get(Aspects.FIRE).getDefaultState(),
										Aspects.ASPECT_TO_MEDIUM_CLUSTER.get(Aspects.FIRE).getDefaultState(),
										Aspects.ASPECT_TO_SMALL_CLUSTER.get(Aspects.FIRE).getDefaultState()
								)
						),
						new SimpleBlockStateProvider(Blocks.BLACKSTONE.getDefaultState()),
						16
				),
				new NetherGeodeLayersConfig(
						new GeodeLayerConfig(
							new SimpleBlockStateProvider(Blocks.CAVE_AIR.getDefaultState()),
							new SimpleBlockStateProvider(Aspects.ASPECT_TO_CLUSTER_BLOCK.get(Aspects.FIRE).getDefaultState()),
							new SimpleBlockStateProvider(Aspects.ASPECT_TO_BUDDING_CLUSTER.get(Aspects.FIRE).getDefaultState()),
							new SimpleBlockStateProvider(Blocks.BLACKSTONE.getDefaultState()),
							new SimpleBlockStateProvider(Blocks.NETHER_WART_BLOCK.getDefaultState()),
							ImmutableList.of(
									Aspects.ASPECT_TO_CLUSTER.get(Aspects.FIRE).getDefaultState(),
									Aspects.ASPECT_TO_LARGE_CLUSTER.get(Aspects.FIRE).getDefaultState(),
									Aspects.ASPECT_TO_MEDIUM_CLUSTER.get(Aspects.FIRE).getDefaultState(),
									Aspects.ASPECT_TO_SMALL_CLUSTER.get(Aspects.FIRE).getDefaultState()
							)
						),
						new SimpleBlockStateProvider(Blocks.NETHERRACK.getDefaultState()),
						1
				),
				new NetherGeodeLayersConfig(
						new GeodeLayerConfig(
								new SimpleBlockStateProvider(Blocks.CAVE_AIR.getDefaultState()),
								new SimpleBlockStateProvider(Aspects.ASPECT_TO_CLUSTER_BLOCK.get(Aspects.FIRE).getDefaultState()),
								new SimpleBlockStateProvider(Aspects.ASPECT_TO_BUDDING_CLUSTER.get(Aspects.FIRE).getDefaultState()),
								new SimpleBlockStateProvider(Blocks.BASALT.getDefaultState()),
								new SimpleBlockStateProvider(Blocks.MAGMA_BLOCK.getDefaultState()),
								ImmutableList.of(
										Aspects.ASPECT_TO_CLUSTER.get(Aspects.FIRE).getDefaultState(),
										Aspects.ASPECT_TO_LARGE_CLUSTER.get(Aspects.FIRE).getDefaultState(),
										Aspects.ASPECT_TO_MEDIUM_CLUSTER.get(Aspects.FIRE).getDefaultState(),
										Aspects.ASPECT_TO_SMALL_CLUSTER.get(Aspects.FIRE).getDefaultState()
								)
						),
						new SimpleBlockStateProvider(Blocks.LAVA.getDefaultState()),
						16
				),
				new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
				new GeodeCrackConfig(0.95D, 2.0D, 2),
				new NetherGeodeSizeConfig(4, 7, 3, 5, 1, 3, -16, 16),
				0.35D, 0.083D, true, 0.05D, 1
			)).rangeOf(YOffset.aboveBottom(6), YOffset.fixed(46)).spreadHorizontally().applyChance(45);
		MOSSY_ASPECT_ROCKS = DECORATED_ROCK_FEATURE.configure(new DecoratedRockConfig(
				new SimpleBlockStateProvider(Blocks.MOSSY_COBBLESTONE.getDefaultState())
		)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).repeatRandomly(2);
		LOST_TREE = Feature.TREE.configure(new OpenTreeFeatureConfig(
				new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()),
				new BendingTrunkPlacer(4, 2, 0, 3, UniformIntProvider.create(1, 1)),
				new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState()),
				new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 3), UniformIntProvider.create(0, 0), UniformIntProvider.create(2, 2), 50),
				new SimpleBlockStateProvider(ThuwumcraftBlocks.DEEPSLATE_GRASS.getDefaultState()),
				new TwoLayersFeatureSize(1, 0, 1),
				new ArrayList<>(),
				false,
				false
		));
		SILVERWOOD_TREE = Feature.TREE.configure(new OpenTreeFeatureConfig(
				new SimpleBlockStateProvider(ThuwumcraftBlocks.SILVERWOOD_LOG.getDefaultState()),
				new SilverwoodTrunkPlacer(7, 2, 2),
				new SimpleBlockStateProvider(ThuwumcraftBlocks.SILVERWOOD_LEAVES.getDefaultState()),
				new RandomSpreadFoliagePlacer(UniformIntProvider.create(3, 3), UniformIntProvider.create(0, 0), UniformIntProvider.create(2, 2), 100),
				new SimpleBlockStateProvider(ThuwumcraftBlocks.DEEPSLATE_GRASS.getDefaultState()),
				new TwoLayersFeatureSize(1, 0, 1),
				new ArrayList<>(),
				false,
				false
		))
				.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_OCEAN_FLOOR_NO_WATER)
				.decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(100)));
		THE_LOST_FOREST_TREES = Feature.RANDOM_SELECTOR.configure(
				new RandomFeatureConfig(
						ImmutableList.of(
								ConfiguredFeatures.HUGE_RED_MUSHROOM.withChance(0.05F),
								ConfiguredFeatures.HUGE_BROWN_MUSHROOM.withChance(0.05F)
						),
						LOST_TREE
				)
		).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_OCEAN_FLOOR_NO_WATER)
				.decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(10, 0.1F, 1)));
		MAGIC_FOREST_TREES = Feature.RANDOM_SELECTOR.configure(
				new RandomFeatureConfig(
						ImmutableList.of(
								ConfiguredFeatures.FANCY_OAK_BEES_0002.withChance(0.5F),
								ConfiguredFeatures.HUGE_BROWN_MUSHROOM.withChance(0.05F),
								ConfiguredFeatures.HUGE_RED_MUSHROOM.withChance(0.05F),
								SILVERWOOD_TREE.withChance(0.5F)
						),
						ConfiguredFeatures.OAK
				))
				.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_OCEAN_FLOOR_NO_WATER)
				.decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(10, 0.1F, 1)));
	}

	public static void register()
	{
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("earth_crystal_geode"), EARTH_CRYSTAL_GEODE);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("basalt_delta_geode"), BASALT_DELTA_GEODE);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("nether_geode"), NETHER_GEODE);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("magic_forest_trees"), MAGIC_FOREST_TREES);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("mossy_aspect_rocks"), MOSSY_ASPECT_ROCKS);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("dimensional_lake"), DIMENSIONAL_LAKE);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("lost_tree"), LOST_TREE);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("the_lost_forest_trees"), THE_LOST_FOREST_TREES);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Thuwumcraft.getId("silverwood_tree"), SILVERWOOD_TREE);
	}
}
