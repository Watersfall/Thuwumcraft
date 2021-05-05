package net.watersfall.alchemy.client.renderer.entity;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class WaterEntityRenderer<T extends Entity> extends EntityRenderer<T>
{
	public WaterEntityRenderer(EntityRendererFactory.Context ctx)
	{
		super(ctx);
	}

	@Override
	public Identifier getTexture(T entity)
	{
		return new Identifier("");
	}
}
