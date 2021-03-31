package net.watersfall.alchemy.world.feature;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.util.collection.WeightedList;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.RandomizedIntBlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.world.config.DecoratedRockConfig;
import net.watersfall.alchemy.world.config.NetherGeodeConfig;
import net.watersfall.alchemy.world.config.NetherGeodeLayersConfig;
import net.watersfall.alchemy.world.config.NetherGeodeSizeConfig;
import net.watersfall.alchemy.world.feature.NetherGeodeFeature;

public class AlchemyFeatures
{
	public static final NetherGeodeFeature NETHER_GEODE_FEATURE;
	public static final DecoratedRockFeature DECORATED_ROCK_FEATURE;

	public static final ConfiguredFeature<?, ?> EARTH_CRYSTAL_GEODE;
	public static final ConfiguredFeature<?, ?> NETHER_GEODE;
	public static final ConfiguredFeature<?, ?> BASALT_DELTA_GEODE;
	public static final ConfiguredFeature<?, ?> MAGIC_FOREST_TREES;
	public static final ConfiguredFeature<?, ?> MOSSY_ASPECT_ROCKS;

	static
	{
		NETHER_GEODE_FEATURE = Registry.register(Registry.FEATURE, AlchemyMod.getId("nether_geode"), new NetherGeodeFeature(NetherGeodeConfig.CODEC));
		DECORATED_ROCK_FEATURE = Registry.register(Registry.FEATURE, AlchemyMod.getId("decorated_rock"), new DecoratedRockFeature(DecoratedRockConfig.CODEC));

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
		MAGIC_FOREST_TREES = Feature.RANDOM_SELECTOR.configure(
				new RandomFeatureConfig(
						ImmutableList.of(
								ConfiguredFeatures.FANCY_OAK_BEES_0002.withChance(0.5F),
								ConfiguredFeatures.HUGE_BROWN_MUSHROOM.withChance(0.05F),
								ConfiguredFeatures.HUGE_RED_MUSHROOM.withChance(0.05F),
								ConfiguredFeatures.OAK.withChance(0.4F)
						),
						ConfiguredFeatures.OAK
				))
				.decorate(ConfiguredFeatures.Decorators.field_29534)
				.decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(10, 0.1F, 1)));
		MOSSY_ASPECT_ROCKS = DECORATED_ROCK_FEATURE.configure(new DecoratedRockConfig(
				new SimpleBlockStateProvider(Blocks.MOSSY_COBBLESTONE.getDefaultState())
		)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).repeatRandomly(2);
	}

	public static void register()
	{
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, AlchemyMod.getId("earth_crystal_geode"), EARTH_CRYSTAL_GEODE);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, AlchemyMod.getId("basalt_delta_geode"), BASALT_DELTA_GEODE);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, AlchemyMod.getId("nether_geode"), NETHER_GEODE);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, AlchemyMod.getId("magic_forest_trees"), MAGIC_FOREST_TREES);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, AlchemyMod.getId("mossy_aspect_rocks"), MOSSY_ASPECT_ROCKS);
	}
}
