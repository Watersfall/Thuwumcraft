package com.watersfall.alchemy.client.renderer;

import com.watersfall.alchemy.blockentity.PedestalEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;

public class PedestalEntityRenderer extends BlockEntityRenderer<PedestalEntity>
{
	public PedestalEntityRenderer(BlockEntityRenderDispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Override
	public void render(PedestalEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		if(!entity.getStack().isEmpty())
		{
			light = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
			matrices.push();
			matrices.translate(0.5D, 1.125D, 0.5D);
			MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);
			matrices.pop();
		}
	}
}
