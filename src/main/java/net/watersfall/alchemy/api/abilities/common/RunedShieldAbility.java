package net.watersfall.alchemy.api.abilities.common;

import net.watersfall.alchemy.api.abilities.Ability;

public interface RunedShieldAbility<T> extends Ability<T>
{
	int getMaxAmount();

	int getShieldAmount();

	int getRechargeRate();

	void setShieldAmount(int amount);

	void setMaxAmount(int amount);

	void setRechargeRate(int amount);
}
