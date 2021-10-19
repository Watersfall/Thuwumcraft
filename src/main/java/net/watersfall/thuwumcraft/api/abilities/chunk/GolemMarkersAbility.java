package net.watersfall.thuwumcraft.api.abilities.chunk;

import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.chunk.Chunk;
import net.watersfall.thuwumcraft.api.golem.GolemMarker;
import net.watersfall.wet.api.abilities.Ability;
import net.watersfall.wet.api.abilities.AbilityClientSerializable;

import java.util.List;
import java.util.Optional;

public interface GolemMarkersAbility extends Ability<Chunk>, AbilityClientSerializable<Chunk>
{
	Identifier ID = new Identifier("thuwumcraft", "golem_markers");

	void removeMarker(BlockPos pos, Direction dir);

	void removeMarkers(BlockPos pos);

	void addMarker(GolemMarker marker);

	Optional<GolemMarker> getMarker(BlockPos pos, Direction dir);

	List<GolemMarker> getMarkers(BlockPos pos);

	Optional<GolemMarker> getClosestMarker(BlockPos pos, DyeColor color, boolean exclude);

	List<GolemMarker> getAllMarkers();

	@Override
	default Identifier getId()
	{
		return ID;
	}
}
