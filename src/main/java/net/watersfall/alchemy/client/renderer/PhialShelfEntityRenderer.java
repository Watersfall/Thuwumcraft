package net.watersfall.alchemy.client.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.block.AlchemyBlocks;
import net.watersfall.alchemy.block.PhialShelfBlock;
import net.watersfall.alchemy.block.entity.PhialShelfEntity;

public class PhialShelfEntityRenderer implements BlockEntityRenderer<PhialShelfEntity>
{
	private final BlockRenderManager manager;
	public PhialShelfEntityRenderer(BlockEntityRendererFactory.Context context)
	{
		manager = context.getRenderManager();
	}

	private void translateForDirection(MatrixStack matrices, Direction direction)
	{
		switch(direction)
		{
			case NORTH:
				matrices.translate(-0.9, 0, 0);
				break;
			case EAST:
				matrices.translate(0, 0, -0.9);
				break;
			case SOUTH:
				matrices.translate(0.9, 0, 0);
				break;
			case WEST:
				matrices.translate(0, 0, 0.9);
				break;
		}
	}

	@Override
	public void render(PhialShelfEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		matrices.push();
		matrices.scale(0.33F, 0.33F, 0.33F);
		Direction direction = entity.getWorld().getBlockState(entity.getPos()).get(PhialShelfBlock.DIRECTION);
		switch(direction)
		{
			case NORTH:
				matrices.translate(2F + 0.4F, 2F, 2F);
				break;
			case SOUTH:
				matrices.translate(0.625F, 2F, 1F);
				break;
			case EAST:
				matrices.translate(1F, 2F, 2F + 0.4F);
				break;
			case WEST:
				matrices.translate(2F, 2F, 0.625F);
				break;
		}
		matrices.push();
		for(int i = 0; i < entity.size(); i++)
		{
			if(!entity.getStack(i).isEmpty())
			{
				matrices.push();
				if(direction == Direction.EAST || direction == Direction.WEST)
				{
					matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90F));
				}
				if(direction == Direction.SOUTH)
				{
					matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180F));
				}
				MinecraftClient.getInstance().getItemRenderer().renderItem(
						entity.getStack(i),
						ModelTransformation.Mode.FIXED,
						light,
						overlay,
						matrices,
						vertexConsumers,
						0
				);
				matrices.pop();
			}
			translateForDirection(matrices, direction);
			if(i == 2)
			{
				matrices.pop();
				matrices.translate(0, -1.325F, 0);
			}
		}
		matrices.pop();
	}
}
