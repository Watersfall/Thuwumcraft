package net.watersfall.alchemy.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.watersfall.alchemy.abilities.entity.RunedShieldAbilityEntity;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.effect.AlchemyStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	@Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

	@Shadow public abstract boolean damage(DamageSource source, float amount);

	public LivingEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	private boolean test(ArrowEntity entity)
	{
		return entity != null && entity.getOwner() != null && this.getId() != entity.getOwner().getId();
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo info)
	{
		if(!world.isClient)
		{
			if(this.hasStatusEffect(AlchemyStatusEffects.PROJECTILE_SHIELD))
			{
				List<ArrowEntity> entities = world.getEntitiesByType(EntityType.ARROW, this.getBoundingBox().expand(3), this::test);
				for(int i = 0; i < entities.size(); i++)
				{
					Vec3d velocity = entities.get(i).getVelocity();
					entities.get(i).setOwner(this);
					entities.get(i).setVelocity(velocity.x * -0.1, velocity.y * -0.1, velocity.z * -0.1);
				}
			}
			else if(this.hasStatusEffect(AlchemyStatusEffects.PROJECTILE_ATTRACTION))
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
			manager.sendToNearbyPlayers(this, new EntityStatusEffectS2CPacket(this.getId(), effect));
		}
	}

	@Inject(method = "onStatusEffectRemoved", at = @At("HEAD"))
	public void removeStatusEffectModifier(StatusEffectInstance effect, CallbackInfo info)
	{
		if(world instanceof ServerWorld)
		{
			ServerWorld serverWorld = (ServerWorld)world;
			ServerChunkManager manager = serverWorld.getChunkManager();
			manager.sendToNearbyPlayers(this, new RemoveEntityStatusEffectS2CPacket(this.getId(), effect.getEffectType()));
		}
	}

	@Inject(method = "method_30129", at = @At(value = "JUMP", opcode = 199), locals = LocalCapture.CAPTURE_FAILHARD)
	public void checkRunedShield(
			CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> info,
			Map<EquipmentSlot, ItemStack> map,
			EquipmentSlot[] slots,
			int size,
			int index,
			EquipmentSlot slot,
			ItemStack currentStack,
			ItemStack newStack)
	{
		LivingEntity entity = (LivingEntity)(Object)this;
		if(slot == EquipmentSlot.CHEST)
		{
			if(newStack.getItem() == Items.NETHERITE_CHESTPLATE)
			{
				if(entity instanceof AbilityProvider)
				{
					AbilityProvider<Entity> provider = (AbilityProvider<Entity>)entity;
					Optional<Ability<Entity>> optional = provider.getAbility(RunedShieldAbilityEntity.ID);
					if(!optional.isPresent())
					{
						provider.addAbility(new RunedShieldAbilityEntity());
						provider.sync(entity);
					}
				}
			}
			else if(currentStack.getItem() == Items.NETHERITE_CHESTPLATE)
			{
				AbilityProvider<Entity> provider = (AbilityProvider<Entity>)entity;
				provider.removeAbility(RunedShieldAbilityEntity.ID);
				provider.sync(entity);
			}
		}
	}

	@ModifyVariable(
			method = "applyDamage",
			at = @At(
					value = "INVOKE",
					target = "net/minecraft/entity/LivingEntity.isInvulnerableTo(Lnet/minecraft/entity/damage/DamageSource;)Z",
					shift = At.Shift.BEFORE
			)
	)
	public float modifyDamage(float amount, DamageSource source)
	{
		if(source.isProjectile())
		{
			if(this.hasStatusEffect(AlchemyStatusEffects.PROJECTILE_RESISTANCE))
			{
				amount = amount * 0.5F;
			}
			if(this.hasStatusEffect(AlchemyStatusEffects.PROJECTILE_WEAKNESS))
			{
				amount = amount * 2.0F;
			}
		}
		return amount;
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void writeCustomData(CompoundTag tag, CallbackInfo info)
	{
		AbilityProvider<Entity> provider = (AbilityProvider<Entity>)(Entity)(Object)this;
		provider.toNbt(tag);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readCustomData(CompoundTag tag, CallbackInfo info)
	{
		AbilityProvider<Entity> provider = (AbilityProvider<Entity>)(Entity)(Object)this;
		provider.fromNbt(tag);
	}
}