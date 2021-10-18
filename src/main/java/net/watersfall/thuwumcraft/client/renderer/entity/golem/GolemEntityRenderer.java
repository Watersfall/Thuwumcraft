package net.watersfall.thuwumcraft.client.renderer.entity.golem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.client.model.GolemEntityModel;
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
	}

	@Override
	protected void scale(GolemEntity entity, MatrixStack matrices, float amount)
	{
		matrices.scale(0.5F, 0.5F, 0.5F);
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
				matrices.scale(1, 1, 2);
				matrices.multiply(Quaternion.method_35821((float)Math.PI, (float)Math.PI, 0));
				matrices.scale(0.85F, 0.85F, 0.85F);
				matrices.translate(0, -0.2425, 0.425);
				MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
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
			ItemStack stack = entity.getMainHandStack();
			matrices.scale(1.25F, 1.25F, 1.25F);
			matrices.translate(0, 0.25, -0.5);
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
		}
	}
}
