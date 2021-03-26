package net.watersfall.alchemy.block;

import net.minecraft.block.AmethystClusterBlock;
import net.watersfall.alchemy.api.aspect.Aspect;

public class ElementalClusterBlock extends AmethystClusterBlock
{
	private final Aspect aspect;

	public ElementalClusterBlock(int height, int xzOffset, Settings settings, Aspect aspect)
	{
		super(height, xzOffset, settings);
		this.aspect = aspect;
	}

	public Aspect getAspect()
	{
		return this.aspect;
	}
}
