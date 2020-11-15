package com.watersfall.poisonedweapons.mixin;

import com.watersfall.poisonedweapons.effect.AlchemyModStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	private boolean hasRepulsion = false;
	private boolean hasAttraction = false;

	@Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

	public LivingEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	private boolean test(ArrowEntity entity)
	{
		return entity != null && entity.getOwner() != null && this.getEntityId() != entity.getOwner().getEntityId();
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo info)
	{
		if(!world.isClient)
		{
			if(this.hasStatusEffect(AlchemyModStatusEffects.PROJECTILE_SHIELD))
			{
				List<ArrowEntity> entities = world.getEntitiesByType(EntityType.ARROW, this.getBoundingBox().expand(3), this::test);
				for(int i = 0; i < entities.size(); i++)
				{
					Vec3d velocity = entities.get(i).getVelocity();
					entities.get(i).setOwner(this);
					entities.get(i).setVelocity(velocity.x * -0.1, velocity.y * -0.1, velocity.z * -0.1);
				}
			}
			else if(this.hasStatusEffect(AlchemyModStatusEffects.PROJECTILE_ATTRACTION))
			{
				List<ArrowEntity> entities = world.getEntitiesByType(EntityType.ARROW, this.getBoundingBox().expand(3), this::test);
				for(int i = 0; i < entities.size(); i++)
				{
					ArrowEntity entity = entities.get(i);
					Vec3d vec = entity.getPos().subtract(this.getPos()).normalize();
					Vec3d vel = entity.getVelocity();
					float speed = (float) Math.sqrt(vel.x * vel.x + vel.y * vel.y + vel.z * vel.z);
					entities.get(i).setVelocity(-vec.x, -vec.y, -vec.z, speed, 0);
				}
			}
		}
	}

	@Inject(method = "onStatusEffectApplied", at = @At("HEAD"))
	public void addStatusEffectModifier(StatusEffectInstance effect, CallbackInfo info)
	{
		if(world instanceof ServerWorld)
		{
			ServerWorld serverWorld = (ServerWorld)world;
			ServerChunkManager manager = serverWorld.getChunkManager();
			manager.sendToNearbyPlayers(this, new EntityStatusEffectS2CPacket(this.getEntityId(), effect));
		}
	}

	@Inject(method = "onStatusEffectRemoved", at = @At("HEAD"))
	public void removeStatusEffectModifier(StatusEffectInstance effect, CallbackInfo info)
	{
		if(world instanceof ServerWorld)
		{
			ServerWorld serverWorld = (ServerWorld)world;
			ServerChunkManager manager = serverWorld.getChunkManager();
			manager.sendToNearbyPlayers(this, new RemoveEntityStatusEffectS2CPacket(this.getEntityId(), effect.getEffectType()));
		}
	}
}