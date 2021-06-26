package net.watersfall.thuwumcraft.client.renderer.block;

import com.google.common.collect.Lists;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.watersfall.thuwumcraft.block.entity.EssentiaRefineryBlockEntity;
import net.watersfall.thuwumcraft.client.util.RenderHelper;

public class EssentiaRefineryRenderer implements BlockEntityRenderer<EssentiaRefineryBlockEntity>
{
	protected final BlockEntityRenderDispatcher dispatcher;
	protected final TextRenderer textRenderer;

	public EssentiaRefineryRenderer(BlockEntityRendererFactory.Context context)
	{
		this.dispatcher = context.getRenderDispatcher();
		this.textRenderer = context.getTextRenderer();
	}

	@Override
	public void render(EssentiaRefineryBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		if(!entity.getStack().isEmpty())
		{
			matrices.push();
			matrices.translate(1, -0.5, 0);
			RenderHelper.renderAspects(Lists.newArrayList(entity.getStack()), entity, matrices, vertexConsumers, textRenderer, dispatcher);
			matrices.pop();
		}
	}
}
