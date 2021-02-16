package net.watersfall.alchemy.client.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.watersfall.alchemy.block.entity.CrucibleEntity;

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
		}
	}
}
