package net.watersfall.alchemy.world;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.watersfall.alchemy.AlchemyMod;

public class AlchemyWorlds
{
	public static final RegistryKey<World> THE_UNKNOWN = RegistryKey.of(Registry.DIMENSION, AlchemyMod.getId("the_unknown"));
}
