package net.watersfall.thuwumcraft.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class FireParticle extends SpriteBillboardParticle
{
	public FireParticle(SpriteProvider provider, ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ)
	{
		super(clientWorld, x, y, z, velX, velY, velZ);
	}

	@Override
	public void tick()
	{
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		if (this.age++ >= this.maxAge)
		{
			this.markDead();
		}
	}

	@Override
	public ParticleTextureSheet getType()
	{
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	public static class Factory implements ParticleFactory<DefaultParticleType>
	{
		protected final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider)
		{
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld world, double x, double y, double z, double velX, double velY, double velZ)
		{
			FireParticle particle = new FireParticle(spriteProvider, world, x, y, z, velX, velY, velZ);
			particle.setMaxAge(20 + (int)(Math.random() * 11));
			particle.setSprite(spriteProvider);
			return particle;
		}
	}
}
