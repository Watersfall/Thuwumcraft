package net.watersfall.thuwumcraft.client.renderer.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.api.fluid.ColoredWaterContainer;
import net.watersfall.thuwumcraft.api.fluid.WaterContainer;
import net.watersfall.thuwumcraft.client.util.RenderHelper;

public abstract class AbstractCauldronRenderer<T extends BlockEntity> implements BlockEntityRenderer<T>
{
	private static final Sprite WATER_SPRITE = ((SpriteAtlasTexture) MinecraftClient.getInstance()
			.getTextureManager()
			.getTexture(new Identifier("minecraft", "textures/atlas/blocks.png"))).getSprite(new Identifier("block/water_still"));

	protected final BlockEntityRenderDispatcher dispatcher;
	protected final TextRenderer textRenderer;

	public AbstractCauldronRenderer(BlockEntityRendererFactory.Context context)
	{
		this.dispatcher = context.getRenderDispatcher();
		this.textRenderer = context.getTextRenderer();
	}

	public void renderWater(MatrixStack matrices,
							VertexConsumerProvider vertexConsumers,
							WaterContainer container,
							float tickDelta,
							int color,
							int light,
							int overlay)
	{
		if(container.getWaterLevel() > 0)
		{
			matrices.push();
			matrices.translate(0.125F, 0.25F, 0.125F);
			float newLevel = container.getAnimationProgress(tickDelta);
			float scale = newLevel / 1000F * 0.5625F;
			matrices.scale(0.75F, scale, 0.75F);
			container.setLastWaterLevel((short) (newLevel));
			VertexConsumer builder = vertexConsumers.getBuffer(RenderLayer.getCutout());
			RenderHelper.drawTexture(builder, matrices, WATER_SPRITE, color, light, overlay);
			matrices.pop();
		}
	}

	public void renderWater(MatrixStack matrices,
							VertexConsumerProvider vertexConsumers,
							ColoredWaterContainer container,
							float tickDelta,
							int light,
							int overlay)
	{
		renderWater(matrices, vertexConsumers, container, tickDelta, container.getColor(), light, overlay);
	}
}
