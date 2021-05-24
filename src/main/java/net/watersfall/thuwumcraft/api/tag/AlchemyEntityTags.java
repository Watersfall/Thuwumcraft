package net.watersfall.thuwumcraft.api.tag;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.Tag;
import net.watersfall.thuwumcraft.AlchemyMod;

public class AlchemyEntityTags
{
	public static final Tag<EntityType<?>> DROPS_ARM;
	public static final Tag<EntityType<?>> DROPS_HEAD;
	public static final Tag<EntityType<?>> DROPS_HEART;
	public static final Tag<EntityType<?>> DROPS_LEG;
	public static final Tag<EntityType<?>> DROPS_RIBCAGE;

	static
	{
		DROPS_ARM = TagRegistry.entityType(AlchemyMod.getId("drops_arm"));
		DROPS_HEAD = TagRegistry.entityType(AlchemyMod.getId("drops_head"));
		DROPS_HEART = TagRegistry.entityType(AlchemyMod.getId("drops_heart"));
		DROPS_LEG = TagRegistry.entityType(AlchemyMod.getId("drops_leg"));
		DROPS_RIBCAGE = TagRegistry.entityType(AlchemyMod.getId("drops_ribcage"));
	}

	public static void register(){}
}
