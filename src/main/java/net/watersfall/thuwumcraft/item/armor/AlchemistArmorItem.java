package net.watersfall.thuwumcraft.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.watersfall.thuwumcraft.registry.ThuwumcraftAttributes;

import java.util.UUID;

public class AlchemistArmorItem extends ArmorItem
{
	private static final UUID[] MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

	private final float resistance;
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributes;

	public AlchemistArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings, float resistance)
	{
		super(material, slot, settings);
		Multimap<EntityAttribute, EntityAttributeModifier> attributes = super.getAttributeModifiers(slot);
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		attributes.entries().forEach(builder::put);
		UUID uuid = MODIFIERS[slot.getEntitySlotId()];
		builder.put(ThuwumcraftAttributes.MAGIC_RESISTANCE, new EntityAttributeModifier(uuid, "Magic Resistance", resistance * 100, EntityAttributeModifier.Operation.ADDITION));
		this.attributes = builder.build();
		this.resistance = resistance;
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot)
	{
		return slot == this.slot ? attributes : super.getAttributeModifiers(slot);
	}

	public float getResistance()
	{
		return this.resistance;
	}
}
