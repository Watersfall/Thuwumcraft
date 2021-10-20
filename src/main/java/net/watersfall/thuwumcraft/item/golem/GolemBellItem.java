package net.watersfall.thuwumcraft.item.golem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.watersfall.thuwumcraft.api.item.golem.RendersGolemOutlines;
import net.watersfall.thuwumcraft.entity.golem.GolemEntity;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;
import net.watersfall.wet.api.item.BeforeActions;

public class GolemBellItem extends Item implements BeforeActions, RendersGolemOutlines
{
	public GolemBellItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand)
	{
		if(entity instanceof GolemEntity golem)
		{
			if(!golem.getMainHandStack().isEmpty() && !user.isSneaking())
			{
				ItemStack handStack = golem.getMainHandStack();
				golem.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
				ItemScatterer.spawn(entity.world, entity.getX(), entity.getY(), entity.getZ(), handStack);
			}
			else if(golem.getSeal().isEmpty() || user.isSneaking())
			{
				if(!entity.world.isClient)
				{
					ItemStack seal = golem.getSeal();
					entity.remove(Entity.RemovalReason.DISCARDED);
					if(!user.isCreative())
					{
						ItemScatterer.spawn(entity.world, entity.getBlockPos(), DefaultedList.copyOf(ItemStack.EMPTY, ThuwumcraftItems.GOLEM.getDefaultStack(), seal));
					}
				}
			}
			else
			{
				if(!entity.world.isClient)
				{
					ItemStack seal = golem.getSeal();
					golem.setSeal(ItemStack.EMPTY);
					if(!user.isCreative())
					{
						ItemScatterer.spawn(entity.world, entity.getX(), entity.getY(), entity.getZ(), seal);
					}
				}
			}
			entity.world.playSound(user, entity.getBlockPos(), SoundEvents.ENTITY_GENERIC_BURN, SoundCategory.NEUTRAL, 0.5F, 1.5F);
			return ActionResult.success(entity.world.isClient);
		}
		return super.useOnEntity(stack, user, entity, hand);
	}

	@Override
	public boolean beforeHit(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		return BeforeActions.super.beforeHit(stack, target, attacker);
	}
}
