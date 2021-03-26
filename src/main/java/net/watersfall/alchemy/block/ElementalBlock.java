package net.watersfall.alchemy.block;

import net.minecraft.block.AmethystBlock;
import net.watersfall.alchemy.api.aspect.Aspect;

public class ElementalBlock extends AmethystBlock
{
	private final Aspect aspect;

	public ElementalBlock(Settings settings, Aspect aspect)
	{
		super(settings);
		this.aspect = aspect;
	}
}
