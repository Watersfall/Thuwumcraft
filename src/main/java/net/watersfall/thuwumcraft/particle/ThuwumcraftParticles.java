package net.watersfall.thuwumcraft.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;

public class ThuwumcraftParticles
{
	public static final DefaultParticleType MAGIC_FOREST = register("magic_forest", FabricParticleTypes.simple());
	public static final DefaultParticleType WATER = register("water", FabricParticleTypes.simple());
	public static final DefaultParticleType FIRE = register("fire", FabricParticleTypes.simple());

	public static <T extends ParticleType<?>> T register(String name, T particle)
	{
		return Registry.register(Registry.PARTICLE_TYPE, Thuwumcraft.getId(name), particle);
	}
}
