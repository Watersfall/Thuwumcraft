package net.watersfall.thuwumcraft.client.gui.button;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;

public class ItemStackButton extends ButtonWidget
{
	private final ItemStack stack;

	public ItemStackButton(int x, int y, int width, int height, ItemStack stack, PressAction onPress, TooltipSupplier tooltipSupplier)
	{
		super(x, y, width, height, new LiteralText(""), onPress, tooltipSupplier);
		this.stack = stack;
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		MinecraftClient client = MinecraftClient.getInstance();
		client.getItemRenderer().renderInGui(stack, x, y);
		if (this.isHovered())
		{
			this.renderTooltip(matrices, mouseX, mouseY);
		}
	}

	public ItemStack getStack()
	{
		return stack;
	}
}
