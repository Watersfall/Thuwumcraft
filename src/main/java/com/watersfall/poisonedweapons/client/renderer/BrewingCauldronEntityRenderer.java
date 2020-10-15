package com.watersfall.poisonedweapons.client.renderer;

import com.watersfall.poisonedweapons.blockentity.BrewingCauldronEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BrewingCauldronEntityRenderer extends BlockEntityRenderer<BrewingCauldronEntity>
{
    public BrewingCauldronEntityRenderer(BlockEntityRenderDispatcher dispatcher)
    {
        super(dispatcher);
    }

    private void add(VertexConsumer renderer, MatrixStack stack, float x, float y, float z, float u, float v, int color) {
        renderer.vertex(stack.peek().getModel(), x, y, z)
                .color((((color >> 16) & 0xFF) / 255F), (((color >> 8) & 0xFF) / 255F), (color & 0xFF) / 255F, 1.0f)
                .texture(u, v)
                .light(0, 240)
                .normal(1, 0, 0)
                .next();
    }

    @Override
    public void render(BrewingCauldronEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
    {
        if(entity.getWaterLevel() > 0)
        {
            matrices.push();
            matrices.translate((2F / 16F), (4F / 16F), (2F / 16F));
            float newLevel = entity.getAnimationProgress(tickDelta);
            float scale = (newLevel / 1000F) * (9F / 16F);
            matrices.scale(1F - (4F / 16F), scale, 1F - (4F / 16F));
            Sprite sprite = ((SpriteAtlasTexture)MinecraftClient.getInstance().getTextureManager().getTexture(new Identifier("minecraft", "textures/atlas/blocks.png"))).getSprite(new Identifier("block/water_still"));
            entity.lastWaterLevel = (short)newLevel;
            int color = BiomeColors.getWaterColor(dispatcher.world, entity.getPos());
            VertexConsumer builder = vertexConsumers.getBuffer(RenderLayer.getTranslucent());
            add(builder, matrices, 0, 1, 0, sprite.getMinU(), sprite.getMinV(), color);
            add(builder, matrices, 1, 1, 0, sprite.getMaxU(), sprite.getMinV(), color);
            add(builder, matrices, 1, 1,  1, sprite.getMaxU(), sprite.getMaxV(), color);
            add(builder, matrices, 0, 1, 1, sprite.getMinU(), sprite.getMaxV(), color);

            add(builder, matrices, 0, 1, 1, sprite.getMinU(), sprite.getMaxV(), color);
            add(builder, matrices, 1, 1, 1, sprite.getMaxU(), sprite.getMaxV(), color);
            add(builder, matrices, 1, 1,  0, sprite.getMaxU(), sprite.getMinV(), color);
            add(builder, matrices, 0, 1, 0, sprite.getMinU(), sprite.getMinV(), color);
            matrices.pop();
        }
    }
}
