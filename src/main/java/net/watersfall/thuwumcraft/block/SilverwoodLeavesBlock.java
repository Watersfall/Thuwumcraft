package net.watersfall.thuwumcraft.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class SilverwoodLeavesBlock extends LeavesBlock
{
	public SilverwoodLeavesBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random)
	{
		if(random.nextDouble() <= 0.01)
		{
			Particle particle = MinecraftClient.getInstance().particleManager.addParticle(
					ParticleTypes.WITCH,
					pos.getX() + random.nextDouble(),
					pos.getY() + random.nextDouble(),
					pos.getZ() + random.nextDouble(),
					0, 0 ,0
			);
			if(particle != null)
			{
				particle.setColor(0, 0xAA / 255F, 1);
			}
		}
		super.randomDisplayTick(state, world, pos, random);
	}
}
