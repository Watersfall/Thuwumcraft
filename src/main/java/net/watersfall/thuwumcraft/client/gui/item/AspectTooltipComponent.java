package net.watersfall.thuwumcraft.client.gui.item;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Matrix4f;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.client.item.AspectTooltipData;

import java.util.List;

public class AspectTooltipComponent implements TooltipComponent
{
	private List<AspectStack> aspects;

	public AspectTooltipComponent(AspectTooltipData data)
	{
		this.aspects = data.getAspects();
	}

	@Override
	public int getHeight()
	{
		return 20;
	}

	@Override
	public int getWidth(TextRenderer textRenderer)
	{
		return 24 * this.aspects.size();
	}

	@Override
	public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix4f, VertexConsumerProvider.Immediate immediate)
	{

	}

	@Override
	public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z)
	{
		for(int i = 0; i  < aspects.size(); i++)
		{
			ItemStack stack = aspects.get(i).getAspect().getItem().getDefaultStack();
			itemRenderer.renderInGuiWithOverrides(stack, x + (24 * i) + 1, y + 1, 0);
			itemRenderer.renderGuiItemOverlay(textRenderer, stack, x + (24 * i) + 1, y + 1);
			if(this.aspects.get(i).getCount() > 1)
			{
				matrices.push();
				matrices.translate(0, 0, 600);
				String string = String.valueOf(this.aspects.get(i).getCount());
				VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
				textRenderer.draw(
						string,
						(float)(x + 19 - 2 - textRenderer.getWidth(string) + (24 * i)),
						(float)(y + 6 + 3),
						16777215,
						true,
						matrices.peek().getPositionMatrix().copy(),
						immediate,
						false,
						0,
						15728880);
				immediate.draw();
				matrices.pop();
			}
		}
	}
}
