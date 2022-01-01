package net.watersfall.thuwumcraft.client.renderer.block;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.block.ArcaneSealBlock;
import net.watersfall.thuwumcraft.block.entity.ArcaneSealBlockEntity;

public class ArcaneSealRenderer implements BlockEntityRenderer<ArcaneSealBlockEntity>
{
	private static final Identifier FONT_ID = new Identifier("minecraft", "alt");
	private static final Style STYLE = Style.EMPTY.withFont(FONT_ID);
	private static final String OUTER_RING = "sscardsisabitch";
	private static final String INNER_RING = "someotherstring";
	private static final Identifier CIRCLE = Thuwumcraft.getId("textures/block/arcane_seal_1.png");

	protected final BlockEntityRenderDispatcher dispatcher;
	protected final TextRenderer textRenderer;

	public ArcaneSealRenderer(BlockEntityRendererFactory.Context context)
	{
		this.dispatcher = context.getRenderDispatcher();
		this.textRenderer = context.getTextRenderer();
	}

	@Override
	public void render(ArcaneSealBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		matrices.push();
		switch(entity.getCachedState().get(ArcaneSealBlock.FACING))
		{
			case UP -> {
				matrices.translate(0.5, 0.01, 0.5);
				matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
			}
			case DOWN -> {
				matrices.translate(0.5, 0.99, 0.5);
				matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(270));
			}
			case NORTH -> matrices.translate(0.5, 0.5, 0.99);
			case SOUTH -> {
				matrices.translate(0.5, 0.5, 0.01);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
			}
			case WEST -> {
				matrices.translate(0.99, 0.5, 0.5);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
			}
			case EAST -> {
				matrices.translate(0.01, 0.5, 0.5);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(270));
			}
		}
		if(entity.getCachedState().get(ArcaneSealBlock.RUNES) >= 1)
		{
			RenderSystem.enableDepthTest();
			RenderSystem.enableBlend();
			matrices.push();
			matrices.translate(-0.5, -0.5, 0.005);
			matrices.scale(0.00389F, 0.00389F, 0.0039F);
			Vec3d color = Vec3d.unpackRgb(entity.getColor(0));
			RenderSystem.setShaderColor((float)color.x, (float)color.y, (float)color.z, 0.5F);
			RenderSystem.setShaderTexture(0, CIRCLE);
			DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 256, 256, 256, 256);
			matrices.pop();
			RenderSystem.disableBlend();
		}
		if(entity.getCachedState().get(ArcaneSealBlock.RUNES) >= 2)
		{
			matrices.push();
			matrices.scale(0.01F, 0.01F, 0.01F);
			matrices.multiply(Quaternion.fromEulerYxz(0, 0, -((entity.getWorld().getTime() + tickDelta) / 200F)));
			for(int i = 0; i < INNER_RING.length(); i++)
			{
				matrices.push();
				double angle = Math.PI * 2  / (INNER_RING.length()) * i;
				matrices.multiply(Quaternion.fromEulerYxz(0, 0, (float)(angle)));
				matrices.translate(34, 0, 0);
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90));
				String character = INNER_RING.substring(i, i + 1);
				textRenderer.draw(matrices, new LiteralText(character).getWithStyle(STYLE).get(0), 0, 0, entity.getColor(1));
				matrices.pop();
			}
			matrices.pop();
		}
		if(entity.getCachedState().get(ArcaneSealBlock.RUNES) == 3)
		{
			matrices.push();
			matrices.scale(0.01F, 0.01F, 0.01F);
			matrices.multiply(Quaternion.fromEulerYxz(0, 0, ((entity.getWorld().getTime() + tickDelta) / 200F)));
			for(int i = 0; i < OUTER_RING.length(); i++)
			{
				matrices.push();
				double angle = Math.PI * 2  / (OUTER_RING.length()) * i;
				matrices.multiply(Quaternion.fromEulerYxz(0, 0, (float)(angle)));
				matrices.translate(47, 0, 0);
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90));
				String character = OUTER_RING.substring(i, i + 1);
				textRenderer.draw(matrices, new LiteralText(character).getWithStyle(STYLE).get(0), 0, 0, entity.getColor(2));
				matrices.pop();
			}
			matrices.pop();
		}
		RenderSystem.disableBlend();
		matrices.pop();
	}
}
