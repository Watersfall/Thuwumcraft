package com.watersfall.poisonedweapons.event;

import com.watersfall.poisonedweapons.api.Poisonable;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ApplyAffectEvent implements AttackEntityCallback
{
	@Override
	public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult entityHitResult)
	{
		if(entity instanceof LivingEntity)
		{
			if(player.getMainHandStack() != null)
			{
				if(player.getMainHandStack().getItem() instanceof Poisonable)
				{
					((Poisonable) player.getMainHandStack().getItem()).applyEffect(player, entity);
				}
			}
		}
		return ActionResult.PASS;
	}
}
