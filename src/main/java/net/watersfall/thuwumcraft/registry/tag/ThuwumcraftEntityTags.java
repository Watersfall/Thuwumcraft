package net.watersfall.thuwumcraft.registry.tag;

import net.minecraft.entity.EntityType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;

public class ThuwumcraftEntityTags
{
	public static final TagKey<EntityType<?>> DROPS_ARM;
	public static final TagKey<EntityType<?>> DROPS_HEAD;
	public static final TagKey<EntityType<?>> DROPS_HEART;
	public static final TagKey<EntityType<?>> DROPS_LEG;
	public static final TagKey<EntityType<?>> DROPS_RIBCAGE;

	static
	{
		DROPS_ARM = TagKey.of(Registry.ENTITY_TYPE_KEY, Thuwumcraft.getId("drops_arm"));
		DROPS_HEAD = TagKey.of(Registry.ENTITY_TYPE_KEY, Thuwumcraft.getId("drops_head"));
		DROPS_HEART = TagKey.of(Registry.ENTITY_TYPE_KEY, Thuwumcraft.getId("drops_heart"));
		DROPS_LEG = TagKey.of(Registry.ENTITY_TYPE_KEY, Thuwumcraft.getId("drops_leg"));
		DROPS_RIBCAGE = TagKey.of(Registry.ENTITY_TYPE_KEY, Thuwumcraft.getId("drops_ribcage"));
	}

	public static void register(){}
}
