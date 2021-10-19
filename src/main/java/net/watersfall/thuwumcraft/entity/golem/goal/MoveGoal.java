package net.watersfall.thuwumcraft.entity.golem.goal;

import net.minecraft.util.math.BlockPos;
import net.watersfall.thuwumcraft.entity.golem.GolemEntity;

import java.util.EnumSet;

public class MoveGoal extends GolemGoal
{
	public MoveGoal(GolemEntity entity)
	{
		super(entity);
		this.setControls(EnumSet.of(Control.MOVE));
	}

	@Override
	public boolean canStart()
	{
		return true;
	}

	@Override
	public void start()
	{
		super.start();
		BlockPos pos = golem.getHome();
		golem.getNavigation().startMovingTo(pos.getX(), pos.getY(), pos.getZ(), 1);
	}

	@Override
	public void tick()
	{
		super.tick();
		BlockPos pos = golem.getHome();
		golem.getNavigation().startMovingTo(pos.getX(), pos.getY(), pos.getZ(), 1);
	}

	@Override
	public GolemGoal create(GolemEntity entity)
	{
		return new MoveGoal(entity);
	}
}
