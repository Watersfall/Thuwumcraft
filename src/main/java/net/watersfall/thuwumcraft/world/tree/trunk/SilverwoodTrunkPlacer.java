package net.watersfall.thuwumcraft.world.tree.trunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class SilverwoodTrunkPlacer extends TrunkPlacer
{
	public static final BlockState LOG = ThuwumcraftBlocks.SILVERWOOD_LOG.getDefaultState();

	public static final boolean[][] UNDERGROUND_LAYER = new boolean[][]{
			{false, false, true, false, false},
			{false, false, false, false, false},
			{true, false, false, false, true},
			{false, false, false, false, false},
			{false, false, true, false, false}
	};

	public static final boolean[][] BOTTOM_SHAPE = new boolean[][]{
			{false, false, true, false, false},
			{false, true, true, true, false},
			{true, true, true, true, true},
			{false, true, true, true, false},
			{false, false, true, false, false}
	};

	public static final boolean[][] ABOVE_BOTTOM = new boolean[][]{
			{false, false, false, false, false},
			{false, true, true, true, false},
			{false, true, true, true, false},
			{false, true, true, true, false},
			{false, false, false, false, false}
	};

	public static final boolean[][] MIDDLE_SHAPE = new boolean[][]{
			{false, false, false, false, false},
			{false, false, true, false, false},
			{false, true, true, true, false},
			{false, false, true, false, false},
			{false, false, false, false, false}
	};

	public static final Codec<SilverwoodTrunkPlacer> CODEC = RecordCodecBuilder.create((instance) -> {
		return fillTrunkPlacerFields(instance).apply(instance, SilverwoodTrunkPlacer::new);
	});

	public SilverwoodTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight)
	{
		super(baseHeight, firstRandomHeight, secondRandomHeight);
	}

	@Override
	protected TrunkPlacerType<?> getType()
	{
		return ThuwumcraftTrunkTypes.SILVERWOOD_TRUNK;
	}

	@Override
	public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> consumer, Random random, int y, BlockPos startPos, TreeFeatureConfig config)
	{
		int height = config.trunkPlacer.getHeight(random);
		List<FoliagePlacer.TreeNode> foliage = new ArrayList<>();
		BlockPos.Mutable pos = startPos.mutableCopy();
		startPos = pos.move(-2, 0, -2).toImmutable();
		for(int x = 0; x < UNDERGROUND_LAYER.length; x++)
		{
			for(int z = 0; z < UNDERGROUND_LAYER[x].length; z++)
			{
				if(UNDERGROUND_LAYER[x][z])
				{
					pos.set(startPos.getX() + x, startPos.getY() - 1, startPos.getZ() + z);
					replaceAir(world, consumer, pos);
				}
			}
		}
		for(int x = 0; x < BOTTOM_SHAPE.length; x++)
		{
			for(int z = 0; z < BOTTOM_SHAPE[x].length; z++)
			{
				if(BOTTOM_SHAPE[x][z])
				{
					pos.set(startPos.getX() + x, startPos.getY(), startPos.getZ() + z);
					consumer.accept(pos, LOG);
				}
			}
		}
		for(int x = 0; x < ABOVE_BOTTOM.length; x++)
		{
			for(int z = 0; z < ABOVE_BOTTOM[x].length; z++)
			{
				if(ABOVE_BOTTOM[x][z])
				{
					pos.set(startPos.getX() + x, startPos.getY() + 1, startPos.getZ() + z);
					if(random.nextInt(2) == 0)
					{
						consumer.accept(pos, LOG);
					}
				}
			}
		}
		int max = 0;
		for(int i = 0; i < height * 2 - height / 2 - 2; i++)
		{
			for(int x = 0; x < MIDDLE_SHAPE.length; x++)
			{
				for(int z = 0; z < MIDDLE_SHAPE[x].length; z++)
				{
					if(MIDDLE_SHAPE[x][z])
					{
						pos.set(startPos.getX() + x, startPos.getY() + i, startPos.getZ() + z);
						consumer.accept(pos, LOG);
						if(i > height - 3)
						{
							foliage.add(new FoliagePlacer.TreeNode(pos.toImmutable(), 2, false));
						}
						if(x == 2 && z == 2)
						{
							if(i > height)
							{
								foliage.add(new FoliagePlacer.TreeNode(pos.toImmutable().add(0, 1, 0), 2, false));
							}
							else if(i > height - 5)
							{
								foliage.add(new FoliagePlacer.TreeNode(pos.toImmutable(), 3, false));
							}
						}
					}
				}
			}
		}
		for(int x = 0; x < ABOVE_BOTTOM.length; x++)
		{
			for(int z = 0; z < ABOVE_BOTTOM[x].length; z++)
			{
				if(ABOVE_BOTTOM[x][z])
				{
					pos.set(startPos.getX() + x, startPos.getY() + height - 2, startPos.getZ() + z);
					if(random.nextInt(2) == 0)
					{
						consumer.accept(pos, LOG);
					}
					foliage.add(new FoliagePlacer.TreeNode(pos.toImmutable(), 5, false));
				}
			}
		}
		for(int x = 0; x < BOTTOM_SHAPE.length; x++)
		{
			for(int z = 0; z < BOTTOM_SHAPE[x].length; z++)
			{
				if(BOTTOM_SHAPE[x][z])
				{
					pos.set(startPos.getX() + x, startPos.getY() + height - 1, startPos.getZ() + z);
					consumer.accept(pos, LOG);
					foliage.add(new FoliagePlacer.TreeNode(pos.toImmutable(), 5, false));
				}
			}
		}
		return foliage;
	}

	public static void replaceAir(TestableWorld world, BiConsumer<BlockPos, BlockState> consumer, BlockPos pos)
	{
		if(world.testBlockState(pos, state -> state.isAir() || state.getMaterial().isReplaceable()))
		{
			consumer.accept(pos, LOG);
		}
	}
}
