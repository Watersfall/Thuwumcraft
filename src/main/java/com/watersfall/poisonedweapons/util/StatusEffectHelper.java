package com.watersfall.poisonedweapons.util;

import com.watersfall.poisonedweapons.api.Poisonable;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.Registry;

import java.util.Set;

public class StatusEffectHelper
{
	public static void set(ItemStack item, StatusEffect effect, int duration, int uses)
	{
		if(item.getItem() instanceof Poisonable)
		{
			CompoundTag tag = new CompoundTag();
			tag.putString("id", String.valueOf(Registry.STATUS_EFFECT.getId(effect)));
			tag.putInt("duration", duration);
			tag.putInt("uses", uses);
			item.putSubTag("effect", tag);
		}
	}

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
