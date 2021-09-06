package net.watersfall.thuwumcraft.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.inventory.BrewingCauldronInventory;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRecipes;
import net.watersfall.thuwumcraft.recipe.CauldronIngredient;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

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

	public static final Text NO_EFFECT = new TranslatableText("text.thuwumcraft.tooltip.no_effect").formatted(Formatting.GRAY);
	public static final Text APPLIED_EFFECTS = new TranslatableText("potion.whenDrank").append(" ").formatted(Formatting.GRAY);

	public static Set<StatusEffectInstance> getEffects(BrewingCauldronInventory inventory, RecipeManager manager, World world)
	{
		Optional<CauldronIngredient> optional1 = manager.getFirstMatch(ThuwumcraftRecipes.CAULDRON_INGREDIENTS, inventory.withInput(0), world);
		Optional<CauldronIngredient> optional2 = manager.getFirstMatch(ThuwumcraftRecipes.CAULDRON_INGREDIENTS, inventory.withInput(1), world);
		Optional<CauldronIngredient> optional3 = manager.getFirstMatch(ThuwumcraftRecipes.CAULDRON_INGREDIENTS, inventory.withInput(2), world);
		if(!optional1.isPresent() || !optional2.isPresent())
		{
			return INVALID_RECIPE;
		}
		CauldronIngredient i1 = optional1.get();
		CauldronIngredient i2 = optional2.get();
		StatusEffect effect = null;
		StatusEffect effect2 = null;
		int length = 0;
		int length2 = 0;
		int strength = 0;
		boolean found = false;
		for(int i = 0; i < i1.getEffects().size() && !found; i++)
		{
			for(int o = 0; o < i2.getEffects().size(); o++)
			{
				if(i1.getEffects().get(i).getEffectType() == i2.getEffects().get(o).getEffectType())
				{
					effect = i1.getEffects().get(i).getEffectType();
					length = i1.getEffects().get(i).getDuration() + i2.getEffects().get(o).getDuration();
					strength = (int) ((double) i1.getEffects().get(i).getAmplifier() / (double) i2.getEffects().get(o).getAmplifier());
					found = true;
					break;
				}
			}
		}
		if(optional3.isPresent())
		{
			CauldronIngredient i3 = optional3.get();
			found = false;
			for(int i = 0; i < i3.getEffects().size(); i++)
			{
				if(i3.getEffects().get(i).getEffectType() == effect)
				{
					strength++;
					found = true;
				}
			}
			if(!found)
			{
				effect2 = i3.getEffects().get(0).getEffectType();
				length2 = i3.getEffects().get(0).getDuration() / 2;
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

	public static NbtList toNbt(List<StatusEffectInstance> effects)
	{
		NbtList tag = new NbtList();
		effects.forEach(effect -> {
			NbtCompound effectTag = new NbtCompound();
			effectTag.putInt(EFFECT, StatusEffect.getRawId(effect.getEffectType()));
			effectTag.putInt(DURATION, effect.getDuration());
			effectTag.putInt(AMPLIFIER, effect.getAmplifier());
			tag.add(effectTag);
		});
		return tag;
	}

	public static void createItem(ItemStack stack, Set<StatusEffectInstance> effects)
	{
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList list = tag.getList(EFFECTS_LIST, NbtType.LIST);
		list.clear();
		effects.forEach((effect) -> {
			NbtCompound effectTag = new NbtCompound();
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
		if(instance.getEffectType().getCategory() == StatusEffectCategory.HARMFUL)
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

	public static StatusEffectInstance getEffectFromTag(NbtCompound tag)
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

	public static List<StatusEffectInstance> getEffectsFromTag(NbtCompound tag)
	{
		if(tag == null || !tag.contains(EFFECTS_LIST))
		{
			return EMPTY_LIST;
		}
		NbtList listTag = tag.getList(EFFECTS_LIST, NbtType.COMPOUND);
		if(listTag.size() <= 0)
		{
			return EMPTY_LIST;
		}
		else
		{
			List<StatusEffectInstance> list = new ArrayList<>(listTag.size());
			listTag.forEach((element) -> {
				list.add(getEffectFromTag((NbtCompound) element));
			});
			return list;
		}
	}

	public static boolean hasUses(NbtCompound tag)
	{
		if(tag.contains(USES))
		{
			int uses = tag.getInt(USES);
			return uses > 0 || uses == -1;
		}
		return false;
	}

	public static void decrementUses(NbtCompound tag)
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
