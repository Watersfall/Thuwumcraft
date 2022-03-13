package net.watersfall.thuwumcraft.block;

import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.aspect.Aspect;
import net.watersfall.thuwumcraft.registry.ThuwumcraftParticles;
import net.watersfall.thuwumcraft.registry.tag.ThuwumcraftBiomeTags;

import java.util.Random;

public class ElementalClusterBlock extends AmethystClusterBlock
{
	private final Aspect aspect;

	public ElementalClusterBlock(int height, int xzOffset, Settings settings, Aspect aspect)
	{
		super(height, xzOffset, settings);
		this.aspect = aspect;
	}

	public Aspect getAspect()
	{
		return this.aspect;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random)
	{
		if(random.nextInt(7) == 0)
		{
			if(world.getBiome(pos).isIn(ThuwumcraftBiomeTags.MAGICAL_FORESTS))
			{
				Particle particle =  MinecraftClient.getInstance().particleManager.addParticle(ThuwumcraftParticles.MAGIC_FOREST, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);
				if(particle != null)
				{
					Vec3d color = Vec3d.unpackRgb(aspect.getColor());
					particle.setColor((float)color.getX(), (float)color.getY(), (float)color.getZ());
				}
			}
		}
	}
}
