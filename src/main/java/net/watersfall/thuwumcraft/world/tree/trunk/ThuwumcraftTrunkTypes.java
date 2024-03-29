package net.watersfall.thuwumcraft.world.tree.trunk;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import net.watersfall.thuwumcraft.Thuwumcraft;

public class ThuwumcraftTrunkTypes
{
	public static TrunkPlacerType<SilverwoodTrunkPlacer> SILVERWOOD_TRUNK;

	public static void register()
	{
		SILVERWOOD_TRUNK = Registry.register(Registry.TRUNK_PLACER_TYPE, Thuwumcraft.getId("silverwood_trunk"), new TrunkPlacerType(SilverwoodTrunkPlacer.CODEC));
	}
}
