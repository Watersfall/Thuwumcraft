package net.watersfall.thuwumcraft.registry;

import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.fluid.DimensionalFluid;

public class ThuwumcraftFluids
{
	public static DimensionalFluid DIMENSIONAL_STILL;
	public static DimensionalFluid DIMENSIONAL_FLOWING;

	public static void register()
	{
		DIMENSIONAL_STILL = Registry.register(Registry.FLUID, Thuwumcraft.getId("dimensional_fluid"), new DimensionalFluid.Still());
		DIMENSIONAL_FLOWING = Registry.register(Registry.FLUID, Thuwumcraft.getId("dimensional_fluid_flowing"), new DimensionalFluid.Flowing());
	}
}
