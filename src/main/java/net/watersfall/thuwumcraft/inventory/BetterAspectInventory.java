package net.watersfall.thuwumcraft.inventory;

import net.watersfall.thuwumcraft.api.aspect.Aspect;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;

import java.util.Map;

public interface BetterAspectInventory
{
	Map<Aspect, AspectStack> getAspects();

	default AspectStack getAspect(Aspect aspect)
	{
		return getAspects().getOrDefault(aspect, AspectStack.EMPTY);
	}

	default int getAspectSize()
	{
		return getAspects().size();
	}

	int getMaxSize();

	default AspectStack setAspect(AspectStack stack)
	{
		AspectStack old = getAspect(stack.getAspect());
		getAspects().put(stack.getAspect(), stack);
		return old;
	}

	default boolean isAspectsEmpty()
	{
		return getAspects().values().stream().allMatch(AspectStack::isEmpty);
	}

	default AspectStack removeAspect(Aspect aspect)
	{
		AspectStack stack = getAspects().remove(aspect);
		return stack == null ? AspectStack.EMPTY : stack;
	}

	default AspectStack removeAspect(Aspect aspect, int count)
	{
		AspectStack current = getAspect(aspect);
		if(current.getCount() <= count)
		{
			return this.removeAspect(aspect);
		}
		else
		{
			current.decrement(count);
			return new AspectStack(aspect, count);
		}
	}
}
