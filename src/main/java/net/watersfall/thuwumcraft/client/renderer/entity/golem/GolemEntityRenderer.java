package net.watersfall.thuwumcraft.client.renderer.entity.golem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.client.model.GolemEntityModel;
import net.watersfall.thuwumcraft.client.util.RenderHelper;
import net.watersfall.thuwumcraft.entity.golem.GolemEntity;

public class GolemEntityRenderer extends BipedEntityRenderer<GolemEntity, GolemEntityModel>
{
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(Thuwumcraft.getId("golem"), "golem");

	private static final Identifier TEXTURE = Thuwumcraft.getId("textures/entity/golem/golem.png");

	public GolemEntityRenderer(EntityRendererFactory.Context ctx)
	{
		super(ctx, new GolemEntityModel(GolemEntityModel.getTexturedModelData().createModel()), 0.25F, 0.5F, 0.5F, 0.5F);
		this.features.remove(this.features.size() - 1);
		this.addFeature(new HeldItemFeatureRenderer(this));
		this.addFeature(new SealFeatureRenderer(this));
		this.addFeature(new WhitelistFeatureRenderer(this));
	}

	@Override
	protected void scale(GolemEntity entity, MatrixStack matrices, float amount)
	{
		matrices.scale(0.5F, 0.5F, 0.5F);
	}

	@Override
	protected boolean hasLabel(GolemEntity mobEntity)
	{
		return super.hasLabel(mobEntity) || RenderHelper.isHoldingBell(MinecraftClient.getInstance().player);
	}

	@Override
	protected void renderLabelIfPresent(GolemEntity golem, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
	{
		if(RenderHelper.isHoldingBell(MinecraftClient.getInstance().player))
		{
			matrices.push();
			BlockPos pos = golem.getHome();
			Direction dir = golem.getSide();
			matrices.translate(0, golem.getHeight() + 0.5F, 0);
			matrices.scale(-0.025F, -0.025F, 0.025F);
			Vec3d camera = dispatcher.camera.getPos();
			Vec3d center = new Vec3d(pos.getX() + 0.5 + dir.getOffsetX(), pos.getY() + 0.5 + dir.getOffsetY(), pos.getZ() + 0.5 + dir.getOffsetZ());
			float angle = (float)MathHelper.atan2(camera.x - center.x, camera.z - center.z);
			matrices.multiply(Quaternion.method_35821(-angle + MathHelper.PI, 0, 0));
			//matrices.translate(golem.getCustomName().getString().length() / 2F * -4F, 0, 0);
			TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
			float width = -textRenderer.getWidth(text) / 2F;
			textRenderer.draw(text, width, 0F, -1, false, matrices.peek().getModel(), vertexConsumers, false, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
			matrices.pop();
		}
		else
		{
			super.renderLabelIfPresent(golem, text, matrices, vertexConsumers, light);
		}
	}

	@Override
	public Identifier getTexture(GolemEntity mobEntity)
	{
		return TEXTURE;
	}

	public static class SealFeatureRenderer extends FeatureRenderer<GolemEntity, GolemEntityModel>
	{
		public SealFeatureRenderer(FeatureRendererContext<GolemEntity, GolemEntityModel> context)
		{
			super(context);
		}

		@Override
		public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, GolemEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
		{
			ItemStack stack = entity.getSeal();
			if(!stack.isEmpty())
			{
				matrices.push();
				matrices.scale(1, 1, 2);
				matrices.multiply(Quaternion.method_35821((float)Math.PI, (float)Math.PI, 0));
				matrices.scale(0.85F, 0.85F, 0.85F);
				matrices.translate(0, -0.6425, 0.1625);
				MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
				matrices.pop();
			}
		}
	}

	public static class HeldItemFeatureRenderer extends FeatureRenderer<GolemEntity, GolemEntityModel>
	{
		public HeldItemFeatureRenderer(FeatureRendererContext<GolemEntity, GolemEntityModel> context)
		{
			super(context);
		}

		@Override
		public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, GolemEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
		{
			matrices.push();
			ItemStack stack = entity.getMainHandStack();
			matrices.scale(1.25F, 1.25F, 1.25F);
			matrices.translate(0, 0.25, -0.5);
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
			matrices.pop();
		}
	}

	public static class WhitelistFeatureRenderer extends FeatureRenderer<GolemEntity, GolemEntityModel>
	{
		public WhitelistFeatureRenderer(FeatureRendererContext<GolemEntity, GolemEntityModel> context)
		{
			super(context);
		}

		@Override
		public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, GolemEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
		{
			PlayerEntity player = MinecraftClient.getInstance().player;
			if(RenderHelper.isHoldingMarker(player))
			{
				if(!entity.getWhitelist().isEmpty())
				{
					matrices.push();
					matrices.translate(0, -0.75, 0);
					matrices.multiply(Quaternion.method_35821(0, MathHelper.PI, 0));
					MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getWhitelist(), ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
					matrices.pop();
				}
			}
		}
	}
}
