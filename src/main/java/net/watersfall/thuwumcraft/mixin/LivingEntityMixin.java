package net.watersfall.thuwumcraft.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.hooks.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	@Shadow public abstract boolean damage(DamageSource source, float amount);

	@Shadow public abstract Iterable<ItemStack> getArmorItems();

	public LivingEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void thuwumcraft$tick(CallbackInfo info)
	{
		Hooks.livingEntityTick((LivingEntity)(Object)this);
	}

	@Inject(method = "onStatusEffectApplied", at = @At("HEAD"))
	public void thuwumcraft$addStatusEffectModifier(StatusEffectInstance effect, Entity entity, CallbackInfo info)
	{
		Hooks.livingEntityAddStatusEffect((LivingEntity)(Object)this, effect);
	}

	@Inject(method = "onStatusEffectRemoved", at = @At("HEAD"))
	public void thuwumcraft$removeStatusEffectModifier(StatusEffectInstance effect, CallbackInfo info)
	{
		Hooks.livingEntityRemoveStatusEffect((LivingEntity)(Object)this, effect);
	}

	@Inject(method = "getEquipment", at = @At(value = "JUMP", opcode = 199), locals = LocalCapture.CAPTURE_FAILHARD)
	public void thuwumcraft$checkRunedShield(
			CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> info,
			Map<EquipmentSlot, ItemStack> map,
			EquipmentSlot[] slots,
			int size,
			int index,
			EquipmentSlot slot,
			ItemStack currentStack,
			ItemStack newStack)
	{
		Hooks.livingEntityOnEquipmentChange((LivingEntity)(Object)this, slot, currentStack, newStack);
	}

	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void thuwumcraft$addMagicResistanceAttribute(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info)
	{
		Hooks.livingEntityAddAttributeModifiers(info);
	}

	@ModifyVariable(
			method = "applyDamage",
			at = @At(
					value = "INVOKE",
					target = "net/minecraft/entity/LivingEntity.isInvulnerableTo(Lnet/minecraft/entity/damage/DamageSource;)Z",
					shift = At.Shift.BEFORE
			)
	)
	public float thuwumcraft$modifyDamage(float amount, DamageSource source)
	{
		return Hooks.livingEntityApplyDamageModifiers((LivingEntity)(Object)this, source, amount);
	}
}