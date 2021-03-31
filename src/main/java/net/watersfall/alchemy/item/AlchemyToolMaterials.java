package net.watersfall.alchemy.item;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public enum AlchemyToolMaterials implements ToolMaterial
{
	MAGIC(921, 8.0F, 3.0F, 3, 25, () -> {
		return Ingredient.ofItems(Items.AMETHYST_SHARD);
	}),
	THUWUMIUM(512, 7.0F, 2.5F, 2, 25, () -> {
		return Ingredient.ofItems(AlchemyItems.THUWUMIUM_INGOT);
	});

	private final int durability;
	private final float miningSpeedMultiplier;
	private final float attackDamage;
	private final int miningLevel;
	private final int enchantability;
	private final Lazy<Ingredient> repairIngredient;

	AlchemyToolMaterials(int durability, float miningSpeedMultiplier, float attackDamage, int miningLevel, int enchantability, Supplier<Ingredient> repairIngredient)
	{
		this.durability = durability;
		this.miningSpeedMultiplier = miningSpeedMultiplier;
		this.attackDamage = attackDamage;
		this.miningLevel = miningLevel;
		this.enchantability = enchantability;
		this.repairIngredient = new Lazy<>(repairIngredient);
	}

	@Override
	public int getDurability()
	{
		return durability;
	}

	@Override
	public float getMiningSpeedMultiplier()
	{
		return miningSpeedMultiplier;
	}

	@Override
	public float getAttackDamage()
	{
		return attackDamage;
	}

	@Override
	public int getMiningLevel()
	{
		return miningLevel;
	}

	@Override
	public int getEnchantability()
	{
		return enchantability;
	}

	@Override
	public Ingredient getRepairIngredient()
	{
		return repairIngredient.get();
	}
}
