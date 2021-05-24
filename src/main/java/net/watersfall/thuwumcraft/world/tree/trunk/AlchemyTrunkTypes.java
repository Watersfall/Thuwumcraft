package net.watersfall.thuwumcraft.world.tree.trunk;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import net.watersfall.thuwumcraft.AlchemyMod;

public class AlchemyTrunkTypes
{
	public static final TrunkPlacerType<SilverwoodTrunkPlacer> SILVERWOOD_TRUNK;

	static
	{
		SILVERWOOD_TRUNK = Registry.register(Registry.TRUNK_PLACER_TYPE, AlchemyMod.getId("silverwood_trunk"), new TrunkPlacerType(SilverwoodTrunkPlacer.CODEC));
	}
}
