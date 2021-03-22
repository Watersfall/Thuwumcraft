package net.watersfall.alchemy.client.gui.element;

import net.minecraft.item.ItemStack;

public interface TooltipElement
{
	ItemStack getTooltip(int mouseX, int mouseY);
}
