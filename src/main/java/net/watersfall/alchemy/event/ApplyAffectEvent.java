package net.watersfall.alchemy.event;

import net.watersfall.alchemy.util.StatusEffectHelper;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
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
		if(!world.isClient)
		{
			if(entity instanceof LivingEntity)
			{
				if(player.getStackInHand(hand) != null)
				{
					if(player.getStackInHand(hand).getTag() != null)
					{
						CompoundTag tag = player.getStackInHand(hand).getTag();
						if(tag.contains(StatusEffectHelper.EFFECTS_LIST))
						{
							if(StatusEffectHelper.hasUses(tag))
							{
								List<StatusEffectInstance> effects = StatusEffectHelper.getEffectsFromTag(tag);
								if(effects.size() > 0)
								{
									effects.forEach(((LivingEntity)entity)::addStatusEffect);
								}
							}
							StatusEffectHelper.decrementUses(tag);
						}
					}
				}
			}
		}
		return ActionResult.PASS;
	}
}
