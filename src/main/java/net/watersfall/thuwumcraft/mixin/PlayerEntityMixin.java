package net.watersfall.thuwumcraft.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.effect.ThuwumcraftStatusEffects;
import net.watersfall.thuwumcraft.entity.ThuwumcraftAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity
{
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@ModifyVariable(
			method = "applyDamage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/player/PlayerEntity;isInvulnerableTo(Lnet/minecraft/entity/damage/DamageSource;)Z",
					shift = At.Shift.BEFORE
			)
	)
	public float thuwumcraft$modifyDamage(float amount, DamageSource source)
	{
		if(source.isProjectile())
		{
			if(this.hasStatusEffect(ThuwumcraftStatusEffects.PROJECTILE_RESISTANCE))
			{
				amount = amount * 0.5F;
			}
			if(this.hasStatusEffect(ThuwumcraftStatusEffects.PROJECTILE_WEAKNESS))
			{
				amount = amount * 2.0F;
			}
		}
		if(source.isMagic())
		{
			double resistance = this.getAttributeValue(ThuwumcraftAttributes.MAGIC_RESISTANCE) / 100;
			amount = amount * (float)(1 - resistance);
		}
		return amount;
	}
}
