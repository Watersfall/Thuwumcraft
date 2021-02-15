package net.watersfall.alchemy.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;

import java.awt.*;

public class AlchemyStatusEffects
{
	public static final StatusEffect PROJECTILE_SHIELD = new SpecialStatusEffect(StatusEffectType.BENEFICIAL, new Color(215, 215, 215, 0).hashCode());
	public static final StatusEffect PROJECTILE_ATTRACTION = new SpecialStatusEffect(StatusEffectType.HARMFUL, new Color(127, 0, 0, 0).hashCode());
	public static final StatusEffect PROJECTILE_WEAKNESS = new SpecialStatusEffect(StatusEffectType.HARMFUL, new Color(127, 0, 0, 0).hashCode());
	public static final StatusEffect PROJECTILE_RESISTANCE = new SpecialStatusEffect(StatusEffectType.BENEFICIAL, new Color(215, 215, 215, 0).hashCode());

	public static void register()
	{
		Registry.register(Registry.STATUS_EFFECT, AlchemyMod.getId("projectile_shield"), AlchemyStatusEffects.PROJECTILE_SHIELD);
		Registry.register(Registry.STATUS_EFFECT, AlchemyMod.getId("projectile_attraction"), AlchemyStatusEffects.PROJECTILE_ATTRACTION);
		Registry.register(Registry.STATUS_EFFECT, AlchemyMod.getId("projectile_weakness"), AlchemyStatusEffects.PROJECTILE_WEAKNESS);
		Registry.register(Registry.STATUS_EFFECT, AlchemyMod.getId("projectile_resistance"), AlchemyStatusEffects.PROJECTILE_RESISTANCE);
	}
}
