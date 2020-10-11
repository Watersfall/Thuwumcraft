package com.watersfall.poisonedweapons;

import com.watersfall.poisonedweapons.event.ApplyAffectEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;

public class PoisonedWeapons implements ModInitializer
{
	@Override
	public void onInitialize()
	{
		AttackEntityCallback.EVENT.register(new ApplyAffectEvent());
	}
}
