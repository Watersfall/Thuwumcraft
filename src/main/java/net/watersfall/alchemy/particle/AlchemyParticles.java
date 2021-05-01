package net.watersfall.alchemy.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;

public class AlchemyParticles
{
	public static final DefaultParticleType MAGIC_FOREST = register("magic_forest", FabricParticleTypes.simple());

	public static <T extends ParticleType<?>> T register(String name, T particle)
	{
		return Registry.register(Registry.PARTICLE_TYPE, AlchemyMod.getId(name), particle);
	}
}
