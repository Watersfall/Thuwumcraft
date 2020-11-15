package com.watersfall.poisonedweapons.api;

import com.google.common.collect.Lists;
import com.watersfall.poisonedweapons.effect.AlchemyModStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.awt.*;
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
										1
								),
								new StatusEffectInstance(
										StatusEffects.POISON,
										1200,
										1
								),
								new StatusEffectInstance(
										StatusEffects.BLINDNESS,
										1200,
										1
								)
						),
						new Color(16, 48, 16, 0).hashCode()
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
								),
								new StatusEffectInstance(
										StatusEffects.INSTANT_DAMAGE,
										1,
										1
								),
								new StatusEffectInstance(
										StatusEffects.POISON,
										1200,
										1
								),
								new StatusEffectInstance(
										StatusEffects.BLINDNESS,
										1200,
										1
								)
						),
						new Color(16, 16, 16, 0).hashCode()
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
								),
								new StatusEffectInstance(
										StatusEffects.POISON,
										200,
										1
								),
								new StatusEffectInstance(
										StatusEffects.NAUSEA,
										200,
										1
								),
								new StatusEffectInstance(
										StatusEffects.WATER_BREATHING,
										200,
										1
								)
						),
						new Color(0, 0, 255, 0).hashCode()
				)
		);
		ingredients.put(
				Items.REDSTONE,
				new Ingredient(
						Items.REDSTONE,
						Lists.newArrayList(
								new StatusEffectInstance(
										StatusEffects.NIGHT_VISION,
										600,
										1
								),
								new StatusEffectInstance(
										StatusEffects.INSTANT_DAMAGE,
										1,
										1
								),
								new StatusEffectInstance(
										StatusEffects.LUCK,
										200,
										1
								),
								new StatusEffectInstance(
										StatusEffects.UNLUCK,
										400,
										1
								)
						),
						new Color(255, 0, 0, 0).hashCode()
				)
		);
		ingredients.put(
				Items.GLOWSTONE_DUST,
				new Ingredient(
						Items.GLOWSTONE_DUST,
						Lists.newArrayList(
								new StatusEffectInstance(
										StatusEffects.GLOWING,
										600,
										1
								),
								new StatusEffectInstance(
										StatusEffects.NIGHT_VISION,
										600,
										1
								),
								new StatusEffectInstance(
										StatusEffects.HASTE,
										300,
										1
								),
								new StatusEffectInstance(
										StatusEffects.FIRE_RESISTANCE,
										200,
										1
								)
						),
						new Color(255, 255, 0, 0).hashCode()
				)
		);
		ingredients.put(
				Items.SPIDER_EYE,
				new Ingredient(
						Items.SPIDER_EYE,
						Lists.newArrayList(
								new StatusEffectInstance(
										StatusEffects.INSTANT_DAMAGE,
										1,
										1
								),
								new StatusEffectInstance(
										StatusEffects.WEAKNESS,
										600,
										1
								),
								new StatusEffectInstance(
										StatusEffects.POISON,
										300,
										1
								),
								new StatusEffectInstance(
										StatusEffects.MINING_FATIGUE,
										300,
										1
								)
						),
						new Color(96, 16, 16, 0).hashCode()
				)
		);
		ingredients.put(
				Items.WHEAT,
				new Ingredient(
						Items.WHEAT,
						Lists.newArrayList(
								new StatusEffectInstance(
										StatusEffects.INSTANT_HEALTH,
										1,
										1
								),
								new StatusEffectInstance(
										StatusEffects.ABSORPTION,
										200,
										1
								)
						),
						new Color(255, 255, 32, 0).hashCode()
				)
		);
		ingredients.put(
				Items.SEA_PICKLE,
				new Ingredient(
						Items.SEA_PICKLE,
						Lists.newArrayList(
								new StatusEffectInstance(
										StatusEffects.GLOWING,
										200,
										1
								),
								new StatusEffectInstance(
										StatusEffects.WATER_BREATHING,
										200,
										1
								)
						)
				)
		);
		ingredients.put(
				Items.DANDELION,
				new Ingredient(
						Items.DANDELION,
						Lists.newArrayList(
								new StatusEffectInstance(
										StatusEffects.INSTANT_HEALTH,
										1,
										1
								)
						)
				)
		);
		ingredients.put(
				Items.POPPY,
				new Ingredient(
						Items.POPPY,
						Lists.newArrayList(
								new StatusEffectInstance(
										StatusEffects.SLOWNESS,
										150,
										1
								)
						)
				)
		);
		ingredients.put(
				Items.BLUE_ORCHID,
				new Ingredient(
						Items.BLUE_ORCHID,
						Lists.newArrayList(
								new StatusEffectInstance(
										StatusEffects.WATER_BREATHING,
										100,
										1
								)
						)
				)
		);
		ingredients.put(
				Items.ALLIUM,
				new Ingredient(
						Items.ALLIUM,
						Lists.newArrayList(
								new StatusEffectInstance(
										StatusEffects.NIGHT_VISION,
										100,
										1
								)
						)
				)
		);
		ingredients.put(
				Items.AZURE_BLUET,
				new Ingredient(
						Items.AZURE_BLUET,
						Lists.newArrayList(
								new StatusEffectInstance(
										StatusEffects.ABSORPTION,
										100,
										1
								)
						)
				)
		);
		ingredients.put(
				Items.RED_TULIP,
				new Ingredient(
						Items.RED_TULIP,
						Lists.newArrayList(
								new StatusEffectInstance(
										AlchemyModStatusEffects.PROJECTILE_SHIELD,
										1000,
										1
								)
						)
				)
		);
		ingredients.put(
				Items.PINK_TULIP,
				new Ingredient(
						Items.PINK_TULIP,
						Lists.newArrayList(
								new StatusEffectInstance(
										AlchemyModStatusEffects.PROJECTILE_SHIELD,
										1000,
										1
								)
						)
				)
		);
		ingredients.put(
				Items.ORANGE_TULIP,
				new Ingredient(
						Items.ORANGE_TULIP,
						Lists.newArrayList(
								new StatusEffectInstance(
										AlchemyModStatusEffects.PROJECTILE_ATTRACTION,
										1000,
										1
								)
						)
				)
		);
		ingredients.put(
				Items.WHITE_TULIP,
				new Ingredient(
						Items.WHITE_TULIP,
						Lists.newArrayList(
								new StatusEffectInstance(
										AlchemyModStatusEffects.PROJECTILE_ATTRACTION,
										1000,
										1
								)
						)
				)
		);
	}
}
