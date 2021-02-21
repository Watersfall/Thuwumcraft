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
import net.minecraft.util.math.BlockPos;
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
			RenderHelper.renderAspects(entity.getAspects().values(), entity, matrices, vertexConsumers, textRenderer, dispatcher);
		}
	}
}
