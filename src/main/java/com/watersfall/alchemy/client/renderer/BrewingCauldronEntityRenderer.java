package com.watersfall.alchemy.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.watersfall.alchemy.block.BrewingCauldronBlock;
import com.watersfall.alchemy.blockentity.BrewingCauldronEntity;
import com.watersfall.alchemy.recipe.CauldronIngredients;
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
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

public class BrewingCauldronEntityRenderer extends BlockEntityRenderer<BrewingCauldronEntity>
{
	private static final Sprite sprite = ((SpriteAtlasTexture) MinecraftClient.getInstance().getTextureManager().getTexture(new Identifier("minecraft", "textures/atlas/blocks.png"))).getSprite(new Identifier("block/water_still"));
	private static final HashMap<Item, Sprite> SPRITE_CACHE = new HashMap<>();

	public BrewingCauldronEntityRenderer(BlockEntityRenderDispatcher dispatcher)
	{
		super(dispatcher);
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

	private void add(VertexConsumer renderer, MatrixStack stack, float x, float y, float z, float u, float v, int color, int light, int overlay)
	{
		renderer.vertex(stack.peek().getModel(), x, y, z)
				.color((((color >> 16) & 0xFF) / 255F), (((color >> 8) & 0xFF) / 255F), (color & 0xFF) / 255F, 1.0f)
				.texture(u, v)
				.light(light)
				.normal(1, 0, 0)
				.next();
	}

	private void drawTexture(VertexConsumer renderer, MatrixStack matrices, Sprite sprite, int color, int light, int overlay)
	{
		add(renderer, matrices, 0, 1, 0, sprite.getMinU(), sprite.getMinV(), color, light, overlay);
		add(renderer, matrices, 1, 1, 0, sprite.getMaxU(), sprite.getMinV(), color, light, overlay);
		add(renderer, matrices, 1, 1, 1, sprite.getMaxU(), sprite.getMaxV(), color, light, overlay);
		add(renderer, matrices, 0, 1, 1, sprite.getMinU(), sprite.getMaxV(), color, light, overlay);
		add(renderer, matrices, 0, 1, 1, sprite.getMinU(), sprite.getMaxV(), color, light, overlay);
		add(renderer, matrices, 1, 1, 1, sprite.getMaxU(), sprite.getMaxV(), color, light, overlay);
		add(renderer, matrices, 1, 1, 0, sprite.getMaxU(), sprite.getMinV(), color, light, overlay);
		add(renderer, matrices, 0, 1, 0, sprite.getMinU(), sprite.getMinV(), color, light, overlay);
	}

	private void drawTexture(VertexConsumer renderer, MatrixStack matrices, Identifier sprite, int color, int light, int overlay)
	{
		RenderSystem.pushMatrix();
		RenderSystem.enableDepthTest();
		this.dispatcher.textureManager.bindTexture(sprite);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		builder.begin(7, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
		add(builder, matrices, 0, 1, 0, 0.125F, 0.25F, color, light, overlay);
		add(builder, matrices, 1, 1, 0, 0.25F, 0.25F, color, light, overlay);
		add(builder, matrices, 1, 1, 1, 0.25F, 0.5F, color, light, overlay);
		add(builder, matrices, 0, 1, 1, 0.125F, 0.5F, color, light, overlay);

		add(builder, matrices, 0, 1, 1, 0.125F, 0.5F, color, light, overlay);
		add(builder, matrices, 1, 1, 1, 0.25F, 0.5F, color, light, overlay);
		add(builder, matrices, 1, 1, 0, 0.25F, 0.25F, color, light, overlay);
		add(builder, matrices, 0, 1, 0, 0.125F, 0.25F, color, light, overlay);
		tessellator.draw();
		RenderSystem.popMatrix();
	}

	private void drawItem(Item item, VertexConsumer renderer, MatrixStack matrices, int light, int overlay)
	{
		if(item instanceof BlockItem && ((BlockItem)item).getBlock() instanceof AbstractSkullBlock)
		{
			Block block = ((BlockItem)item).getBlock();
			Identifier texture = getSkullTexture(((AbstractSkullBlock)block).getSkullType());
			drawTexture(renderer, matrices, texture, -1, light, overlay);
		}
		else
		{
			Sprite sprite = getSprite(item);
			drawTexture(renderer, matrices, sprite, -1, light, overlay);
		}
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
					CauldronIngredients ingredient = BrewingCauldronBlock.getIngredient(entity.getStack(i - 1).getItem(), this.dispatcher.world.getRecipeManager());
					colors[i] = ingredient == null ? -1 : ingredient.color;
				}
				entity.color = getColor(colors);
				entity.needsColorUpdate = false;
			}
			color = entity.color;
			VertexConsumer builder = vertexConsumers.getBuffer(RenderLayer.getTranslucent());
			drawTexture(builder, matrices, sprite, color, light, overlay);
			matrices.pop();

			if(entity.getIngredientCount() > 0)
			{
				HitResult result = MinecraftClient.getInstance().crosshairTarget;
				if(result != null && result.getType() == HitResult.Type.BLOCK)
				{
					Vec3d blockPos = new Vec3d(entity.getPos().getX() + 0.5D, entity.getPos().getY() + 0.5D, entity.getPos().getZ() + 0.5D);
					if(result.getPos().distanceTo(blockPos) <= 1D)
					{
						matrices.push();
						builder = vertexConsumers.getBuffer(RenderLayer.getCutout());
						matrices.translate(0.5D, 1.75D, 0.5D);
						Quaternion quaternion = dispatcher.camera.getRotation().copy();
						quaternion.hamiltonProduct(Vector3f.NEGATIVE_X.getDegreesQuaternion(270));
						matrices.scale(0.25F, 0.25F, 0.25F);
						matrices.multiply(quaternion);
						matrices.translate(0.5D, 0D, 0.5D);
						if(entity.getIngredientCount() == 1)
						{
							matrices.translate(-1D, 0, 0);
							drawItem(entity.getContents().get(0).getItem(), builder, matrices, 9437408, 655360);
						}
						else if(entity.getIngredientCount() == 2)
						{
							matrices.translate(-0.5D, 0, 0);
							drawItem(entity.getContents().get(0).getItem(), builder, matrices, 9437408, 655360);
							matrices.translate(-1D, 0, 0);
							drawItem(entity.getContents().get(1).getItem(), builder, matrices, 9437408, 655360);
						}
						else
						{
							for(int i = 0; i < entity.getIngredientCount(); i++)
							{
								drawItem(entity.getContents().get(i).getItem(), builder, matrices, 9437408, 655360);
								matrices.translate(-1D, 0, 0);
							}
						}
						matrices.pop();
					}
				}
			}
		}
	}
}
