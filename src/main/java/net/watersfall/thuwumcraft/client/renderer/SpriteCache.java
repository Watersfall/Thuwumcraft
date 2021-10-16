package net.watersfall.thuwumcraft.client.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.client.renderer.block.BrewingCauldronEntityRenderer;

public class SpriteCache
{
	public static Sprite WATER_SPRITE = ((SpriteAtlasTexture) MinecraftClient.getInstance()
			.getTextureManager()
			.getTexture(new Identifier("minecraft", "textures/atlas/blocks.png"))).getSprite(new Identifier("block/water_still"));

	public static void reload()
	{
		WATER_SPRITE = ((SpriteAtlasTexture) MinecraftClient.getInstance()
				.getTextureManager()
				.getTexture(new Identifier("minecraft", "textures/atlas/blocks.png"))).getSprite(new Identifier("block/water_still"));
		BrewingCauldronEntityRenderer.SPRITE_CACHE.clear();
	}
}
