package net.watersfall.thuwumcraft.client.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.block.entity.JarEntity;
import net.watersfall.thuwumcraft.client.util.RenderHelper;

public class JarEntityRenderer implements BlockEntityRenderer<JarEntity>
{
	private static final Sprite WATER_SPRITE = ((SpriteAtlasTexture) MinecraftClient.getInstance()
			.getTextureManager()
			.getTexture(new Identifier("minecraft", "textures/atlas/blocks.png"))).getSprite(new Identifier("block/water_still"));

	private final BlockEntityRenderDispatcher dispatcher;
	private final TextRenderer textRenderer;

	public JarEntityRenderer(BlockEntityRendererFactory.Context context)
	{
		this.dispatcher = context.getRenderDispatcher();
		this.textRenderer = context.getTextRenderer();
	}

	@Override
	public void render(JarEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		if(entity.aspectSize() > 0)
		{
			matrices.push();
			AspectStack stack = entity.getAspects().values().stream().findFirst().get();
			float scale = ((float)stack.getCount() / (float)entity.getMaxAspectCount()) / 1.5F;
			matrices.scale(0.5F, scale, 0.5F);
			matrices.translate(0.5F, 0.0625F, 0.5F);
			RenderHelper.drawTexture(
					vertexConsumers.getBuffer(RenderLayer.getSolid()),
					matrices,
					WATER_SPRITE,
					stack.getAspect().getColor(),
					light,
					overlay
			);
			matrices.push();
			float minU = WATER_SPRITE.getMinU();
			float maxU = WATER_SPRITE.getMaxU();
			float minV = WATER_SPRITE.getMinV();
			float maxV = minV + (WATER_SPRITE.getMaxV() - minV) * scale;
			matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(90F));
			matrices.translate(0F, -1F, 0F);
			RenderHelper.drawTexture(
					vertexConsumers.getBuffer(RenderLayer.getSolid()),
					matrices,
					WATER_SPRITE,
					minU,
					minV,
					maxU,
					maxV,
					stack.getAspect().getColor(),
					light,
					overlay
			);
			matrices.pop();
			matrices.push();
			matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(270F));
			matrices.translate(0F, 0F, -1F);
			RenderHelper.drawTexture(
					vertexConsumers.getBuffer(RenderLayer.getSolid()),
					matrices,
					WATER_SPRITE,
					minU,
					minV,
					maxU,
					maxV,
					stack.getAspect().getColor(),
					light,
					overlay
			);
			matrices.pop();
			matrices.push();
			matrices.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(270F));
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90F));
			matrices.translate(-1F, -1F, 0F);
			RenderHelper.drawTexture(
					vertexConsumers.getBuffer(RenderLayer.getSolid()),
					matrices,
					WATER_SPRITE,
					minU,
					minV,
					maxU,
					maxV,
					stack.getAspect().getColor(),
					light,
					overlay
			);
			matrices.pop();
			matrices.push();
			matrices.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(90F));
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90F));
			matrices.translate(-1F, 0F, -1F);
			RenderHelper.drawTexture(
					vertexConsumers.getBuffer(RenderLayer.getSolid()),
					matrices,
					WATER_SPRITE,
					minU,
					minV,
					maxU,
					maxV,
					stack.getAspect().getColor(),
					light,
					overlay
			);
			matrices.pop();
			matrices.pop();
			RenderHelper.renderAspects(entity.getAspects().values(), entity, matrices, vertexConsumers, textRenderer, dispatcher);
		}
	}
}
