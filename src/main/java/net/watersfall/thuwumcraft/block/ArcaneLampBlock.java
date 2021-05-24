package net.watersfall.thuwumcraft.block;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.block.entity.ArcaneLampEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ArcaneLampBlock extends LanternBlock implements BlockEntityProvider
{
	public ArcaneLampBlock(Settings settings)
	{
		super(settings.ticksRandomly());
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		for(int i = 0; i < 60; i++)
		{
			int x = pos.getX() + (int)(Math.random() * 33 - 16);
			int y = pos.getY() + (int)(Math.random() * 17 - 8);
			int z = pos.getZ() + (int)(Math.random() * 33 - 16);
			BlockPos testPos = new BlockPos(x, y, z);
			if(world.isPosLoaded(x, z) && world.getBlockState(testPos).isAir() && !world.getBlockState(testPos.down()).isAir())
			{
				if(world.getLightLevel(LightType.BLOCK, testPos) <= 7)
				{
					BlockEntity test = world.getBlockEntity(pos);
					if(test instanceof ArcaneLampEntity)
					{
						ArcaneLampEntity entity = (ArcaneLampEntity)test;
						entity.addLight(testPos);
						world.setBlockState(testPos, AlchemyBlocks.ARCANE_LIGHT_BLOCK.getDefaultState());
					}
				}
			}
		}
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player)
	{
		super.onBreak(world, pos, state, player);
		BlockEntity test = world.getBlockEntity(pos);
		if(test instanceof ArcaneLampEntity)
		{
			ArcaneLampEntity lamp = (ArcaneLampEntity)test;
			lamp.getLights().forEach(lightPos -> {
				if(world.getBlockState(lightPos).getBlock() == AlchemyBlocks.ARCANE_LIGHT_BLOCK)
				{
					world.breakBlock(lightPos, false);
				}
			});
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new ArcaneLampEntity(pos, state);
	}
}
