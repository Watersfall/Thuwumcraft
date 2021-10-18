package net.watersfall.thuwumcraft.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.watersfall.thuwumcraft.entity.golem.GolemEntity;

public class GolemEntityModel extends BipedEntityModel<GolemEntity>
{
	public GolemEntityModel(ModelPart root)
	{
		super(root);
	}

	public static TexturedModelData getTexturedModelData()
	{
		ModelData model = new ModelData();
		ModelPartData part = model.getRoot();
		part.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4F, -8F, -4F, 8F, 8F, 8F), ModelTransform.NONE);
		part.addChild("hat", ModelPartBuilder.create().uv(0, 0), ModelTransform.NONE);
		part.addChild("body", ModelPartBuilder.create().uv(0, 16).cuboid(-8, 0, -4, 16, 14, 8), ModelTransform.NONE);
		part.addChild("left_arm", ModelPartBuilder.create().uv(0, 38).mirrored().cuboid(2F, 0F, -2F, 4F, 16F, 4F), ModelTransform.NONE);
		part.addChild("right_arm", ModelPartBuilder.create().uv(0, 38).cuboid(-6F, 0F, -2F, 4F, 16F, 4F), ModelTransform.NONE);
		part.addChild("left_leg", ModelPartBuilder.create().uv(16, 38).mirrored().cuboid(-6, 2, -3, 6, 10, 6), ModelTransform.NONE);
		part.addChild("right_leg", ModelPartBuilder.create().uv(16, 38).cuboid(0, 2, -3, 6, 10, 6), ModelTransform.NONE);
		return TexturedModelData.of(model, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha)
	{
		super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void setAngles(GolemEntity golem, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		if(!golem.getSeal().isEmpty())
		{
			super.setAngles(golem, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		}
		else
		{
			super.setAngles(golem, 0, 0, 0F, 0, 0);
		}
		if(!golem.getMainHandStack().isEmpty())
		{
			this.leftArm.setAngles(-0.8F, 0.5F, 0);
			this.rightArm.setAngles(-0.8F, -0.5F, 0);
		}
	}
}
