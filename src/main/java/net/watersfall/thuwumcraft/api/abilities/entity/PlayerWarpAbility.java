package net.watersfall.thuwumcraft.api.abilities.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.watersfall.wet.api.abilities.Ability;
import net.watersfall.wet.api.abilities.AbilityClientSerializable;

public interface PlayerWarpAbility extends Ability<Entity>, AbilityClientSerializable<Entity>
{
	public static final Identifier ID = new Identifier("thuwumcraft", "warp");

	double getTemporaryWarp();

	void setTemporaryWarp(double warp);

	double getNormalWarp();

	void setNormalWarp(double warp);

	double getPermanentWarp();

	void setPermanentWarp(double warp);

	default double getTotalWarp()
	{
		return getTemporaryWarp() + getNormalWarp() + getPermanentWarp();
	}

	@Override
	default Identifier getId()
	{
		return ID;
	}
}
