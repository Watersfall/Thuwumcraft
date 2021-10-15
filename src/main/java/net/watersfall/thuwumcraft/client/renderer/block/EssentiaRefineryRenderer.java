package net.watersfall.thuwumcraft.client.renderer.block;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3f;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.block.entity.EssentiaRefineryBlockEntity;
import net.watersfall.thuwumcraft.client.util.RenderHelper;

public class EssentiaRefineryRenderer implements BlockEntityRenderer<EssentiaRefineryBlockEntity>
{
	private static final Sprite WATER_SPRITE = ((SpriteAtlasTexture) MinecraftClient.getInstance()
		.getTextureManager()
		.getTexture(new Identifier("minecraft", "textures/atlas/blocks.png"))).getSprite(new Identifier("block/water_still"));
	private final BlockEntityRenderDispatcher dispatcher;
	private final TextRenderer textRenderer;

	public EssentiaRefineryRenderer(BlockEntityRendererFactory.Context context)
	{
		this.dispatcher = context.getRenderDispatcher();
		this.textRenderer = context.getTextRenderer();
	}

	@Override
	public void render(EssentiaRefineryBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		if(!entity.getStacksForRender().isEmpty())
		{
			AspectStack stack = entity.getStacksForRender().stream().findFirst().get();
			if(!stack.isEmpty())
			{
				RenderSystem.enableDepthTest();
				matrices.push();
				float scale = ((float)stack.getCount() / 64F) * 0.375F;
				matrices.translate(0.3125F, 0.3125F, 0.3125F);
				matrices.scale(0.375F, scale, 0.375F);
				RenderHelper.drawTexture(
						vertexConsumers.getBuffer(RenderLayer.getSolid()),
						matrices,
						WATER_SPRITE,
						stack.getAspect().getColor(),
						light,
						overlay
				);
				matrices.push();
				float minU = WATER_SPRITE.getMinU();
				float maxU = WATER_SPRITE.getMaxU();
				float minV = WATER_SPRITE.getMinV();
				float maxV = minV + (WATER_SPRITE.getMaxV() - minV) * scale;
				matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(90F));
				matrices.translate(0F, -1F, 0F);
				RenderHelper.drawTexture(
						vertexConsumers.getBuffer(RenderLayer.getSolid()),
						matrices,
						WATER_SPRITE,
						minU,
						minV,
						maxU,
						maxV,
						stack.getAspect().getColor(),
						light,
						overlay
				);
				matrices.pop();
				matrices.push();
				matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(270F));
				matrices.translate(0F, 0F, -1F);
				RenderHelper.drawTexture(
						vertexConsumers.getBuffer(RenderLayer.getSolid()),
						matrices,
						WATER_SPRITE,
						minU,
						minV,
						maxU,
						maxV,
						stack.getAspect().getColor(),
						light,
						overlay
				);
				matrices.pop();
				matrices.push();
				matrices.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(270F));
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90F));
				matrices.translate(-1F, -1F, 0F);
				RenderHelper.drawTexture(
						vertexConsumers.getBuffer(RenderLayer.getSolid()),
						matrices,
						WATER_SPRITE,
						minU,
						minV,
						maxU,
						maxV,
						stack.getAspect().getColor(),
						light,
						overlay
				);
				matrices.pop();
				matrices.push();
				matrices.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(90F));
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90F));
				matrices.translate(-1F, 0F, -1F);
				RenderHelper.drawTexture(
						vertexConsumers.getBuffer(RenderLayer.getSolid()),
						matrices,
						WATER_SPRITE,
						minU,
						minV,
						maxU,
						maxV,
						stack.getAspect().getColor(),
						light,
						overlay
				);
				matrices.pop();
				matrices.pop();
				if(MinecraftClient.getInstance().crosshairTarget instanceof BlockHitResult hit && hit.getBlockPos().equals(entity.getPos()))
				{
					//entity.render(matrices, vertexConsumers, textRenderer, entity.getPos(), dispatcher.camera.getPos(), hit);
				}
			}
		}
	}
}