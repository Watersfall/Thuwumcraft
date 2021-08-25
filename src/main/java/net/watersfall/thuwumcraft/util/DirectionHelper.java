package net.watersfall.thuwumcraft.util;

import net.minecraft.util.math.Direction;

public class DirectionHelper
{
	public static Direction getLeft(Direction direction)
	{
		return switch(direction)
		{
			case NORTH -> Direction.WEST;
			case SOUTH -> Direction.EAST;
			case WEST -> Direction.SOUTH;
			case EAST -> Direction.NORTH;
			default -> direction;
		};
	}

	public static Direction getRight(Direction direction)
	{
		return getLeft(direction).getOpposite();
	}
}
