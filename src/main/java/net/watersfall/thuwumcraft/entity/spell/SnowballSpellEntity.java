package net.watersfall.thuwumcraft.entity.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class SnowballSpellEntity extends SnowballEntity
{
	private boolean icy;

	public SnowballSpellEntity(EntityType<? extends SnowballEntity> entityType, World world)
	{
		super(entityType, world);
	}

	public SnowballSpellEntity(World world, LivingEntity owner)
	{
		super(world, owner);
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult)
	{
		super.onEntityHit(entityHitResult);
		Entity entity = entityHitResult.getEntity();
		int i = (icy ? 3 : 0) + (entity instanceof BlazeEntity ? 3 : 0);
		entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), i);
	}

	public void setIcy(boolean icy)
	{
		this.icy = icy;
	}

	@Override
	public Packet<?> createSpawnPacket()
	{
		Entity entity = this.getOwner();
		SnowballEntity snowball = new SnowballEntity(this.world, this.getX(), this.getY(), this.getZ());
		snowball.setVelocity(this.getVelocity());
		snowball.setId(this.getId());
		snowball.setUuid(this.getUuid());
		snowball.setOwner(this.getOwner());
		return new EntitySpawnS2CPacket(snowball, entity == null ? 0 : entity.getId());
	}
}
