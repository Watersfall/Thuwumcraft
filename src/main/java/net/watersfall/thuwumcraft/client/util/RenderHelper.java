package net.watersfall.thuwumcraft.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;

import java.util.Collection;

public class RenderHelper
{
	public static int getColor(int... colors)
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

	private static void add(VertexConsumer renderer, MatrixStack stack, float x, float y, float z, float u, float v, int color, int light, int overlay)
	{
		renderer.vertex(stack.peek().getModel(), x, y, z)
				.color((((color >> 16) & 0xFF) / 255F), (((color >> 8) & 0xFF) / 255F), (color & 0xFF) / 255F, 1.0f)
				.texture(u, v)
				.light(light)
				.normal(1, 0, 0)
				.next();
	}

	public static void drawTexture(VertexConsumer renderer, MatrixStack matrices, Sprite sprite, int color, int light, int overlay)
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

	public static void drawTexture(VertexConsumer renderer, MatrixStack matrices, Sprite sprite, int color, int light, int overlay, boolean inverted)
	{
		if(inverted)
		{
			drawTexture(renderer, matrices, sprite, color, light, overlay, inverted);
		}
		else
		{
			add(renderer, matrices, 1, 1, 0, sprite.getMinU(), sprite.getMinV(), color, light, overlay);
			add(renderer, matrices, 0, 1, 0, sprite.getMaxU(), sprite.getMinV(), color, light, overlay);
			add(renderer, matrices, 0, 1, 1, sprite.getMaxU(), sprite.getMaxV(), color, light, overlay);
			add(renderer, matrices, 1, 1, 1, sprite.getMinU(), sprite.getMaxV(), color, light, overlay);
			add(renderer, matrices, 1, 1, 1, sprite.getMinU(), sprite.getMaxV(), color, light, overlay);
			add(renderer, matrices, 0, 1, 1, sprite.getMaxU(), sprite.getMaxV(), color, light, overlay);
			add(renderer, matrices, 0, 1, 0, sprite.getMaxU(), sprite.getMinV(), color, light, overlay);
			add(renderer, matrices, 1, 1, 0, sprite.getMinU(), sprite.getMinV(), color, light, overlay);
		}
	}

	public static void drawTexture(VertexConsumer renderer, MatrixStack matrices, Sprite sprite, float minU, float minV, float maxU, float maxV, int color, int light, int overlay)
	{
		add(renderer, matrices, 0, 1, 0, minU, minV, color, light, overlay);
		add(renderer, matrices, 1, 1, 0, maxU, minV, color, light, overlay);
		add(renderer, matrices, 1, 1, 1, maxU, maxV, color, light, overlay);
		add(renderer, matrices, 0, 1, 1, minU, maxV, color, light, overlay);
		add(renderer, matrices, 0, 1, 1, minU, maxV, color, light, overlay);
		add(renderer, matrices, 1, 1, 1, maxU, maxV, color, light, overlay);
		add(renderer, matrices, 1, 1, 0, maxU, minV, color, light, overlay);
		add(renderer, matrices, 0, 1, 0, minU, minV, color, light, overlay);
	}

	public static void drawTexture(VertexConsumer renderer, MatrixStack matrices, Identifier sprite, int color, int light, int overlay)
	{
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderTexture(0, sprite);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
		add(builder, matrices, 0, 1, 0, 0.125F, 0.25F, color, light, overlay);
		add(builder, matrices, 1, 1, 0, 0.25F, 0.25F, color, light, overlay);
		add(builder, matrices, 1, 1, 1, 0.25F, 0.5F, color, light, overlay);
		add(builder, matrices, 0, 1, 1, 0.125F, 0.5F, color, light, overlay);

		add(builder, matrices, 0, 1, 1, 0.125F, 0.5F, color, light, overlay);
		add(builder, matrices, 1, 1, 1, 0.25F, 0.5F, color, light, overlay);
		add(builder, matrices, 1, 1, 0, 0.25F, 0.25F, color, light, overlay);
		add(builder, matrices, 0, 1, 0, 0.125F, 0.25F, color, light, overlay);
		tessellator.draw();
	}

	public static void renderAspects(Collection<AspectStack> aspects,
									 BlockEntity entity,
									 MatrixStack matrices,
									 VertexConsumerProvider vertexConsumers,
									 TextRenderer textRenderer,
									 BlockEntityRenderDispatcher dispatcher)
	{
		if(aspects.size() > 0)
		{
			HitResult result = MinecraftClient.getInstance().crosshairTarget;
			ClientPlayerEntity player = MinecraftClient.getInstance().player;
			boolean shouldRender = (
					!MinecraftClient.getInstance().options.hudHidden
							&& result != null && result.getType() == HitResult.Type.BLOCK
							&& ((BlockHitResult)result).getBlockPos().equals(entity.getPos()))
					&& (player.getStackInHand(Hand.MAIN_HAND).getItem() == ThuwumcraftItems.THUWUMIC_MAGNIFYING_GLASS
							|| player.getStackInHand(Hand.OFF_HAND).getItem() == ThuwumcraftItems.THUWUMIC_MAGNIFYING_GLASS)
					|| (player.getEquippedStack(EquipmentSlot.HEAD).getItem() == ThuwumcraftItems.GOGGLES
							&& entity.getPos().getSquaredDistance(player.getBlockPos()) < 256);
			if(shouldRender)
			{
				matrices.push();
				VertexConsumer builder = vertexConsumers.getBuffer(RenderLayer.getCutout());
				matrices.translate(0.5D, 1.75D, 0.5D);
				Quaternion quaternion = dispatcher.camera.getRotation().copy();
				quaternion.hamiltonProduct(Vec3f.NEGATIVE_X.getDegreesQuaternion(270));
				matrices.scale(0.25F, 0.25F, 0.25F);
				matrices.multiply(quaternion);
				matrices.translate(0.5D, 0D, 0.5D);
				matrices.translate(-0.5F * (aspects.size() + 1), 0F, 0F);
				aspects.forEach((aspectStack -> {
					Sprite sprite = MinecraftClient.getInstance().getItemRenderer().getModels().getModel(aspectStack.getAspect().getItem()).getParticleSprite();
					RenderHelper.drawTexture(builder, matrices, sprite, aspectStack.getAspect().getColor(), 9437408, 655360, false);
					if(aspectStack.getCount() > 1)
					{
						matrices.push();
						matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(90));
						if(aspectStack.getCount() >= 10)
						{
							matrices.translate(0.5F, -0.625F, 0.99F);
						}
						else
						{
							matrices.translate(0.375F, -0.625F, 0.99F);
						}
						matrices.scale(0.0625F, 0.0625F, 0.0625F);
						matrices.scale(-1F, -1F, -1F);
						textRenderer.draw(matrices, "" + aspectStack.getCount(), 0F, 0F, -1);
						matrices.pop();
					}
					matrices.translate(1F, 0F, 0F);
				}));
				matrices.pop();
			}

		}
	}
}
