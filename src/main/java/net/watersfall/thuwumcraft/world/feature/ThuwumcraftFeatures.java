package net.watersfall.thuwumcraft.world.feature;

import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.world.config.DecoratedRockConfig;
import net.watersfall.thuwumcraft.world.config.NetherGeodeConfig;

public class ThuwumcraftFeatures
{
	public static NetherGeodeFeature NETHER_GEODE_FEATURE;
	public static DecoratedRockFeature DECORATED_ROCK_FEATURE;

	public static void register()
	{
		NETHER_GEODE_FEATURE = Registry.register(Registry.FEATURE, Thuwumcraft.getId("nether_geode"), new NetherGeodeFeature(NetherGeodeConfig.CODEC));
		DECORATED_ROCK_FEATURE = Registry.register(Registry.FEATURE, Thuwumcraft.getId("decorated_rock"), new DecoratedRockFeature(DecoratedRockConfig.CODEC));
	}
}
