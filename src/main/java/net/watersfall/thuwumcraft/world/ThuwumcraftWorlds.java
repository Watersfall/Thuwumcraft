package net.watersfall.thuwumcraft.world;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.Thuwumcraft;

public class ThuwumcraftWorlds
{
	/**
	 * TODO: The Realm of the Ancient Beings
	 *
	 * 		A decrepit and decayed dimension that was once home to the ancient beings
	 * that seemingly "created" thaumaturgy. The dimension will first be accessed
	 * temporarily, after being sufficiently warped and jumping into dimensional
	 * fluid. However, this will only work ?once?, quickly kicking the player out
	 * of the dimension. This quick arrival will still infuse the player with
	 * enough of the dimensions magical energy to unlock the full thaumaturgical
	 * capabilities.
	 *
	 * 		The dimension should have a high, mostly intact surface, similar to the
	 * overworld. The surface should a harsh and inorganic place, with run down ruins,
	 * harsh wind?, and no/quiet ambient music. The sun is dim, but toxic, negatively
	 * impacting the players health, but increasing magical ability. The structures
	 * found on the surface should be primarily dedicated to channeling that magical
	 * ability from the sun, with roofs made of material that blocks/channels the
	 * radiation, surrounding a central spire that collects it. The underground, however,
	 * should be more hollow and layered, like the nether. It is also covered in life.
	 * Various types of magical forests, living crystals, and creatures unique to the
	 * dimension. Structures in this area will primarily revolve around living areas,
	 * similar to villages. Deep underground is similar to the overworld again, being
	 * mostly solid with occasional caves. Large openings and ancient mining shafts,
	 * however, can be found here, occasionally with leftover mining equipment.
	 */
	public static final RegistryKey<World> THE_UNKNOWN = RegistryKey.of(Registry.WORLD_KEY, Thuwumcraft.getId("the_unknown"));
}
