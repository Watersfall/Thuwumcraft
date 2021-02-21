package net.watersfall.alchemy.client.item;

import net.minecraft.client.item.TooltipData;
import net.watersfall.alchemy.api.aspect.AspectStack;

public class GlassPhialTooltipData implements TooltipData
{
	private AspectStack stack;

	public GlassPhialTooltipData(AspectStack stack)
	{
		this.stack = stack;
	}

	public AspectStack getStack()
	{
		return this.stack;
	}
}
