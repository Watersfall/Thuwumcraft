package com.watersfall.poisonedweapons.mixin;

import com.watersfall.poisonedweapons.api.Poisonable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class ApplyEffectMixin extends LivingEntity
{
	protected ApplyEffectMixin(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Inject(method = "attack", at = @At("TAIL"))
	public void applyEffect(Entity target, CallbackInfo callback)
	{
		if(this.getMainHandStack().getItem() instanceof Poisonable)
		{
			((Poisonable)this.getMainHandStack().getItem()).applyEffect(this, target);
		}
	}
}
