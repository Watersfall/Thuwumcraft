package com.watersfall.alchemy.util;

import com.google.common.collect.ImmutableSet;
import com.watersfall.alchemy.block.BrewingCauldronBlock;
import com.watersfall.alchemy.inventory.BrewingCauldronInventory;
import com.watersfall.alchemy.recipe.CauldronRecipe;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.Registry;

import java.util.Collections;
import java.util.Set;

public class StatusEffectHelper
{
	public static void use(ItemStack stack)
	{
		if(stack.getTag() != null)
		{
			CompoundTag tag = stack.getTag().getCompound("effect");
			int uses = tag.getInt("uses");
			if(uses <= 1)
			{
				stack.getTag().remove("effect");
			}
			else
			{
				tag.putInt("uses", uses - 1);
			}
		}
	}

	public static Set<StatusEffectInstance> getEffects(BrewingCauldronInventory inventory)
	{
		CauldronRecipe i1 = BrewingCauldronBlock.INGREDIENTS.get(inventory.getContents().get(0).getItem());
		CauldronRecipe i2 = BrewingCauldronBlock.INGREDIENTS.get(inventory.getContents().get(1).getItem());
		CauldronRecipe i3 = null;
		if(inventory.getIngredientCount() == 3)
		{
			i3 = BrewingCauldronBlock.INGREDIENTS.get(inventory.getContents().get(2).getItem());
		}
		StatusEffect effect = null;
		StatusEffect effect2 = null;
		int length = 0;
		int length2 = 0;
		int strength = 0;
		boolean found = false;
		for(int i = 0; i < i1.effects.size() && !found; i++)
		{
			for(int o = 0; o < i2.effects.size(); o++)
			{
				if(i1.effects.get(i).getEffectType() == i2.effects.get(o).getEffectType())
				{
					effect = i1.effects.get(i).getEffectType();
					length = i1.effects.get(i).getDuration() + i2.effects.get(o).getDuration();
					strength = (int) ((double) i1.effects.get(i).getAmplifier() / (double) i2.effects.get(o).getAmplifier());
					found = true;
					break;
				}
			}
		}
		if(i3 != null)
		{
			found = false;
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

	public static void createLadle(ItemStack stack, Set<StatusEffectInstance> effects)
	{
		CompoundTag tag;
		if(stack.getTag() == null)
		{
			tag = new CompoundTag();
		}
		else
		{
			tag = stack.getTag();
		}
		tag.remove("effects");
		tag.put("effects", new CompoundTag());
		stack.setTag(tag);
		tag = tag.getCompound("effects");
		int i = 0;
		for(StatusEffectInstance effect : effects)
		{
			CompoundTag subTag = new CompoundTag();
			subTag.putString("id", String.valueOf(Registry.STATUS_EFFECT.getId(effect.getEffectType())));
			subTag.putInt("duration", effect.getDuration());
			tag.put("effect" + i, subTag);
			i++;
		}

	}
}
