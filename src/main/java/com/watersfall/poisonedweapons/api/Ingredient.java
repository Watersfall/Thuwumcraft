package com.watersfall.poisonedweapons.api;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Ingredient
{
	public final Item item;
	public final List<StatusEffectInstance> effects;
	public final int color;

	public Ingredient(Item item, List<StatusEffectInstance> effects)
	{
		this.item = item;
		this.effects = effects;
		color = -1;
	}

	public Ingredient(Item item, List<StatusEffectInstance> effects, int color)
	{

		this.item = item;
		this.effects = effects;
		this.color = color;
	}

	public static StatusEffectInstance getSingleEffectFromIngredients(Ingredient i1, Ingredient i2, Ingredient i3)
	{
		StatusEffect effect = null;
		int length = 0;
		int strength = 0;
		for(int i = 0; i < i1.effects.size(); i++)
		{
			for(int o = 0; o < i2.effects.size(); o++)
			{
				if(i1.effects.get(i).getEffectType() == i2.effects.get(o).getEffectType())
				{
					effect = i1.effects.get(i).getEffectType();
					length = i1.effects.get(i).getDuration() + i2.effects.get(o).getDuration();
					strength = (int) ((double) i1.effects.get(i).getAmplifier() / (double) i2.effects.get(o).getAmplifier());
				}
			}
		}
		if(i3 != null)
		{
			for(int i = 0; i < i3.effects.size(); i++)
			{
				if(i3.effects.get(i).getEffectType() == effect)
				{
					strength++;
				}
			}
		}
		return new StatusEffectInstance(effect, length, strength);
	}

	public static Set<StatusEffectInstance> getEffectsFromIngredients(Ingredient i1, Ingredient i2, Ingredient i3)
	{
		StatusEffect effect = null;
		StatusEffect effect2 = null;
		int length = 0;
		int length2 = 0;
		int strength = 0;
		for(int i = 0; i < i1.effects.size(); i++)
		{
			for(int o = 0; o < i2.effects.size(); o++)
			{
				if(i1.effects.get(i).getEffectType() == i2.effects.get(o).getEffectType())
				{
					effect = i1.effects.get(i).getEffectType();
					length = i1.effects.get(i).getDuration() + i2.effects.get(o).getDuration();
					strength = (int) ((double) i1.effects.get(i).getAmplifier() / (double) i2.effects.get(o).getAmplifier());
				}
			}
		}
		if(i3 != null)
		{
			boolean found = false;
			for(int i = 0; i < i3.effects.size(); i++)
			{
				if(i3.effects.get(i).getEffectType() == effect)
				{
					strength++;
					found = true;
				}
			}
			if(!found)
			{
				effect2 = i3.effects.get(0).getEffectType();
				length2 = i3.effects.get(0).getDuration() / 2;
			}
		}
		if(effect2 == null)
		{
			return Collections.singleton(new StatusEffectInstance(effect, length, strength));
		}
		else
		{
			return ImmutableSet.of(new StatusEffectInstance(effect, length, strength), new StatusEffectInstance(effect2, length2, 1));
		}
	}
}
