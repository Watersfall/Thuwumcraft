package net.watersfall.thuwumcraft.client.gui.item;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Matrix4f;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.client.item.GlassPhialTooltipData;

public class GlassPhialTooltipComponent implements TooltipComponent
{
	private AspectStack stack;

	public GlassPhialTooltipComponent(GlassPhialTooltipData data)
	{
		this.stack = data.getStack();
	}

	@Override
	public int getHeight()
	{
		return 20;
	}

	@Override
	public int getWidth(TextRenderer textRenderer)
	{
		return textRenderer.getWidth(new LiteralText("" + this.stack.getCount()).append(new TranslatableText(this.stack.getAspect().getTranslationKey()))) + 28;
	}

	@Override
	public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix4f, VertexConsumerProvider.Immediate immediate)
	{
		textRenderer.draw(
				new LiteralText(this.stack.getCount() + " ").append(new TranslatableText(this.stack.getAspect().getTranslationKey())),
				(float)x + 20,
				(float)y + 6,
				-1,
				false,
				matrix4f,
				immediate,
				false,
				1,
				9437408
			);
	}

	@Override
	public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z)
	{
		ItemStack stack = this.stack.getAspect().getItem().getDefaultStack();
		itemRenderer.renderInGuiWithOverrides(stack, x + 1, y + 1, 0);
		itemRenderer.renderGuiItemOverlay(textRenderer, stack, x + 1, y + 1);
	}
}
