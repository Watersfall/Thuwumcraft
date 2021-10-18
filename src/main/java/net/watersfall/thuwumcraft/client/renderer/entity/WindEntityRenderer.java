package net.watersfall.thuwumcraft.client.renderer.entity;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.entity.spell.WindEntity;

public class WindEntityRenderer extends EntityRenderer<WindEntity>
{
	public static final Identifier TEXTURE = Thuwumcraft.getId("textures/entity/wind.png");
	private final ModelPart model;

	public WindEntityRenderer(EntityRendererFactory.Context ctx)
	{
		super(ctx);
		ModelData data = new ModelData();
		data.getRoot().addChild("model", new ModelPartBuilder().cuboid(-4, -4, -4, 8, 8, 8).uv(0, 0), ModelTransform.NONE);
		this.model = data.getRoot().createPart(16, 16);
	}

	@Override
	public void render(WindEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
	{
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
		matrices.push();
		matrices.translate(0, 0.25F, 0);
		model.yaw = entity.age / 8F;
		model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, 0);
		matrices.pop();
	}

	@Override
	public Identifier getTexture(WindEntity entity)
	{
		return TEXTURE;
	}
}
