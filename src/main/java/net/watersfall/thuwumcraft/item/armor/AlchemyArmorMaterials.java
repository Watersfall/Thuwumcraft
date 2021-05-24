package net.watersfall.thuwumcraft.item.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Lazy;
import net.watersfall.thuwumcraft.item.AlchemyItems;

import java.util.function.Supplier;

public enum AlchemyArmorMaterials implements ArmorMaterial
{
	GOGGLES("goggles", 15, new int[]{1,1,1,1}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0, 0, () -> Ingredient.ofItems(Items.GOLD_INGOT)),
	THUWUMIUM("thuwumium", 20, new int[]{2,5,6,3}, 25, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0, 0, () -> Ingredient.ofItems(AlchemyItems.THUWUMIUM_INGOT)),
	FORTRESS("fortress", 35, new int[]{4, 6, 7, 3}, 30, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 3, 0, () -> Ingredient.ofItems(AlchemyItems.THUWUMIUM_INGOT)),
	MAGIC("magic", 15, new int[]{2, 3, 5, 3}, 45, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1, 0, () -> Ingredient.ofItems(Items.LEATHER));

	private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
	private final String name;
	private final int durabilityMultiplier;
	private final int[] protectionAmounts;
	private final int enchantability;
	private final SoundEvent equipSound;
	private final float toughness;
	private final float knockbackResistance;
	private final Lazy<Ingredient> repairIngredientSupplier;

	AlchemyArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredientSupplier)
	{
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.protectionAmounts = protectionAmounts;
		this.enchantability = enchantability;
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredientSupplier = new Lazy(repairIngredientSupplier);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public int getDurability(EquipmentSlot slot)
	{
		return durabilityMultiplier * BASE_DURABILITY[slot.getEntitySlotId()];
	}

	@Override
	public int getProtectionAmount(EquipmentSlot slot)
	{
		return protectionAmounts[slot.getEntitySlotId()];
	}

	@Override
	public int getEnchantability()
	{
		return enchantability;
	}

	@Override
	public SoundEvent getEquipSound()
	{
		return equipSound;
	}

	@Override
	public float getToughness()
	{
		return toughness;
	}

	@Override
	public float getKnockbackResistance()
	{
		return knockbackResistance;
	}

	@Override
	public Ingredient getRepairIngredient()
	{
		return repairIngredientSupplier.get();
	}
}
