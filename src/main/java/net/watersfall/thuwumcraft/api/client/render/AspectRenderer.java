package net.watersfall.thuwumcraft.api.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.client.util.RenderHelper;

import java.util.Collection;

@Environment(EnvType.CLIENT)
public interface AspectRenderer
{
	@Environment(EnvType.CLIENT)
	default boolean shouldRenderInEvent()
	{
		return false;
	}

	@Environment(EnvType.CLIENT)
	default void setup(MatrixStack matrices, BlockHitResult hit) { }

	@Environment(EnvType.CLIENT)
	default void render(MatrixStack matrices, VertexConsumerProvider vertex, TextRenderer text, BlockPos pos, Vec3d camera, BlockHitResult hit)
	{
		matrices.translate(pos.getX() - camera.x, pos.getY() - camera.y, pos.getZ() - camera.z);
		setup(matrices, hit);
		RenderHelper.renderAspects(getStacksForRender(), pos, matrices, vertex, text, camera);
	}

	@Environment(EnvType.CLIENT)
	Collection<AspectStack> getStacksForRender();
}
