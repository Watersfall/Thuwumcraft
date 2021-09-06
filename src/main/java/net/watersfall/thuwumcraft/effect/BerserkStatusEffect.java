package net.watersfall.thuwumcraft.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectCategory;

public class BerserkStatusEffect extends SpecialStatusEffect
{
	public BerserkStatusEffect()
	{
		super(StatusEffectCategory.BENEFICIAL, 0xff9999);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier)
	{
		int k = 50 >> amplifier;
		if (k > 0)
		{
			return duration % k == 0;
		}
		else
		{
			return true;
		}
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier)
	{
		if (entity.getHealth() < entity.getMaxHealth())
		{
			entity.heal(1);
		}
	}

	@Override
	public double adjustModifierAmount(int amplifier, EntityAttributeModifier modifier)
	{
		if(attributesEqual(EntityAttributes.GENERIC_ATTACK_SPEED, modifier))
		{
			return super.adjustModifierAmount(amplifier, modifier);
		}
		else if(attributesEqual(EntityAttributes.GENERIC_ATTACK_DAMAGE, modifier))
		{
			return 0.15 * amplifier;
		}
		else if(attributesEqual(EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier))
		{
			return 0.05 * amplifier;
		}
		else if(attributesEqual(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, modifier))
		{
			return 0.2 * amplifier;
		}
		else if(attributesEqual(EntityAttributes.GENERIC_ARMOR, modifier))
		{
			return 2 * amplifier;
		}
		else if(attributesEqual(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, modifier))
		{
			return amplifier;
		}
		return 0;
	}

	private boolean attributesEqual(EntityAttribute attribute, EntityAttributeModifier modifier)
	{
		return (this.getAttributeModifiers().get(attribute).getId().equals(modifier.getId()));
	}
}
