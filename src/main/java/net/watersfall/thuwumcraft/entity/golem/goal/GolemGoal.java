package net.watersfall.thuwumcraft.entity.golem.goal;

import net.minecraft.entity.ai.goal.Goal;
import net.watersfall.thuwumcraft.entity.golem.GolemEntity;

public abstract class GolemGoal extends Goal
{
	protected final GolemEntity golem;

	public GolemGoal(GolemEntity golem)
	{
		this.golem = golem;
	}

	public abstract GolemGoal create(GolemEntity entity);
}
