package net.watersfall.alchemy.entity;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;

public class AlchemyAttributes
{
	public static final EntityAttribute MAGIC_RESISTANCE;

	static
	{
		MAGIC_RESISTANCE = Registry.register(
				Registry.ATTRIBUTE,
				AlchemyMod.getId("generic.magic_resistance"),
				new ClampedEntityAttribute("attribute.name.generic.magic_resistance", 0.0F, 0.0F, 100.0F));
	}
}
