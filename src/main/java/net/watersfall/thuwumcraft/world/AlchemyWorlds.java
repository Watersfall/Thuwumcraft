package net.watersfall.thuwumcraft.world;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.AlchemyMod;

public class AlchemyWorlds
{
	public static final RegistryKey<World> THE_UNKNOWN = RegistryKey.of(Registry.WORLD_KEY, AlchemyMod.getId("the_unknown"));
}
