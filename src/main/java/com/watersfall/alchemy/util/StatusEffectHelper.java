package com.watersfall.alchemy.util;

import com.google.common.collect.ImmutableSet;
import com.watersfall.alchemy.block.BrewingCauldronBlock;
import com.watersfall.alchemy.inventory.BrewingCauldronInventory;
import com.watersfall.alchemy.recipe.CauldronRecipe;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

import java.util.*;

public class StatusEffectHelper
{
	public static final Set<StatusEffectInstance> INVALID_RECIPE = new HashSet<>(0);
	public static final List<StatusEffectInstance> EMPTY_LIST = new ArrayList<>(0);
	public static final String EFFECTS_LIST = "waters_effects";
	public static final String EFFECT = "effect";
	public static final String DURATION = "duration";
	public static final String AMPLIFIER = "amplifier";
	public static final String USES = "waters_effects_uses";

	public static final Text NO_EFFECT = new TranslatableText("text.waters_alchemy_mod.tooltip.no_effect").formatted(Formatting.GRAY);
	public static final Text APPLIED_EFFECTS = new TranslatableText("potion.whenDrank").append(": ").formatted(Formatting.GRAY);

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
		if(effect != null)
		{
			if(effect2 == null)
			{
				return Collections.singleton(new StatusEffectInstance(effect, length, strength));
			}
			else
			{
				return ImmutableSet.of(new StatusEffectInstance(effect, length, strength), new StatusEffectInstance(effect2, length2, 1));
			}
		}
		else
		{
			return INVALID_RECIPE;
		}
	}

	public static void createItem(ItemStack stack, Set<StatusEffectInstance> effects)
	{
		CompoundTag tag = stack.getOrCreateTag();
		ListTag list = tag.getList(EFFECTS_LIST, NbtType.LIST);
		list.clear();
		effects.forEach((effect) -> {
			CompoundTag effectTag = new CompoundTag();
			effectTag.putInt(EFFECT, StatusEffect.getRawId(effect.getEffectType()));
			effectTag.putInt(DURATION, effect.getDuration());
			effectTag.putInt(AMPLIFIER, effect.getAmplifier());
			list.add(effectTag);
		});
		tag.put(EFFECTS_LIST, list);
	}

	public static Text getEffectText(StatusEffectInstance instance, boolean indent)
	{
		LiteralText prefix = new LiteralText(indent ? " " : "");
		MutableText text = prefix.append(new TranslatableText(instance.getTranslationKey()))
				.append(" ")
				.append(new TranslatableText("potion.potency." + instance.getAmplifier()))
				.append(" ")
				.append("(")
				.append(StatusEffectUtil.durationToString(instance, 1))
				.append(")");
		if(instance.getEffectType().getType() == StatusEffectType.HARMFUL)
		{
			text.formatted(Formatting.RED);
		}
		else if(instance.getEffectType().isBeneficial())
		{
			text.formatted(Formatting.BLUE);
		}
		else
		{
			text.formatted(Formatting.GRAY);
		}
		return text;
	}

	public static StatusEffectInstance getEffectFromTag(CompoundTag tag)
	{
		StatusEffect effect = StatusEffect.byRawId(tag.getInt(EFFECT));
		int duration = 0;
		int amplifier = 0;
		if(tag.contains(DURATION))
		{
			duration = tag.getInt(DURATION);
		}
		if(tag.contains(AMPLIFIER))
		{
			amplifier = tag.getInt(AMPLIFIER);
		}
		return new StatusEffectInstance(effect, duration, amplifier);
	}

	public static List<StatusEffectInstance> getEffectsFromTag(CompoundTag tag)
	{
		if(tag == null || !tag.contains(EFFECTS_LIST))
		{
			return EMPTY_LIST;
		}
		ListTag listTag = tag.getList(EFFECTS_LIST, NbtType.COMPOUND);
		if(listTag.size() <= 0)
		{
			return EMPTY_LIST;
		}
		else
		{
			List<StatusEffectInstance> list = new ArrayList<>(listTag.size());
			listTag.forEach((element) -> {
				list.add(getEffectFromTag((CompoundTag) element));
			});
			return list;
		}
	}

	public static boolean hasUses(CompoundTag tag)
	{
		if(tag.contains(USES))
		{
			int uses = tag.getInt(USES);
			return uses > 0 || uses == -1;
		}
		return false;
	}

	public static void decrementUses(CompoundTag tag)
	{
		if(tag.contains(USES))
		{
			int uses = tag.getInt(USES);
			if(uses == 0 || uses == 1)
			{
				tag.remove(USES);
				tag.remove(EFFECTS_LIST);
			}
			else if(uses > 0)
			{
				tag.putInt(USES, --uses);
			}
		}
	}
}
