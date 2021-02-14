package net.watersfall.alchemy.client.renderer;

import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.util.math.Vec3f;
import net.watersfall.alchemy.block.entity.PedestalEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class PedestalEntityRenderer implements BlockEntityRenderer<PedestalEntity>
{
	public PedestalEntityRenderer(BlockEntityRendererFactory.Context context)
	{

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
	}
}
