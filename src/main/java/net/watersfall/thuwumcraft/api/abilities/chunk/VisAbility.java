package net.watersfall.thuwumcraft.api.abilities.chunk;

import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.Chunk;
import net.watersfall.wet.api.abilities.Ability;
import net.watersfall.wet.api.abilities.AbilityClientSerializable;


/**
 * TODO: Fake Magic
 *
 * The magic ever present. Vis, and it's counterpart, flux, flow out
 * of the unknown and into the other worlds through nodes. Vis is "ordered"
 * magical energy, able to be captured, harnessed, and released in controlled
 * rituals. Flux, on the other hand, is chaotic magic. It can manifest in
 * the physical world in a variety of ways, but much like Vis, these
 * manifestations consume flux, reducing the amount present. Unlike Vis, however
 * flux is not produced naturally, and is only caused by the workings of a
 * thaumaturge.
 *
 * Effects of flux:
 * 		- Spawn mobs (creepers?)
 * 		- "Warp" mobs, to increase power? Maybe make them new mobs entirely?
 * 		- Increase flux for nearby players
 * 		- Reduce max vis capacity
 * 		- Increase infusion instability
 */
public interface VisAbility extends Ability<Chunk>, AbilityClientSerializable<Chunk>
{
	public static final Identifier ID = new Identifier("thuwumcraft", "vis_ability");

	double getVis();

	double getMaxVis();

	double getFlux();

	void setVis(double vis);

	void setMaxVis(double maxVis);

	void setFlux(double flux);
}
