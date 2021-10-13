package net.watersfall.thuwumcraft.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderLayer;

public class AspectRenderLayer extends RenderLayer
{
	public static final AspectRenderLayer INSTANCE = new AspectRenderLayer(RenderLayer.getCutout());

	private final RenderLayer layer;

	private static void before()
	{
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
	}

	private static void after()
	{
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(true);
	}

	public AspectRenderLayer(RenderLayer layer)
	{
		super("aspect", layer.getVertexFormat(), layer.getDrawMode(), layer.getExpectedBufferSize(), layer.hasCrumbling(), false, () -> {
			layer.startDrawing();
			before();
		}, () -> {
			after();
			layer.endDrawing();
		});
		this.layer = layer;
	}
}
