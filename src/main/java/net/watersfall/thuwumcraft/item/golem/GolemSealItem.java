package net.watersfall.thuwumcraft.item.golem;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.watersfall.thuwumcraft.entity.golem.GolemEntity;
import net.watersfall.thuwumcraft.entity.golem.goal.GolemGoal;

import java.util.List;

public class GolemSealItem extends Item
{
	private final List<GolemGoal> goals;

	public GolemSealItem(Settings settings, GolemGoal... goals)
	{
		super(settings);
		this.goals = ImmutableList.copyOf(goals);
	}

	public List<GolemGoal> getGoals()
	{
		return goals;
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand)
	{
		if(entity instanceof GolemEntity golem)
		{
			if(golem.getSeal().isEmpty())
			{
				golem.setSeal(stack);
			}
			return ActionResult.success(entity.world.isClient);
		}
		return super.useOnEntity(stack, user, entity, hand);
	}
}
