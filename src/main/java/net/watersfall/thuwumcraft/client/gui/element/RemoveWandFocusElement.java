package net.watersfall.thuwumcraft.client.gui.element;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.watersfall.thuwumcraft.Thuwumcraft;

import java.util.List;

public class RemoveWandFocusElement extends ItemElement
{
	private static final List<Text> tooltip = List.of(new TranslatableText("item.thuwumcraft.wand.spell.remove"));

	public RemoveWandFocusElement(int x, int y)
	{
		super(new ItemStack[]{new ItemStack(Items.BARRIER)}, x, y);
	}

	@Override
	public List<Text> getTooltip(int mouseX, int mouseY)
	{
		return tooltip;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if(isMouseOver(mouseX, mouseY))
		{
			PacketByteBuf buf = PacketByteBufs.create();
			ClientPlayNetworking.send(Thuwumcraft.getId("focus_remove_click"), buf);
			return true;
		}
		return true;
	}
}
