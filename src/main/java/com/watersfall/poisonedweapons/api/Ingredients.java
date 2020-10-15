package com.watersfall.poisonedweapons.api;

import com.google.common.collect.Lists;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class Ingredients
{

	public static final Map<Item, Ingredient> ingredients = new HashMap<>();

	static
	{
		ingredients.put(
				Items.WITHER_ROSE,
				new Ingredient(
						Items.WITHER_ROSE,
						Lists.newArrayList(
								new StatusEffectInstance(
										StatusEffects.WITHER,
										200,
										1
								),
								new StatusEffectInstance(
										StatusEffects.INSTANT_DAMAGE,
										1,
										5
								)
						)
				)
		);
		ingredients.put(
				Items.WITHER_SKELETON_SKULL,
				new Ingredient(
						Items.WITHER_SKELETON_SKULL,
						Lists.newArrayList(
								new StatusEffectInstance(
										StatusEffects.WITHER,
										400,
										1
								)
						)
				)
		);
		ingredients.put(
				Items.INK_SAC,
				new Ingredient(
						Items.INK_SAC,
						Lists.newArrayList(
								new StatusEffectInstance(
										StatusEffects.BLINDNESS,
										200,
										1
								)
						)
				)
		);
	}
}
