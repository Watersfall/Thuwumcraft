package net.watersfall.thuwumcraft.registry;

import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.fluid.DimensionalFluid;

public class ThuwumcraftFluids
{
	/**
	 * TODO: Black Goo
	 *
	 * A mystical fluid originating in the unknown. Normal, unwarped people
	 * perceive it as just simple water, and it will behave just like that. Those
	 * whose minds have been tainted by warp, however, will see its true solid
	 * blackness.
	 */
	public static DimensionalFluid DIMENSIONAL_STILL;
	public static DimensionalFluid DIMENSIONAL_FLOWING;

	public static void register()
	{
		DIMENSIONAL_STILL = Registry.register(Registry.FLUID, Thuwumcraft.getId("dimensional_fluid"), new DimensionalFluid.Still());
		DIMENSIONAL_FLOWING = Registry.register(Registry.FLUID, Thuwumcraft.getId("dimensional_fluid_flowing"), new DimensionalFluid.Flowing());
	}
}
