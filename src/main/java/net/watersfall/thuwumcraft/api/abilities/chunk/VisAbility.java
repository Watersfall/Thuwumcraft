package net.watersfall.thuwumcraft.api.abilities.chunk;

import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.Chunk;
import net.watersfall.thuwumcraft.api.abilities.Ability;
import net.watersfall.thuwumcraft.api.abilities.AbilityClientSerializable;

public interface VisAbility extends Ability<Chunk>, AbilityClientSerializable<Chunk>
{
	public static final Identifier ID = new Identifier("thuwumcraft", "vis_ability");

	int getVis();

	int getMaxVis();

	void setVis(int vis);

	void setMaxVis(int maxVis);
}
