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
	public static final EntityType<WaterEntity> WATER_ENTITY;
	public static final EntityType<FireEntity> FIRE_ENTITY;
	public static final EntityType<SandEntity> SAND_ENTITY;

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
		WATER_ENTITY = Registry.register(
				Registry.ENTITY_TYPE,
				AlchemyMod.getId("water_entity"),
				FabricEntityTypeBuilder.<WaterEntity>create(SpawnGroup.MISC, WaterEntity::new)
						.dimensions(EntityDimensions.fixed(3.0F, 3.0F))
						.trackedUpdateRate(10)
						.trackRangeBlocks(4)
						.build()
		);
		FIRE_ENTITY = Registry.register(
				Registry.ENTITY_TYPE,
				AlchemyMod.getId("fire_entity"),
				FabricEntityTypeBuilder.<FireEntity>create(SpawnGroup.MISC, FireEntity::new)
						.dimensions(EntityDimensions.fixed(3.0F, 3.0F))
						.trackedUpdateRate(10)
						.trackRangeBlocks(4)
						.build()
		);
		SAND_ENTITY = Registry.register(
				Registry.ENTITY_TYPE,
				AlchemyMod.getId("sand_entity"),
				FabricEntityTypeBuilder.<SandEntity>create(SpawnGroup.MISC, SandEntity::new)
						.dimensions(EntityDimensions.fixed(2, 2))
						.trackedUpdateRate(10)
						.trackRangeBlocks(4)
						.build()
		);
	}
}
