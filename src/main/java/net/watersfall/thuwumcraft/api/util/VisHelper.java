package net.watersfall.thuwumcraft.api.util;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.abilities.chunk.VisAbility;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.util.Optional;

public class VisHelper
{
	public static VisAbility getAbility(World world, ChunkPos pos)
	{
		return getAbility(world.getChunk(pos.x, pos.z));
	}

	public static VisAbility getAbility(Chunk chunk)
	{
		Optional<VisAbility> optional = AbilityProvider.getAbility(chunk, VisAbility.ID, VisAbility.class);
		if(optional.isPresent())
		{
			return optional.get();
		}
		else
		{
			Thuwumcraft.LOGGER.error("Failed to get vis from a chunk! Things are about to go very bad");
			return null;
		}
	}
}
