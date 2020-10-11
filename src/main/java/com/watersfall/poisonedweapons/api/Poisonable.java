package com.watersfall.poisonedweapons.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;

public interface Poisonable
{
	StatusEffect getEffect(ItemStack stack);

	int getDuration(ItemStack stack);

	int getUses(ItemStack stack);

	void applyEffect(Entity attacker, Entity target);
}
