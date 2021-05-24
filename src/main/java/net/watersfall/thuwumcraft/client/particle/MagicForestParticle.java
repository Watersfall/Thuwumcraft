package net.watersfall.thuwumcraft.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class MagicForestParticle extends SpriteBillboardParticle
{
	private final SpriteProvider provider;
	public MagicForestParticle(SpriteProvider provider, ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ)
	{
		super(clientWorld, x, y, z, velX, velY, velZ);
		this.provider = provider;
	}

	@Override
	public void tick()
	{
		super.tick();
		this.setSpriteForAge(provider);
	}

	@Override
	protected int getBrightness(float tint)
	{
		return 15728880;
	}

	@Override
	public ParticleTextureSheet getType()
	{
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
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
			MagicForestParticle particle = new MagicForestParticle(spriteProvider, world, x, y, z, velX, velY, velZ);
			particle.setMaxAge(20 + (int)(Math.random() * 11));
			particle.setColor((float)Math.random(), (float)Math.random(), (float)Math.random());
			particle.setSpriteForAge(spriteProvider);
			return particle;
		}
	}
}
