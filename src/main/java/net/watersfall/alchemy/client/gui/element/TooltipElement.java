package net.watersfall.alchemy.client.gui.element;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public interface TooltipElement
{
	List<Text> getTooltip(int mouseX, int mouseY);
}
