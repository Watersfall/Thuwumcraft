package net.watersfall.thuwumcraft.client.item;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.watersfall.thuwumcraft.abilities.item.PhialStorageAbility;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.api.client.item.CustomTooltipDataComponent;
import net.watersfall.thuwumcraft.client.gui.item.GlassPhialTooltipComponent;

public class GlassPhialTooltipData implements TooltipData, CustomTooltipDataComponent
{
	private final AspectStack stack;
	private final GlassPhialTooltipComponent component;

	public GlassPhialTooltipData(PhialStorageAbility ability)
	{
		this.stack = ability.getAspects().get(0);
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
