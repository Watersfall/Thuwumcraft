package net.watersfall.alchemy.client.item;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.watersfall.alchemy.api.aspect.AspectStack;
import net.watersfall.alchemy.api.client.item.CustomTooltipDataComponent;
import net.watersfall.alchemy.client.gui.item.AspectTooltipComponent;

import java.util.List;

public class AspectTooltipData implements TooltipData, CustomTooltipDataComponent
{
	private final List<AspectStack> aspects;
	private final AspectTooltipComponent component;

	public AspectTooltipData(List<AspectStack> aspects)
	{
		this.aspects = aspects;
		this.component = new AspectTooltipComponent(this);
	}

	public List<AspectStack> getAspects()
	{
		return this.aspects;
	}

	@Override
	public TooltipComponent getComponent()
	{
		return this.component;
	}
}
