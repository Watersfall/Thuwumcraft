package net.watersfall.thuwumcraft.entity.golem.goal;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.item.ItemStack;
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
			Path path = golem.getNavigation().findPathTo(item, 32);
			return path != null && path.reachesTarget();
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
					if(hand.getCount() >= 16 || hand.getCount() >= hand.getMaxCount())
					{
						break;
					}
					ItemStack stack = entity.getStack();
					if(hand.isEmpty())
					{
						golem.setStackInHand(Hand.MAIN_HAND, stack);
						entity.setStack(ItemStack.EMPTY);
					}
					else
					{
						int original = hand.getCount();
						int count = Math.min(16, hand.getCount() + stack.getCount());
						hand.setCount(count);
						stack.decrement(count - original);
						entity.setStack(stack);
					}
				}
			}
		}
	}
}
