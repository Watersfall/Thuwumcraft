package net.watersfall.alchemy.api.abilities.chunk;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.Chunk;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.api.abilities.AbilityClientSerializable;

public interface VisAbility extends Ability<Chunk>, AbilityClientSerializable<Chunk>
{
	public static final Identifier ID = new Identifier("waters_alchemy_mod", "vis_ability");

	int getVis();

	int getMaxVis();

	void setVis(int vis);

	void setMaxVis(int maxVis);
}
