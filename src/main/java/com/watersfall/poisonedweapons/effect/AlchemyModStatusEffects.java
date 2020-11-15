package com.watersfall.poisonedweapons.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

import java.awt.*;

public class AlchemyModStatusEffects
{
	public static final StatusEffect PROJECTILE_SHIELD = new SpecialStatusEffect(StatusEffectType.BENEFICIAL, new Color(215, 215, 215, 0).hashCode());
	public static final StatusEffect PROJECTILE_ATTRACTION = new SpecialStatusEffect(StatusEffectType.HARMFUL, new Color(127, 0, 0, 0).hashCode());
}
