package net.watersfall.thuwumcraft.client.gui.element;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.watersfall.thuwumcraft.AlchemyMod;

import java.util.List;

public class WandFocusElement implements Element, Drawable, TooltipElement
{
	private int x;
	private int y;
	private ItemElement element;
	private int index;

	public WandFocusElement(ItemStack stack, int x, int y, int index)
	{
		this.x = x;
		this.y = y;
		this.element = new ItemElement(new ItemStack[]{stack}, x, y, x, y, this);
		this.index = index;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		this.element.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		return mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16;
	}

	@Override
	public List<Text> getTooltip(int mouseX, int mouseY)
	{
		return element.getTooltip(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if(isMouseOver(mouseX, mouseY))
		{
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeInt(index);
			ClientPlayNetworking.send(AlchemyMod.getId("focus_click"), buf);
			return true;
		}
		return false;
	}
}
