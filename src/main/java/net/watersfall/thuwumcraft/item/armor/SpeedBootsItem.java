package net.watersfall.thuwumcraft.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;

import java.util.UUID;

public class SpeedBootsItem extends ArmorItem
{
	private static final UUID[] MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

	private final float boost;
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributes;

	public SpeedBootsItem(ArmorMaterial material, float boost)
	{
		super(material, EquipmentSlot.FEET, new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP).maxCount(1));
		Multimap<EntityAttribute, EntityAttributeModifier> attributes = super.getAttributeModifiers(slot);
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		attributes.entries().forEach(builder::put);
		UUID uuid = MODIFIERS[slot.getEntitySlotId()];
		builder.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(uuid, "Speed Boost", boost, EntityAttributeModifier.Operation.MULTIPLY_BASE));
		this.attributes = builder.build();
		this.boost = boost;
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot)
	{
		return slot == this.slot ? attributes : super.getAttributeModifiers(slot);
	}

	public float getSpeedBoost()
	{
		return this.boost;
	}
}
