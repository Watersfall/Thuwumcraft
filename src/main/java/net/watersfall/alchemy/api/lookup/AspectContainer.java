package net.watersfall.alchemy.api.lookup;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.util.math.Direction;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.aspect.AspectStack;

public interface AspectContainer
{
	public static final BlockApiLookup<AspectContainer, Direction> API = BlockApiLookup.get(AlchemyMod.getId("aspect_container"), AspectContainer.class, Direction.class);

	AspectStack insert(AspectStack stack);

	AspectStack extract(AspectStack stack);

	int getSuction();
}
