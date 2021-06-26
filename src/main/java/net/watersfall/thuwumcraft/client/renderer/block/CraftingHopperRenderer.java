package net.watersfall.thuwumcraft.client.renderer.block;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.watersfall.thuwumcraft.block.CraftingHopper;
import net.watersfall.thuwumcraft.block.entity.CraftingHopperEntity;

public class CraftingHopperRenderer implements BlockEntityRenderer<CraftingHopperEntity>
{

	public CraftingHopperRenderer(BlockEntityRendererFactory.Context context) { }

	@Override
	public void render(CraftingHopperEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		int patternIndex = entity.getCachedState().get(CraftingHopper.PATTERN);
		int[] pattern = CraftingHopperEntity.PATTERNS[patternIndex];
		for(int i = 0; i < pattern.length; i++)
		{
			matrices.push();
			int x = pattern[i] % 3 + 1;
			int y = pattern[i] / 3 + 1;
			Direction direction = entity.getCachedState().get(CraftingHopper.FACING);
			if(direction == Direction.WEST)
			{
				matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(90));
			}
			else if(direction == Direction.NORTH)
			{
				matrices.translate(1, 0, 0);
				matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(180));
			}
			else if(direction == Direction.EAST)
			{
				matrices.translate(1, 0, 1);
				matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(-90));
			}
			else
			{
				matrices.translate(0, 0, 1);
				matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(0));
			}
			matrices.scale(0.125F, 0.125F, 0.125F);
			matrices.translate(x + 2, y + 4, 0);
			MinecraftClient.getInstance().getItemRenderer().renderItem(Items.DIAMOND.getDefaultStack(), ModelTransformation.Mode.GUI, light, overlay, matrices, vertexConsumers, 0);
			matrices.pop();
		}
	}
}
