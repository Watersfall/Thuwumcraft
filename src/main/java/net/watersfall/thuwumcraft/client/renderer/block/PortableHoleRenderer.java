package net.watersfall.thuwumcraft.client.renderer.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.watersfall.thuwumcraft.block.entity.PortableHoleBlockEntity;

public class PortableHoleRenderer implements BlockEntityRenderer<PortableHoleBlockEntity>
{
	private final BlockRenderManager manager;

	public PortableHoleRenderer(BlockEntityRendererFactory.Context context)
	{
		manager = context.getRenderManager();
	}

	@Override
	public void render(PortableHoleBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		this.renderSides(entity, matrices.peek().getModel(), vertexConsumers.getBuffer(RenderLayer.getEndPortal()));
	}

	private void renderSides(PortableHoleBlockEntity entity, Matrix4f matrix4f, VertexConsumer vertexConsumer)
	{
		this.renderSide(entity, matrix4f, vertexConsumer, 0.0001F, 0.9999F, 0.0001F, 0.9999F, 0.9999F, 0.9999F, 0.9999F, 0.9999F, Direction.NORTH);
		this.renderSide(entity, matrix4f, vertexConsumer, 0.0001F, 0.9999F, 0.9999F, 0.0001F, 0.0001F, 0.0001F, 0.0001F, 0.0001F, Direction.SOUTH);
		this.renderSide(entity, matrix4f, vertexConsumer, 0.9999F, 0.9999F, 0.9999F, 0.0001F, 0.0001F, 0.9999F, 0.9999F, 0.0001F, Direction.WEST);
		this.renderSide(entity, matrix4f, vertexConsumer, 0.0001F, 0.0001F, 0.0001F, 0.9999F, 0.0001F, 0.9999F, 0.9999F, 0.0001F, Direction.EAST);
		this.renderSide(entity, matrix4f, vertexConsumer, 0.0001F, 0.9999F, 0.9999F, 0.9999F, 0.0001F, 0.0001F, 0.9999F, 0.9999F, Direction.DOWN);
		this.renderSide(entity, matrix4f, vertexConsumer, 0.0001F, 0.9999F, 0.0001F, 0.0001F, 0.9999F, 0.9999F, 0.0001F, 0.0001F, Direction.UP);
	}

	private void renderSide(PortableHoleBlockEntity entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction direction)
	{
		if (shouldDrawSide(direction, entity))
		{
			addVertex(vertices, model, x1, y1, z1, 0, 0);
			addVertex(vertices, model, x2, y1, z2, 1, 0);
			addVertex(vertices, model, x2, y2, z3, 1, 1);
			addVertex(vertices, model, x1, y2, z4, 0, 1);
			addVertex(vertices, model, x1, y2, z1, 0, 1);
			addVertex(vertices, model, x2, y2, z2, 1, 1);
			addVertex(vertices, model, x2, y1, z3, 1, 0);
			addVertex(vertices, model, x1, y1, z4, 0, 0);
		}
	}

	private boolean shouldDrawSide(Direction direction, PortableHoleBlockEntity entity)
	{
		BlockState state = MinecraftClient.getInstance().world.getBlockState(entity.getPos().offset(direction.getOpposite()));
		return !state.isAir() && state.getMaterial().isSolid();
	}
	
	private void addVertex(VertexConsumer vertices, Matrix4f model, float x, float y, float z, float u, float v)
	{
		vertices.vertex(model, x, y, z).texture(u, v).next();
	}
}
