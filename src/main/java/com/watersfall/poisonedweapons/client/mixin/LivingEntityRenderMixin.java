package com.watersfall.poisonedweapons.client.mixin;

import com.watersfall.poisonedweapons.effect.AlchemyModStatusEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRenderMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T>
{
	ItemStack stack = new ItemStack(Items.SHIELD, 1);

	protected LivingEntityRenderMixin(EntityRenderDispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Inject(method = "render", at = @At("TAIL"))
	public void render(T livingEntity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info)
	{
		if(livingEntity.hasStatusEffect(AlchemyModStatusEffects.PROJECTILE_SHIELD))
		{
			matrices.push();
			matrices.translate(-1.5f, 1f, -0.4f);
			matrices.scale(3f, 3f, 3f);
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(270f));
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, 15728640, 655360, matrices, vertexConsumerProvider);
			matrices.pop();
			matrices.push();
			matrices.translate(1.5f, 1f, 0.4f);
			matrices.scale(3f, 3f, 3f);
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90f));
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, 15728640, 655360, matrices, vertexConsumerProvider);
			matrices.pop();
			matrices.push();
			matrices.translate(0.4f, 1f, -1.5f);
			matrices.scale(3f, 3f, 3f);
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180f));
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, 15728640, 655360, matrices, vertexConsumerProvider);
			matrices.pop();
			matrices.push();
			matrices.translate(-0.4f, 1f, 1.5f);
			matrices.scale(3f, 3f, 3f);
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, 15728640, 655360, matrices, vertexConsumerProvider);
			matrices.pop();
		}
	}
}
