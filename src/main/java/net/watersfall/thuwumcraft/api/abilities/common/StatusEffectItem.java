package net.watersfall.thuwumcraft.api.abilities.common;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.watersfall.wet.api.abilities.Ability;
import net.watersfall.wet.api.abilities.AbilityClientSerializable;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.util.List;

public interface StatusEffectItem extends Ability<ItemStack>, AbilityClientSerializable<ItemStack>
{
	public static final Identifier ID = new Identifier("thuwumcraft", "status_effect_item");

	List<StatusEffectInstance> getEffects();

	int getUses();

	void setUses(int uses);

	default void use(LivingEntity target, ItemStack stack)
	{
		setUses(getUses() - 1);
		getEffects().forEach(effect -> target.addStatusEffect(new StatusEffectInstance(effect)));
		if(getUses() <= 0)
		{
			AbilityProvider.getProvider(stack).removeAbility(this);
		}
	}
}
