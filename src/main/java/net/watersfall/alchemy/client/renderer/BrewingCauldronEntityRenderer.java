package net.watersfall.alchemy.client.renderer;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.watersfall.alchemy.block.BrewingCauldronBlock;
import net.watersfall.alchemy.block.entity.BrewingCauldronEntity;
import net.watersfall.alchemy.client.util.RenderHelper;
import net.watersfall.alchemy.recipe.CauldronIngredient;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

public class BrewingCauldronEntityRenderer extends AbstractCauldronRenderer<BrewingCauldronEntity>
{
	private static final HashMap<Item, Sprite> SPRITE_CACHE = new HashMap<>();

	public BrewingCauldronEntityRenderer(BlockEntityRendererFactory.Context context)
	{
		super(context);
	}

	private Sprite getSprite(Item item)
	{
		if(SPRITE_CACHE.containsKey(item))
		{
			return SPRITE_CACHE.get(item);
		}
		else
		{
			SPRITE_CACHE.put(item, MinecraftClient.getInstance().getItemRenderer().getModels().getModel(item).getSprite());
		}
		return SPRITE_CACHE.get(item);
	}

	private Identifier getSkullTexture(SkullBlock.SkullType type)
	{
		return SkullBlockEntityRenderer.TEXTURES.get(type);
	}

	private void drawItem(Item item, VertexConsumer renderer, MatrixStack matrices, int light, int overlay)
	{
		if(item instanceof BlockItem && ((BlockItem)item).getBlock() instanceof AbstractSkullBlock)
		{
			Block block = ((BlockItem)item).getBlock();
			Identifier texture = getSkullTexture(((AbstractSkullBlock)block).getSkullType());
			RenderHelper.drawTexture(renderer, matrices, texture, -1, light, overlay);
		}
		else
		{
			Sprite sprite = getSprite(item);
			RenderHelper.drawTexture(renderer, matrices, sprite, -1, light, overlay);
		}
	}

	@Override
	public void render(BrewingCauldronEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		if(entity.getWaterLevel() == 0)
		{
			entity.setLastWaterLevel((short) 0);
		}
		else if(entity.getWaterLevel() > 0)
		{
			renderWater(matrices, vertexConsumers, entity, tickDelta, light, overlay);
			HitResult result = MinecraftClient.getInstance().crosshairTarget;
			if(!MinecraftClient.getInstance().options.hudHidden && result != null && result.getType() == HitResult.Type.BLOCK)
			{
				BlockPos pos = new BlockPos(result.getPos());
				if(pos.equals(entity.getPos()))
				{
					matrices.push();
					VertexConsumer builder = vertexConsumers.getBuffer(RenderLayer.getCutout());
					matrices.translate(0.5D, 1.75D, 0.5D);
					Quaternion quaternion = dispatcher.camera.getRotation().copy();
					quaternion.hamiltonProduct(Vec3f.NEGATIVE_X.getDegreesQuaternion(270));
					matrices.scale(0.25F, 0.25F, 0.25F);
					matrices.multiply(quaternion);
					matrices.translate(0.5D, 0D, 0.5D);
					matrices.translate(-0.5F * (entity.getIngredientCount() + 1), 0F, 0F);
					entity.getContents().forEach((stack -> {
						if(!stack.isEmpty())
						{
							Sprite sprite = MinecraftClient.getInstance().getItemRenderer().getModels().getModel(stack.getItem()).getSprite();
							RenderHelper.drawTexture(builder, matrices, sprite, -1, 9437408, 655360, false);
							if(stack.getCount() > 1)
							{
								matrices.push();
								matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(90));
								if(stack.getCount() >= 10)
								{
									matrices.translate(0.5F, -0.625F, 0.99F);
								}
								else
								{
									matrices.translate(0.375F, -0.625F, 0.99F);
								}
								matrices.scale(0.0625F, 0.0625F, 0.0625F);
								matrices.scale(-1F, -1F, -1F);
								textRenderer.draw(matrices, "" + stack.getCount(), 0F, 0F, -1);
								matrices.pop();
							}
							matrices.translate(1F, 0F, 0F);
						}
					}));
					matrices.pop();
				}
			}
		}
	}
}
