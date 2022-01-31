package net.watersfall.thuwumcraft.entity.spell;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.registry.ThuwumcraftEntities;

public class SandEntity extends WaterEntity
{
	private int blindnessTime = 0;

	public SandEntity(World world, Entity owner)
	{
		super(ThuwumcraftEntities.SAND_PROJECTILE, world);
		this.owner = owner;
	}

	public SandEntity(EntityType<SandEntity> type, World world)
	{
		super(type, world);
	}

	@Override
	public void tick()
	{
		this.baseTick();
		double scale = age / 10F - 1;
		this.particleBox = this.getBoundingBox().expand(scale);
		this.noClip = true;
		this.move(MovementType.SELF, this.getVelocity());
		this.addVelocity(0, -0.02, 0);
		if(world.isClient)
		{
			for(int i = 0; i < (this.age / 3) + 5; i++)
			{
				double x = particleBox.minX + Math.random() * particleBox.getXLength();
				double y = particleBox.minY + Math.random() * particleBox.getYLength();
				double z = particleBox.minZ + Math.random() * particleBox.getZLength();
				Particle particle =  MinecraftClient.getInstance().particleManager.addParticle(new BlockStateParticleEffect(ParticleTypes.FALLING_DUST, Blocks.SAND.getDefaultState()), x, y, z, this.getVelocity().x / 2, this.getVelocity().y / 2, this.getVelocity().z / 2);
				particle.setMaxAge(10);
			}
		}
		if(!world.isClient)
		{
			StatusEffectInstance effect = new StatusEffectInstance(StatusEffects.BLINDNESS, blindnessTime * 20);
			world.getOtherEntities(this, this.getBoundingBox(), (entity) -> !entity.isSpectator() && entity.getId() != owner.getId()).forEach((entity -> {
				if(entity instanceof LivingEntity)
				{
					LivingEntity living = (LivingEntity)entity;
					if(living.canHaveStatusEffect(effect))
					{
						living.addStatusEffect(effect);
					}
				}
			}));
		}
		if(this.age > 12)
		{
			this.remove(RemovalReason.DISCARDED);
		}
	}

	public void setBlindnessTime(int blindnessTime)
	{
		this.blindnessTime = blindnessTime;
	}
}
