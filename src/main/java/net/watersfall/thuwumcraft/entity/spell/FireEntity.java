package net.watersfall.thuwumcraft.entity.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.registry.ThuwumcraftEntities;

public class FireEntity extends WaterEntity
{
	public FireEntity(World world, Entity owner)
	{
		super(ThuwumcraftEntities.FIRE_ENTITY, world);
		if(owner != null)
		{
			this.ownerUUID = owner.getUuid();
		}
	}

	public FireEntity(EntityType<? extends WaterEntity> type, World world)
	{
		super(type, world);
	}

	@Override
	public void tick()
	{
		this.baseTick();
		double scale = age / 20F - 1.5;
		this.particleBox = this.getBoundingBox().expand(scale);
		this.noClip = true;
		this.move(MovementType.SELF, this.getVelocity());
		for(int i = 0; i < (this.age / 10) + 1; i++)
		{
			double x = particleBox.minX + Math.random() * particleBox.getXLength();
			double y = particleBox.minY + Math.random() * particleBox.getYLength();
			double z = particleBox.minZ + Math.random() * particleBox.getZLength();
			this.world.addParticle(ParticleTypes.FLAME, x, y, z, this.getVelocity().x / 2, this.getVelocity().y / 2, this.getVelocity().z / 2);
		}
		if(this.age > 40)
		{
			this.remove(RemovalReason.DISCARDED);
		}
		if(!world.isClient)
		{
			world.getOtherEntities(this, this.getBoundingBox(), (entity) -> !entity.isSpectator() && !entity.getUuid().equals(this.ownerUUID)).forEach(entity -> {
				if(!entity.isFireImmune())
				{
					entity.setFireTicks(30);
					entity.damage(DamageSource.IN_FIRE, 2.0F);
				}
			});
		}
	}

	@Override
	public boolean doesRenderOnFire()
	{
		return false;
	}
}
