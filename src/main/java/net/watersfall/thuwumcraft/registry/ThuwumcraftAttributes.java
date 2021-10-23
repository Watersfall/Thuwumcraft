package net.watersfall.thuwumcraft.registry;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;

public class ThuwumcraftAttributes
{
	public static EntityAttribute MAGIC_RESISTANCE;

	static
	{
		MAGIC_RESISTANCE = Registry.register(
				Registry.ATTRIBUTE,
				Thuwumcraft.getId("generic.magic_resistance"),
				new ClampedEntityAttribute("attribute.name.generic.magic_resistance", 0.0F, 0.0F, 100.0F));
	}
}
