package net.watersfall.thuwumcraft.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.entity.golem.GolemEntity;
import net.watersfall.thuwumcraft.entity.spell.*;

public class ThuwumcraftEntities
{
	public static EntityType<IceProjectileEntity> ICE_PROJECTILE;
	public static EntityType<WaterEntity> WATER_PROJECTILE;
	public static EntityType<FireEntity> FIRE_PROJECTILE;
	public static EntityType<SandEntity> SAND_PROJECTILE;
	public static EntityType<WindEntity> WIND_PROJECTILE;
	public static EntityType<GolemEntity> GOLEM;

	public static void register()
	{
		ICE_PROJECTILE = Registry.register(
				Registry.ENTITY_TYPE,
				Thuwumcraft.getId("ice_projectile_entity"),
				FabricEntityTypeBuilder.<IceProjectileEntity>create(SpawnGroup.MISC, IceProjectileEntity::new)
					.dimensions(EntityDimensions.fixed(0.25F, 0.25F))
					.trackRangeBlocks(4)
					.trackedUpdateRate(10)
					.build()
		);
		WATER_PROJECTILE = Registry.register(
				Registry.ENTITY_TYPE,
				Thuwumcraft.getId("water_entity"),
				FabricEntityTypeBuilder.<WaterEntity>create(SpawnGroup.MISC, WaterEntity::new)
						.dimensions(EntityDimensions.fixed(3.0F, 3.0F))
						.trackedUpdateRate(10)
						.trackRangeBlocks(4)
						.build()
		);
		FIRE_PROJECTILE = Registry.register(
				Registry.ENTITY_TYPE,
				Thuwumcraft.getId("fire_entity"),
				FabricEntityTypeBuilder.<FireEntity>create(SpawnGroup.MISC, FireEntity::new)
						.dimensions(EntityDimensions.fixed(3.0F, 3.0F))
						.trackedUpdateRate(10)
						.trackRangeBlocks(4)
						.build()
		);
		SAND_PROJECTILE = Registry.register(
				Registry.ENTITY_TYPE,
				Thuwumcraft.getId("sand_entity"),
				FabricEntityTypeBuilder.<SandEntity>create(SpawnGroup.MISC, SandEntity::new)
						.dimensions(EntityDimensions.fixed(2, 2))
						.trackedUpdateRate(10)
						.trackRangeBlocks(4)
						.build()
		);
		WIND_PROJECTILE = Registry.register(
				Registry.ENTITY_TYPE,
				Thuwumcraft.getId("wind_entity"),
				FabricEntityTypeBuilder.<WindEntity>create(SpawnGroup.MISC, WindEntity::new)
						.dimensions(EntityDimensions.fixed(0.5F, 0.5F))
						.trackedUpdateRate(10)
						.trackRangeBlocks(4)
						.build()
		);
		GOLEM = Registry.register(
				Registry.ENTITY_TYPE,
				Thuwumcraft.getId("golem"),
				FabricEntityTypeBuilder.<GolemEntity>create(SpawnGroup.MISC, GolemEntity::new)
						.dimensions(EntityDimensions.fixed(0.65F, 1))
						.build()
		);
	}
}
