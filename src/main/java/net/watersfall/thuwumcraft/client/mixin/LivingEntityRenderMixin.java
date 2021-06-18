package net.watersfall.thuwumcraft.client.mixin;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.math.Vec3f;
import net.watersfall.thuwumcraft.effect.ThuwumcraftStatusEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRenderMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T>
{
	ItemStack stack = new ItemStack(Items.SHIELD, 1);

	protected LivingEntityRenderMixin(EntityRendererFactory.Context ctx)
	{
		super(ctx);
	}

	@Inject(method = "render", at = @At("TAIL"))
	public void render(T entity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info)
	{
		if(entity.hasStatusEffect(ThuwumcraftStatusEffects.PROJECTILE_SHIELD))
		{
			matrices.push();
			matrices.translate(-1.5f, 1f, -0.4f);
			matrices.scale(3f, 3f, 3f);
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(270f));
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, 15728640, 655360, matrices, vertexConsumerProvider, 0);
			matrices.pop();
			matrices.push();
			matrices.translate(1.5f, 1f, 0.4f);
			matrices.scale(3f, 3f, 3f);
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90f));
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, 15728640, 655360, matrices, vertexConsumerProvider, 0);
			matrices.pop();
			matrices.push();
			matrices.translate(0.4f, 1f, -1.5f);
			matrices.scale(3f, 3f, 3f);
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180f));
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, 15728640, 655360, matrices, vertexConsumerProvider, 0);
			matrices.pop();
			matrices.push();
			matrices.translate(-0.4f, 1f, 1.5f);
			matrices.scale(3f, 3f, 3f);
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, 15728640, 655360, matrices, vertexConsumerProvider, 0);
			matrices.pop();
		}
	}
}
