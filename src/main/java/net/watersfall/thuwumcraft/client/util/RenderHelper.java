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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.client.renderer.ThuwumcraftRenderLayers;
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
									 BlockPos pos,
									 MatrixStack matrices,
									 VertexConsumerProvider vertexConsumers,
									 TextRenderer textRenderer,
									 Vec3d camera)
	{
		if(aspects.size() > 0)
		{
			HitResult result = MinecraftClient.getInstance().crosshairTarget;
			ClientPlayerEntity player = MinecraftClient.getInstance().player;
			ItemStack helmet = player.getEquippedStack(EquipmentSlot.HEAD);
			boolean shouldRender = (
					!MinecraftClient.getInstance().options.hudHidden
							&& result != null && result.getType() == HitResult.Type.BLOCK
							&& ((BlockHitResult)result).getBlockPos().equals(pos))
					&& (player.getStackInHand(Hand.MAIN_HAND).getItem() == ThuwumcraftItems.THUWUMIC_MAGNIFYING_GLASS
							|| player.getStackInHand(Hand.OFF_HAND).getItem() == ThuwumcraftItems.THUWUMIC_MAGNIFYING_GLASS
							|| (helmet.hasNbt() && helmet.getNbt().contains("thuwumcraft$goggles") && helmet.getNbt().getBoolean("thuwumcraft$goggles"))
					)
					|| (helmet.getItem() == ThuwumcraftItems.GOGGLES
							&& pos.getSquaredDistance(player.getBlockPos()) < 256);
			if(shouldRender)
			{
				matrices.push();
				VertexConsumer builder = vertexConsumers.getBuffer(ThuwumcraftRenderLayers.INSTANCE);
				matrices.translate(0.5D, 1.75D, 0.5D);
				Vec3d center = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
				float angle = (float)MathHelper.atan2(camera.x - center.x, camera.z - center.z);
				matrices.scale(0.25F, 0.25F, 0.25F);
				Quaternion quaternion = Vec3f.POSITIVE_X.getDegreesQuaternion(90);
				quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getRadialQuaternion(-angle + MathHelper.PI));
				matrices.multiply(quaternion);
				matrices.translate(0.5D, 0D, 0.5D);
				matrices.translate(-0.5F * (aspects.size() + 1), 0F, 0F);
				for(AspectStack aspectStack : aspects)
				{
					Sprite sprite = MinecraftClient.getInstance().getItemRenderer().getModels().getModel(aspectStack.getAspect().getItem()).getParticleSprite();
					RenderHelper.drawTexture(builder, matrices, sprite, aspectStack.getAspect().getColor(), 9437408, 655360);
					RenderSystem.disableDepthTest();
					if(aspectStack.getCount() > 0)
					{
						matrices.push();
						matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(90));
						if(aspectStack.getCount() >= 10)
						{
							matrices.translate(0.5F, -0.625F, 0.98F);
						}
						else
						{
							matrices.translate(0.375F, -0.625F, 0.99F);
						}
						matrices.scale(0.0625F, 0.0625F, 0.0625F);
						matrices.scale(-1F, -1F, -1F);
						textRenderer.draw("" + aspectStack.getCount(), 0, 0, 16777215, false, matrices.peek().getModel(), vertexConsumers, true, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
						matrices.pop();
					}
					matrices.translate(1F, 0F, 0F);
				}
				matrices.pop();
			}
		}
	}

	public static void renderAspects(Collection<AspectStack> aspects,
									 BlockEntity entity,
									 MatrixStack matrices,
									 VertexConsumerProvider vertexConsumers,
									 TextRenderer textRenderer,
									 BlockEntityRenderDispatcher dispatcher)
	{
		renderAspects(aspects, entity.getPos(), matrices, vertexConsumers, textRenderer, dispatcher.camera.getPos());
	}

	public static boolean isHoldingBell(PlayerEntity player)
	{
		if(player == null)
		{
			return false;
		}
		ItemStack main = player.getMainHandStack();
		ItemStack off = player.getOffHandStack();
		return main.isOf(ThuwumcraftItems.GOLEM_BELL_ITEM) || off.isOf(ThuwumcraftItems.GOLEM_BELL_ITEM);
	}

	public static boolean isHoldingMarker(PlayerEntity player)
	{
		if(player == null)
		{
			return false;
		}
		ItemStack main = player.getMainHandStack();
		ItemStack off = player.getOffHandStack();
		return main.isOf(ThuwumcraftItems.GOLEM_MARKER) || off.isOf(ThuwumcraftItems.GOLEM_MARKER);
	}
}
