package net.watersfall.thuwumcraft.entity.golem.goal;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.util.Hand;
import net.watersfall.thuwumcraft.entity.golem.GolemEntity;

import java.util.EnumSet;
import java.util.List;

public class PickupItemGoal extends GolemGoal
{
	public PickupItemGoal(GolemEntity entity)
	{
		super(entity);
		this.setControls(EnumSet.of(Goal.Control.MOVE));
	}

	@Override
	public GolemGoal create(GolemEntity entity)
	{
		return new PickupItemGoal(entity);
	}

	@Override
	public boolean canStart()
	{
		if(golem.getMainHandStack().isEmpty())
		{
			List<ItemEntity> items = golem.world.getEntitiesByClass(ItemEntity.class, golem.getBoundingBox().expand(16), (item) -> {
				Path path = golem.getNavigation().findPathTo(item, 32);
				return path != null && path.reachesTarget();
			});
			return !items.isEmpty();
		}
		return false;
	}

	@Override
	public void start()
	{
		List<ItemEntity> items = golem.world.getEntitiesByClass(ItemEntity.class, golem.getBoundingBox().expand(16), (item) -> {
			if(golem.getWhitelist().isEmpty() || golem.getWhitelist().isOf(item.getStack().getItem()))
			{
				Path path = golem.getNavigation().findPathTo(item, 32);
				return path != null && path.reachesTarget();
			}
			return false;
		});
		if(!items.isEmpty())
		{
			golem.getNavigation().startMovingTo(items.get(0), 1);
		}
		super.start();
	}

	@Override
	public void tick()
	{
		super.tick();
		start();
		if(golem.world.getTime() % 10 == 0)
		{
			List<ItemEntity> items = golem.world.getEntitiesByClass(ItemEntity.class, golem.getBoundingBox().expand(1.5, 1, 1.5), (item) -> {
				if(golem.getMainHandStack().isEmpty())
				{
					return true;
				}
				else if(golem.getMainHandStack().getCount() >= 16 || golem.getMainHandStack().getCount() >= golem.getMainHandStack().getMaxCount())
				{
					return false;
				}
				return ItemStack.canCombine(item.getStack(), golem.getMainHandStack());
			});
			if(!items.isEmpty())
			{
				ItemStack hand = golem.getMainHandStack();
				for(ItemEntity entity : items)
				{
					if(hand.getCount() >= 16 || hand.getCount() >= hand.getMaxCount() || (!golem.getWhitelist().isEmpty() && !entity.getStack().isOf(golem.getWhitelist().getItem())))
					{
						break;
					}
					ItemStack stack = entity.getStack();
					int count = Math.min(16, hand.getCount() + stack.getCount());
					if(hand.isEmpty())
					{
						ItemStack newStack = stack.copy();
						newStack.setCount(count);
						golem.setStackInHand(Hand.MAIN_HAND, newStack);
						stack.decrement(count);
					}
					else
					{
						int original = hand.getCount();
						hand.setCount(count);
						stack.decrement(count - original);
					}
					PlayerLookup.tracking(golem).forEach(player -> {
						player.networkHandler.sendPacket(new ItemPickupAnimationS2CPacket(entity.getId(), golem.getId(), count));
					});
					if(stack.isEmpty())
					{
						entity.discard();
					}
					else
					{
						entity.setStack(stack);
					}
					break;
				}
			}
		}
	}
}
