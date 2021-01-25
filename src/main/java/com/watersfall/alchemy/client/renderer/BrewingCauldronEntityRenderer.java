package com.watersfall.alchemy.client.renderer;

import com.watersfall.alchemy.blockentity.BrewingCauldronEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BrewingCauldronEntityRenderer extends BlockEntityRenderer<BrewingCauldronEntity>
{
	private static final Sprite sprite = ((SpriteAtlasTexture) MinecraftClient.getInstance().getTextureManager().getTexture(new Identifier("minecraft", "textures/atlas/blocks.png"))).getSprite(new Identifier("block/water_still"));

	public BrewingCauldronEntityRenderer(BlockEntityRenderDispatcher dispatcher)
	{
		super(dispatcher);
	}

	private int getColor(int... colors)
	{
		int r = 0;
		int g = 0;
		int b = 0;
		int total = 1;
		for(int i = 1; i < colors.length; i++)
		{
			if(colors[i] >= 0)
			{
				r += (colors[i] >> 16) & 0xFF;
				g += (colors[i] >> 8) & 0xFF;
				b += (colors[i]) & 0xFF;
				total++;
			}
		}
		if(total > 1)
		{
			r += ((colors[0] >> 16) & 0xFF) / 2;
			g += ((colors[0] >> 8) & 0xFF) / 2;
			b += ((colors[0]) & 0xFF) / 2;
		}
		else
		{
			r += ((colors[0] >> 16) & 0xFF);
			g += ((colors[0] >> 8) & 0xFF);
			b += ((colors[0]) & 0xFF);
		}
		r /= total;
		g /= total;
		b /= total;
		int color = r;
		color = (color << 8) + g;
		color = (color << 8) + b;
		return color;
	}

	private void add(VertexConsumer renderer, MatrixStack stack, float x, float y, float z, float u, float v, int color)
	{
		renderer.vertex(stack.peek().getModel(), x, y, z)
				.color((((color >> 16) & 0xFF) / 255F), (((color >> 8) & 0xFF) / 255F), (color & 0xFF) / 255F, 1.0f)
				.texture(u, v)
				.light(0, 240)
				.normal(1, 0, 0)
				.next();
	}

	@Override
	public void render(BrewingCauldronEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		if(entity.getWaterLevel() > 0)
		{
			matrices.push();
			matrices.translate(0.125F, 0.25F, 0.125F);
			float newLevel = entity.getAnimationProgress(tickDelta);
			float scale = newLevel / 1000F * 0.5625F;
			matrices.scale(0.75F, scale, 0.75F);
			entity.lastWaterLevel = (short) (newLevel);
			int color;
			if(entity.needsColorUpdate)
			{
				int[] colors = new int[1 + entity.getIngredientCount()];
				colors[0] = BiomeColors.getWaterColor(dispatcher.world, entity.getPos());
				for(int i = 1; i <= entity.getIngredientCount(); i++)
				{
					colors[i] = 0;//Ingredients.ingredients.get(entity.getStack(i - 1).getItem()).color;
				}
				entity.color = getColor(colors);
				entity.needsColorUpdate = false;
			}
			color = entity.color;
			VertexConsumer builder = vertexConsumers.getBuffer(RenderLayer.getTranslucent());
			add(builder, matrices, 0, 1, 0, sprite.getMinU(), sprite.getMinV(), color);
			add(builder, matrices, 1, 1, 0, sprite.getMaxU(), sprite.getMinV(), color);
			add(builder, matrices, 1, 1, 1, sprite.getMaxU(), sprite.getMaxV(), color);
			add(builder, matrices, 0, 1, 1, sprite.getMinU(), sprite.getMaxV(), color);

			add(builder, matrices, 0, 1, 1, sprite.getMinU(), sprite.getMaxV(), color);
			add(builder, matrices, 1, 1, 1, sprite.getMaxU(), sprite.getMaxV(), color);
			add(builder, matrices, 1, 1, 0, sprite.getMaxU(), sprite.getMinV(), color);
			add(builder, matrices, 0, 1, 0, sprite.getMinU(), sprite.getMinV(), color);
			matrices.pop();
		}
	}
}
