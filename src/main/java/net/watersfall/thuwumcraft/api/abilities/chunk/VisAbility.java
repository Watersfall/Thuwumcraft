package net.watersfall.thuwumcraft.api.abilities.chunk;

import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.Chunk;
import net.watersfall.wet.api.abilities.Ability;
import net.watersfall.wet.api.abilities.AbilityClientSerializable;

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
