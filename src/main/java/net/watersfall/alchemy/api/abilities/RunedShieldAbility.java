package net.watersfall.alchemy.api.abilities;

import net.minecraft.entity.Entity;

public interface RunedShieldAbility extends Ability<Entity>
{
	int getMaxAmount();

	int getShieldAmount();

	int getRechargeRate();

	void setShieldAmount(int amount);

	void setMaxAmount(int amount);

	void setRechargeRate(int amount);
}
