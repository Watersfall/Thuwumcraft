package net.watersfall.thuwumcraft.registry;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.effect.BerserkStatusEffect;
import net.watersfall.thuwumcraft.effect.SpecialStatusEffect;

import java.awt.*;

public class ThuwumcraftStatusEffects
{
	public static final StatusEffect PROJECTILE_SHIELD = new SpecialStatusEffect(StatusEffectType.BENEFICIAL, new Color(215, 215, 215, 0).hashCode());
	public static final StatusEffect PROJECTILE_ATTRACTION = new SpecialStatusEffect(StatusEffectType.HARMFUL, new Color(127, 0, 0, 0).hashCode());
	public static final StatusEffect PROJECTILE_WEAKNESS = new SpecialStatusEffect(StatusEffectType.HARMFUL, new Color(127, 0, 0, 0).hashCode());
	public static final StatusEffect PROJECTILE_RESISTANCE = new SpecialStatusEffect(StatusEffectType.BENEFICIAL, new Color(215, 215, 215, 0).hashCode());
	public static final StatusEffect BERSERK = new BerserkStatusEffect()
			.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "91193874-e7fc-4390-a179-2f7d92590427", 0.25, EntityAttributeModifier.Operation.MULTIPLY_BASE)
			.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "33e6af94-20c6-405a-b7a2-a1083627fd39", 0, EntityAttributeModifier.Operation.MULTIPLY_BASE)
			.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "4dacc547-b0c3-4715-b60c-356b9bf7915f", 0, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
			.addAttributeModifier(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, "d209c50b-8ee9-41eb-b7d2-1ae9c3a00878", 0, EntityAttributeModifier.Operation.ADDITION)
			.addAttributeModifier(EntityAttributes.GENERIC_ARMOR, "bb0b4074-be72-47f8-90de-52871ed4b2c1", 0, EntityAttributeModifier.Operation.ADDITION)
			.addAttributeModifier(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, "f91ddca3-d421-4ac4-8db0-30729fe29c30", 0, EntityAttributeModifier.Operation.ADDITION);

	public static void register()
	{
		Registry.register(Registry.STATUS_EFFECT, Thuwumcraft.getId("projectile_shield"), ThuwumcraftStatusEffects.PROJECTILE_SHIELD);
		Registry.register(Registry.STATUS_EFFECT, Thuwumcraft.getId("projectile_attraction"), ThuwumcraftStatusEffects.PROJECTILE_ATTRACTION);
		Registry.register(Registry.STATUS_EFFECT, Thuwumcraft.getId("projectile_weakness"), ThuwumcraftStatusEffects.PROJECTILE_WEAKNESS);
		Registry.register(Registry.STATUS_EFFECT, Thuwumcraft.getId("projectile_resistance"), ThuwumcraftStatusEffects.PROJECTILE_RESISTANCE);
		Registry.register(Registry.STATUS_EFFECT, Thuwumcraft.getId("berserk"), BERSERK);
	}
}
