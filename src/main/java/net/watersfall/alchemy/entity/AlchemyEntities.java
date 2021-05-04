package net.watersfall.alchemy.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;

public class AlchemyEntities
{
	public static final EntityType<IceProjectileEntity> ICE_PROJECTILE;

	static
	{
		ICE_PROJECTILE = Registry.register(
				Registry.ENTITY_TYPE,
				AlchemyMod.getId("ice_projectile_entity"),
				FabricEntityTypeBuilder.<IceProjectileEntity>create(SpawnGroup.MISC, IceProjectileEntity::new)
					.dimensions(EntityDimensions.fixed(0.25F, 0.25F))
					.trackRangeBlocks(4)
					.trackedUpdateRate(10)
					.build()
		);
	}
}
