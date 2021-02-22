package net.watersfall.alchemy.client.item;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.watersfall.alchemy.api.aspect.AspectStack;
import net.watersfall.alchemy.api.client.item.CustomTooltipDataComponent;
import net.watersfall.alchemy.client.gui.item.GlassPhialTooltipComponent;

public class GlassPhialTooltipData implements TooltipData, CustomTooltipDataComponent
{
	private final AspectStack stack;
	private final GlassPhialTooltipComponent component;

	public GlassPhialTooltipData(AspectStack stack)
	{
		this.stack = stack;
		this.component = new GlassPhialTooltipComponent(this);
	}

	public AspectStack getStack()
	{
		return this.stack;
	}

	@Override
	public TooltipComponent getComponent()
	{
		return this.component;
	}
}
