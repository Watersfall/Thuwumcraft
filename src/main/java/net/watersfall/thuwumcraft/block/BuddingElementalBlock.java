package net.watersfall.thuwumcraft.block;

import net.minecraft.block.AmethystBlock;
import net.watersfall.thuwumcraft.api.aspect.Aspect;

public class BuddingElementalBlock extends AmethystBlock
{
	private final Aspect aspect;

	public BuddingElementalBlock(Settings settings, Aspect aspect)
	{
		super(settings);
		this.aspect = aspect;
	}

	public Aspect getAspect()
	{
		return this.aspect;
	}
}
