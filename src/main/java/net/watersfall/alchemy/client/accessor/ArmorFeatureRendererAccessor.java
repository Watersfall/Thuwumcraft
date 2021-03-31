package net.watersfall.alchemy.client.accessor;

import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ArmorFeatureRenderer.class)
public interface ArmorFeatureRendererAccessor
{
	@Accessor("ARMOR_TEXTURE_CACHE")
	public static Map<String, Identifier> getArmorTextureCache()
	{
		throw new RuntimeException();
	}
}
