package net.watersfall.alchemy.client.renderer;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.watersfall.alchemy.block.entity.PedestalEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.watersfall.alchemy.client.util.RenderHelper;

public class PedestalEntityRenderer implements BlockEntityRenderer<PedestalEntity>
{
	private final BlockEntityRenderDispatcher dispatcher;
	private final TextRenderer textRenderer;
	public PedestalEntityRenderer(BlockEntityRendererFactory.Context context)
	{
		dispatcher = context.getRenderDispatcher();
		textRenderer = context.getTextRenderer();
	}

	@Override
	public void render(PedestalEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		if(!entity.getStack().isEmpty())
		{
			light = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
			matrices.push();
			double yOffset = MathHelper.sin((entity.getWorld().getTime() + tickDelta) / 10F) / 10D;
			matrices.translate(0.5D, 1.25D + yOffset, 0.5D);
			float angle = (float)((entity.getWorld().getTime() + tickDelta) / 20F + yOffset);
			matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(angle));
			matrices.scale(0.625F, 0.625F, 0.625F);
			MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
			matrices.pop();
		}
		if(entity.isMain() && !entity.getNeededAspects().isEmpty())
		{
			HitResult result = MinecraftClient.getInstance().crosshairTarget;
			if(!MinecraftClient.getInstance().options.hudHidden && result != null && result.getType() == HitResult.Type.BLOCK)
			{
				BlockPos pos = new BlockPos(result.getPos());
				if(pos.equals(entity.getPos()))
				{
					matrices.push();
					VertexConsumer builder = vertexConsumers.getBuffer(RenderLayer.getCutout());
					matrices.translate(0.5D, 2.125D, 0.5D);
					Quaternion quaternion = dispatcher.camera.getRotation().copy();
					quaternion.hamiltonProduct(Vec3f.NEGATIVE_X.getDegreesQuaternion(270));
					matrices.scale(0.25F, 0.25F, 0.25F);
					matrices.multiply(quaternion);
					matrices.translate(0.5D, 0D, 0.5D);
					matrices.translate(-0.5F * (entity.getNeededAspects().size() + 1), 0F, 0F);
					entity.getNeededAspects().forEach((aspectStack -> {
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
