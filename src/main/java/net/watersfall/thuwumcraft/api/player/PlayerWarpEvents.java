package net.watersfall.thuwumcraft.api.player;

import net.minecraft.util.collection.WeightedList;

public class PlayerWarpEvents
{
	private static final WeightedList<PlayerWarpEvent> EVENTS = new WeightedList<>();

	public static PlayerWarpEvent register(int weight, PlayerWarpEvent event)
	{
		EVENTS.add(event, weight);
		return event;
	}

	public static WeightedList<PlayerWarpEvent> getEvents()
	{
		return EVENTS;
	}
}
