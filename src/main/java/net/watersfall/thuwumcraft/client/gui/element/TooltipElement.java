package net.watersfall.thuwumcraft.client.gui.element;

import net.minecraft.text.Text;

import java.util.List;

public interface TooltipElement
{
	List<Text> getTooltip(int mouseX, int mouseY);
}
