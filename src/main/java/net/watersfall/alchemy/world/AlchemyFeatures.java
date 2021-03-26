package net.watersfall.alchemy.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.aspect.Aspects;

public class AlchemyFeatures
{
	public static final ConfiguredFeature<?, ?> EARTH_CRYSTAL_GEODE;

	static
	{
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
						new GeodeCrackConfig(0.95D, 2.0D, 2), 0.35D, 0.083D, true, 4, 7, 3, 5, 1, 3, -16, 16, 0.05D, 1)).rangeOf(YOffset.aboveBottom(6), YOffset.fixed(46)).spreadHorizontally().applyChance(30);
	}

	public static void register()
	{
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, AlchemyMod.getId("earth_crystal_geode"), EARTH_CRYSTAL_GEODE);
	}
}
