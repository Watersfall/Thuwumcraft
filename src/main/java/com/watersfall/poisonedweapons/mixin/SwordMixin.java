package com.watersfall.poisonedweapons.mixin;

import com.watersfall.poisonedweapons.api.Poisonable;
import com.watersfall.poisonedweapons.util.StatusEffectHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SwordItem.class)
public abstract class SwordMixin extends ToolItem implements Poisonable
{
	public SwordMixin(ToolMaterial material, Settings settings)
	{
		super(material, settings);
	}

	@Override
	public StatusEffect getEffect(ItemStack stack)
	{
		if(stack.getTag() != null && stack.getTag().contains("effect"))
		{
			return Registry.STATUS_EFFECT.get(Identifier.tryParse(stack.getTag().getCompound("effect").getString("id")));
		}
		return null;
	}

	@Override
	public int getDuration(ItemStack stack)
	{
		if(stack.getTag() != null && stack.getTag().contains("effect"))
		{
			return stack.getTag().getCompound("effect").getInt("duration");
		}
		return 0;
	}

	@Override
	public int getUses(ItemStack stack)
	{
		if(stack.getTag() != null && stack.getTag().contains("effect"))
		{
			return stack.getTag().getCompound("effect").getInt("uses");
		}
		return 0;
	}

	@Override
	public void applyEffect(Entity attacker, Entity target)
	{
		if(target.isAttackable())
		{
			if(!target.handleAttack(attacker))
			{
				if(target instanceof LivingEntity)
				{
					if(attacker instanceof PlayerEntity)
					{
						PlayerEntity player = (PlayerEntity)attacker;
						if(this.getUses(((PlayerEntity) attacker).getMainHandStack()) > 0)
						{
							ItemStack stack = player.getMainHandStack();
							StatusEffect effect = this.getEffect(stack);
							if(effect != null)
							{
								((LivingEntity) target).addStatusEffect(new StatusEffectInstance(this.getEffect(stack), this.getDuration(stack)));
								StatusEffectHelper.use(stack);
							}
						}
					}
				}
			}
		}
	}
}
