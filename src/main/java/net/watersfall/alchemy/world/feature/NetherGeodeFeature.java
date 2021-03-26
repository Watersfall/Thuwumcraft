package net.watersfall.alchemy.world.feature;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BuddingAmethystBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.watersfall.alchemy.world.config.NetherGeodeConfig;
import net.watersfall.alchemy.world.config.NetherGeodeLayersConfig;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class NetherGeodeFeature extends Feature<NetherGeodeConfig>
{
	public NetherGeodeFeature(Codec<NetherGeodeConfig> configCodec)
	{
		super(configCodec);
	}

	public boolean generate(FeatureContext<NetherGeodeConfig> context)
	{
		NetherGeodeConfig config = context.getConfig();
		Random random = context.getRandom();
		BlockPos origin = context.getOrigin();
		StructureWorldAccess world = context.getWorld();
		int min = config.sizeConfig.minGenOffset;
		int max = config.sizeConfig.maxGenOffset;
		List<Pair<BlockPos, Integer>> list = Lists.newLinkedList();
		int size = config.sizeConfig.minDistributionPoints + random.nextInt(config.sizeConfig.maxDistributionPoints - config.sizeConfig.minDistributionPoints);
		ChunkRandom chunkRandom = new ChunkRandom(world.getSeed());
		DoublePerlinNoiseSampler doublePerlinNoiseSampler = DoublePerlinNoiseSampler.create(chunkRandom, -4, 1.0D);
		List<BlockPos> list2 = Lists.newLinkedList();
		double d = (double) size / (double) config.sizeConfig.maxOuterWallDistance;
		GeodeLayerThicknessConfig geodeLayerThicknessConfig = config.layerThicknessConfig;
		NetherGeodeLayersConfig[] layers = new NetherGeodeLayersConfig[]{config.layerConfig1, config.layerConfig2, config.layerConfig3};
		GeodeCrackConfig crack = config.crackConfig;
		double e = 1.0D / Math.sqrt(geodeLayerThicknessConfig.filling);
		double f = 1.0D / Math.sqrt(geodeLayerThicknessConfig.innerLayer + d);
		double g = 1.0D / Math.sqrt(geodeLayerThicknessConfig.middleLayer + d);
		double h = 1.0D / Math.sqrt(geodeLayerThicknessConfig.outerLayer + d);
		double l = 1.0D / Math.sqrt(crack.baseCrackSize + random.nextDouble() / 2.0D + (size > 3 ? d : 0.0D));
		boolean bl = (double) random.nextFloat() < crack.generateCrackChance;
		int m = 0;

		int r;
		int s;
		for(r = 0; r < size; ++r)
		{
			s = config.sizeConfig.minOuterWallDistance + random.nextInt(config.sizeConfig.maxOuterWallDistance - config.sizeConfig.minOuterWallDistance);
			int p = config.sizeConfig.minOuterWallDistance + random.nextInt(config.sizeConfig.maxOuterWallDistance - config.sizeConfig.minOuterWallDistance);
			int q = config.sizeConfig.minOuterWallDistance + random.nextInt(config.sizeConfig.maxOuterWallDistance - config.sizeConfig.minOuterWallDistance);
			BlockPos blockPos2 = origin.add(s, p, q);
			BlockState blockState = world.getBlockState(blockPos2);
			if(blockState.isAir() || blockState.isOf(Blocks.WATER) || blockState.isOf(Blocks.LAVA))
			{
				++m;
				if(m > config.invalidBlocksThreshold)
				{
					return false;
				}
			}

			list.add(Pair.of(blockPos2, config.sizeConfig.minPointOffset + random.nextInt(config.sizeConfig.maxPointOffset - config.sizeConfig.minPointOffset)));
		}

		if(bl)
		{
			r = random.nextInt(4);
			s = size * 2 + 1;
			if(r == 0)
			{
				list2.add(origin.add(s, 7, 0));
				list2.add(origin.add(s, 5, 0));
				list2.add(origin.add(s, 1, 0));
			}
			else if(r == 1)
			{
				list2.add(origin.add(0, 7, s));
				list2.add(origin.add(0, 5, s));
				list2.add(origin.add(0, 1, s));
			}
			else if(r == 2)
			{
				list2.add(origin.add(s, 7, s));
				list2.add(origin.add(s, 5, s));
				list2.add(origin.add(s, 1, s));
			}
			else
			{
				list2.add(origin.add(0, 7, 0));
				list2.add(origin.add(0, 5, 0));
				list2.add(origin.add(0, 1, 0));
			}
		}

		List<BlockPos> list3 = Lists.newArrayList();
		Iterator<BlockPos> area = BlockPos.iterate(origin.add(min, min, min), origin.add(max, max, max)).iterator();
		BlockPos pos = null;
		HashMap<Block, Integer> blocks = new HashMap<>();
		while(area.hasNext())
		{
			pos = area.next();
			BlockState state = world.getBlockState(pos);
			blocks.compute(state.getBlock(), (k, v) -> v == null ? 1 : v + 1);
		}
		int highest = 0;
		Block highestBlock = null;
		for(Block block : blocks.keySet())
		{
			for(int i = 0; i < layers.length; i++)
			{
				int index = i;
				if(block == layers[i].required.getBlockState(random, origin).getBlock())
				{
					blocks.compute(block, (k, v) -> v * layers[index].multiplier);
				}
			}
			if(blocks.get(block) > highest)
			{
				highest = blocks.get(block);
				highestBlock = block;
			}
		}
		blocks.keySet().removeIf(block -> block.getDefaultState().isAir());
		NetherGeodeLayersConfig geodeLayerConfig = null;
		for(int i = 0; i < layers.length; i++)
		{
			if(layers[i].required.getBlockState(random, pos).getBlock() == highestBlock)
			{
				geodeLayerConfig = layers[i];
				break;
			}
		}
		if(geodeLayerConfig == null)
		{
			return false;
		}
		area = BlockPos.iterate(origin.add(min, min, min), origin.add(max, max, max)).iterator();

		while(true)
		{
			while(true)
			{
				double v;
				BlockPos blockPos3;
				double u;
				do
				{
					if(!area.hasNext())
					{
						List<BlockState> list4 = geodeLayerConfig.layer.innerBlocks;
						Iterator var45 = list3.iterator();

						while(true)
						{
							while(var45.hasNext())
							{
								BlockPos blockPos5 = (BlockPos) var45.next();
								BlockState blockState2 = (BlockState) list4.get(random.nextInt(list4.size()));
								Direction[] var50 = Direction.values();
								int var36 = var50.length;

								for(int var51 = 0; var51 < var36; ++var51)
								{
									Direction direction = var50[var51];
									if(blockState2.contains(Properties.FACING))
									{
										blockState2 = (BlockState) blockState2.with(Properties.FACING, direction);
									}

									BlockPos blockPos6 = blockPos5.offset(direction);
									BlockState blockState3 = world.getBlockState(blockPos6);
									if(blockState2.contains(Properties.WATERLOGGED))
									{
										blockState2 = (BlockState) blockState2.with(Properties.WATERLOGGED, blockState3.getFluidState().isStill());
									}

									if(BuddingAmethystBlock.canGrowIn(blockState3))
									{
										world.setBlockState(blockPos6, blockState2, 2);
										break;
									}
								}
							}

							return true;
						}
					}

					blockPos3 = (BlockPos) area.next();
					double t = doublePerlinNoiseSampler.sample((double) blockPos3.getX(), (double) blockPos3.getY(), (double) blockPos3.getZ()) * config.noiseMultiplier;
					u = 0.0D;
					v = 0.0D;

					Iterator var39;
					Pair pair;
					for(var39 = list.iterator(); var39.hasNext(); u += MathHelper.fastInverseSqrt(blockPos3.getSquaredDistance((Vec3i) pair.getFirst()) + (double) (Integer) pair.getSecond()) + t)
					{
						pair = (Pair) var39.next();
					}

					BlockPos blockPos4;
					for(var39 = list2.iterator(); var39.hasNext(); v += MathHelper.fastInverseSqrt(blockPos3.getSquaredDistance(blockPos4) + (double) crack.crackPointOffset) + t)
					{
						blockPos4 = (BlockPos) var39.next();
					}
				}
				while(u < h);

				if(bl && v >= l && u < e)
				{
					if(world.getFluidState(blockPos3).isEmpty())
					{
						world.setBlockState(blockPos3, Blocks.AIR.getDefaultState(), 2);
					}
				}
				else if(u >= e)
				{
					world.setBlockState(blockPos3, geodeLayerConfig.layer.fillingProvider.getBlockState(random, blockPos3), 2);
				}
				else if(u >= f)
				{
					boolean bl2 = (double) random.nextFloat() < config.useAlternateLayer0Chance;
					if(bl2)
					{
						world.setBlockState(blockPos3, geodeLayerConfig.layer.alternateInnerLayerProvider.getBlockState(random, blockPos3), 2);
					}
					else
					{
						world.setBlockState(blockPos3, geodeLayerConfig.layer.innerLayerProvider.getBlockState(random, blockPos3), 2);
					}

					if((!config.placementsRequireLayer0Alternate || bl2) && (double) random.nextFloat() < config.usePotentialPlacementsChance)
					{
						list3.add(blockPos3.toImmutable());
					}
				}
				else if(u >= g)
				{
					world.setBlockState(blockPos3, geodeLayerConfig.layer.middleLayerProvider.getBlockState(random, blockPos3), 2);
				}
				else if(u >= h)
				{
					world.setBlockState(blockPos3, geodeLayerConfig.layer.outerLayerProvider.getBlockState(random, blockPos3), 2);
				}
			}
		}
	}
}
