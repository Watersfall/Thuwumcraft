package net.watersfall.alchemy.client.renderer;

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
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.watersfall.alchemy.api.aspect.AspectStack;
import net.watersfall.alchemy.block.entity.JarEntity;
import net.watersfall.alchemy.client.util.RenderHelper;

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
			HitResult result = MinecraftClient.getInstance().crosshairTarget;
			if(!MinecraftClient.getInstance().options.hudHidden && result != null && result.getType() == HitResult.Type.BLOCK)
			{
				BlockPos pos = new BlockPos(result.getPos());
				if(pos.equals(entity.getPos()))
				{
					matrices.push();
					VertexConsumer builder = vertexConsumers.getBuffer(RenderLayer.getCutout());
					matrices.translate(0.5D, 1.75D, 0.5D);
					Quaternion quaternion = dispatcher.camera.getRotation().copy();
					quaternion.hamiltonProduct(Vec3f.NEGATIVE_X.getDegreesQuaternion(270));
					matrices.scale(0.25F, 0.25F, 0.25F);
					matrices.multiply(quaternion);
					matrices.translate(0.5D, 0D, 0.5D);
					matrices.translate(-0.5F * (entity.getAspects().size() + 1), 0F, 0F);
					entity.getAspects().values().forEach((aspectStack -> {
						Sprite sprite = MinecraftClient.getInstance().getItemRenderer().getModels().getModel(aspectStack.getAspect().getItem()).getSprite();
						RenderHelper.drawTexture(builder, matrices, sprite, -1, 9437408, 655360);
						if(aspectStack.getCount() > 1)
						{
							matrices.push();
							matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(90));
							if(aspectStack.getCount() >= 10)
							{
								matrices.translate(0.5F, -0.625F, 0.99F);
							}
							else
							{
								matrices.translate(0.375F, -0.625F, 0.99F);
							}
							matrices.scale(0.0625F, 0.0625F, 0.0625F);
							matrices.scale(-1F, -1F, -1F);
							textRenderer.draw(matrices, "" + aspectStack.getCount(), 0F, 0F, -1);
							matrices.pop();
						}
						matrices.translate(1F, 0F, 0F);
					}));
					matrices.pop();
				}
			}
		}
	}
}
