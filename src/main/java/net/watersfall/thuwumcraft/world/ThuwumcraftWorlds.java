package net.watersfall.thuwumcraft.world;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.Thuwumcraft;

public class ThuwumcraftWorlds
{
	public static final RegistryKey<World> THE_UNKNOWN = RegistryKey.of(Registry.WORLD_KEY, Thuwumcraft.getId("the_unknown"));
}
