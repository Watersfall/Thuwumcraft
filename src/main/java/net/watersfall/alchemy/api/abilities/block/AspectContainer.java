package net.watersfall.alchemy.api.abilities.block;

import net.minecraft.util.math.Direction;
import net.watersfall.alchemy.api.aspect.AspectStack;

public interface AspectContainer
{
	boolean canInsert(AspectStack stack, Direction direction);

	boolean canExtract(Direction direction);

	AspectStack insert(AspectStack stack);

	AspectStack extract(AspectStack stack);
}
