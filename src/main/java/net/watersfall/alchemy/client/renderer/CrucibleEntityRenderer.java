package net.watersfall.alchemy.client.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.watersfall.alchemy.block.entity.CrucibleEntity;
import net.watersfall.alchemy.client.util.RenderHelper;

public class CrucibleEntityRenderer extends AbstractCauldronRenderer<CrucibleEntity>
{
	public CrucibleEntityRenderer(BlockEntityRendererFactory.Context context)
	{
		super(context);
	}

	@Override
	public void render(CrucibleEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		if(entity.getWaterLevel() == 0)
		{
			entity.setLastWaterLevel((short) 0);
		}
		if(entity.getWaterLevel() > 0)
		{
			super.renderWater(matrices, vertexConsumers, entity, tickDelta, light, overlay);
			if(entity.aspectSize() > 0)
			{
				HitResult result = MinecraftClient.getInstance().crosshairTarget;
				if(result != null && result.getType() == HitResult.Type.BLOCK)
				{
					Vec3d blockPos = new Vec3d(entity.getPos().getX() + 0.5D, entity.getPos().getY() + 0.5D, entity.getPos().getZ() + 0.5D);
					if(result.getPos().distanceTo(blockPos) <= 1D)
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
}
