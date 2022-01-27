package net.watersfall.thuwumcraft.client.renderer.block;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.watersfall.thuwumcraft.block.entity.FocalManipulatorBlockEntity;

public class FocalManipulatorRenderer implements BlockEntityRenderer<FocalManipulatorBlockEntity>
{
	private final ItemRenderer itemRenderer;

	public FocalManipulatorRenderer(BlockEntityRendererFactory.Context context)
	{
		itemRenderer = MinecraftClient.getInstance().getItemRenderer();
	}

	@Override
	public void render(FocalManipulatorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		ItemStack stack = entity.getStack(0);
		if(!stack.isEmpty())
		{
			light = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
			matrices.push();
			double offset = MathHelper.sin((entity.getWorld().getTime() + tickDelta) / 10F) / 10D;
			matrices.translate(0.5, 1.1 + offset , 0.5);
			float angle = (float)((entity.getWorld().getTime() + tickDelta) / 20F + offset);
			matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(angle));
			itemRenderer.renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
			matrices.pop();
		}
	}
}
