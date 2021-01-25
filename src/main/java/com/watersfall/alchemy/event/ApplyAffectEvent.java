package com.watersfall.alchemy.event;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ApplyAffectEvent implements AttackEntityCallback
{
	@Override
	public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult entityHitResult)
	{
		if(entity instanceof LivingEntity)
		{
			if(player.getMainHandStack() != null)
			{
				if(player.getMainHandStack().getTag() != null)
				{
					ItemStack stack = player.getMainHandStack();
					if(stack.getTag().contains("uses"))
					{
						if(stack.getTag().getInt("uses") > 0)
						{
							List<StatusEffectInstance> effects = PotionUtil.getCustomPotionEffects(stack);
							if(effects.size() > 0)
							{
								effects.forEach(((LivingEntity)entity)::addStatusEffect);
								stack.getTag().putInt("uses", stack.getTag().getInt("uses") - 1);
							}
						}
					}
				}
			}
		}
		return ActionResult.PASS;
	}
}
