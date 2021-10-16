package net.watersfall.thuwumcraft.client.renderer;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.watersfall.thuwumcraft.client.accessor.RenderLayerAccessor;

public abstract class ThuwumcraftRenderLayers extends RenderLayer
{
	public static final RenderLayer INSTANCE;

	static
	{
		if(FabricLoader.getInstance().isModLoaded("canvas"))
		{
			INSTANCE = RenderLayerAccessor.of(
				"aspect",
				VertexFormats.POSITION_COLOR_TEXTURE,
				VertexFormat.DrawMode.QUADS,
				131072,
				false,
				false,
				RenderLayer.MultiPhaseParameters.builder()
						.lightmap(RenderPhase.DISABLE_LIGHTMAP)
						.shader(RenderPhase.POSITION_COLOR_TEXTURE_SHADER)
						.texture(BLOCK_ATLAS_TEXTURE)
						.depthTest(RenderPhase.ALWAYS_DEPTH_TEST)
						.build(true));
		}
		else
		{
			INSTANCE = RenderLayerAccessor.of(
					"aspect",
					VertexFormats.POSITION_COLOR_TEXTURE,
					VertexFormat.DrawMode.QUADS,
					131072,
					false,
					false,
					RenderLayer.MultiPhaseParameters.builder()
							.lightmap(RenderPhase.DISABLE_LIGHTMAP)
							.shader(RenderPhase.POSITION_COLOR_TEXTURE_SHADER)
							.texture(BLOCK_ATLAS_TEXTURE)
							.build(true));
		}
	}

	protected ThuwumcraftRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction)
	{
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}
}
