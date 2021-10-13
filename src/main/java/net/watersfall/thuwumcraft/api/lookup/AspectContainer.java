package net.watersfall.thuwumcraft.api.lookup;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.util.math.Direction;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;

public interface AspectContainer
{
	public static final BlockApiLookup<AspectContainer, Direction> API = BlockApiLookup.get(Thuwumcraft.getId("aspect_container"), AspectContainer.class, Direction.class);

	AspectStack insert(AspectStack stack, boolean simulate);

	AspectStack extract(AspectStack stack, boolean simulate);

	int getSuction();
}
