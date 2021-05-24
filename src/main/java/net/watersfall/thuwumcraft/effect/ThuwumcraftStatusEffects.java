package net.watersfall.thuwumcraft.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;

import java.awt.*;

public class ThuwumcraftStatusEffects
{
	public static final StatusEffect PROJECTILE_SHIELD = new SpecialStatusEffect(StatusEffectType.BENEFICIAL, new Color(215, 215, 215, 0).hashCode());
	public static final StatusEffect PROJECTILE_ATTRACTION = new SpecialStatusEffect(StatusEffectType.HARMFUL, new Color(127, 0, 0, 0).hashCode());
	public static final StatusEffect PROJECTILE_WEAKNESS = new SpecialStatusEffect(StatusEffectType.HARMFUL, new Color(127, 0, 0, 0).hashCode());
	public static final StatusEffect PROJECTILE_RESISTANCE = new SpecialStatusEffect(StatusEffectType.BENEFICIAL, new Color(215, 215, 215, 0).hashCode());

	public static void register()
	{
		Registry.register(Registry.STATUS_EFFECT, Thuwumcraft.getId("projectile_shield"), ThuwumcraftStatusEffects.PROJECTILE_SHIELD);
		Registry.register(Registry.STATUS_EFFECT, Thuwumcraft.getId("projectile_attraction"), ThuwumcraftStatusEffects.PROJECTILE_ATTRACTION);
		Registry.register(Registry.STATUS_EFFECT, Thuwumcraft.getId("projectile_weakness"), ThuwumcraftStatusEffects.PROJECTILE_WEAKNESS);
		Registry.register(Registry.STATUS_EFFECT, Thuwumcraft.getId("projectile_resistance"), ThuwumcraftStatusEffects.PROJECTILE_RESISTANCE);
	}
}
