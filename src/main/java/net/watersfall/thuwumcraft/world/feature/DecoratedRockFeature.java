package net.watersfall.thuwumcraft.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.watersfall.thuwumcraft.api.aspect.Aspect;
import net.watersfall.thuwumcraft.api.aspect.Aspects;
import net.watersfall.thuwumcraft.world.config.DecoratedRockConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class DecoratedRockFeature extends Feature<DecoratedRockConfig>
{
	public DecoratedRockFeature(Codec<DecoratedRockConfig> configCodec)
	{
		super(configCodec);
	}

	@Nullable
	private static BlockState getRandomAspectCrystal(Random random)
	{
		int number = random.nextInt(6);
		Aspect aspect;
		if(number < 6)
		{
			switch(number)
			{
				case 0:
					aspect = Aspects.AIR;
					break;
				case 1:
					aspect = Aspects.WATER;
					break;
				case 2:
					aspect = Aspects.EARTH;
					break;
				case 3:
					aspect = Aspects.FIRE;
					break;
				case 4:
					aspect = Aspects.ORDER;
					break;
				default:
					aspect = Aspects.DISORDER;
			}
			number = random.nextInt(4);
			switch(number)
			{
				case 0:
					return Aspects.ASPECT_TO_SMALL_CLUSTER.get(aspect).getDefaultState();
				case 1:
					return Aspects.ASPECT_TO_MEDIUM_CLUSTER.get(aspect).getDefaultState();
				case 2:
					return Aspects.ASPECT_TO_LARGE_CLUSTER.get(aspect).getDefaultState();
				default:
					return Aspects.ASPECT_TO_CLUSTER.get(aspect).getDefaultState();
			}
		}
		return null;
	}

	@Override
	public boolean generate(FeatureContext<DecoratedRockConfig> context)
	{
		BlockPos origin = context.getOrigin();
		StructureWorldAccess world = context.getWorld();
		Random random = context.getRandom();
		DecoratedRockConfig config = context.getConfig();
		for(; origin.getY() > world.getBottomY() + 3; origin = origin.down())
		{
			if (!world.isAir(origin.down()))
			{
				BlockState state = world.getBlockState(origin.down());
				if (isSoil(state) || isStone(state))
				{
					break;
				}
			}
		}
		if (origin.getY() <= world.getBottomY() + 3)
		{
			return false;
		}
		else
			{
			for(int i = 0; i < 3; ++i)
			{
				int x = random.nextInt(2);
				int y = random.nextInt(2);
				int z = random.nextInt(2);
				float size = (float)(x + y + z) * 0.333F + 0.5F;

				for(BlockPos pos : BlockPos.iterate(origin.add(-x, -y, -z), origin.add(x, y, z)))
				{
					if(pos.getSquaredDistance(origin) <= (double)(size * size))
					{
						world.setBlockState(pos, config.primaryBlock.getBlockState(random, pos), 4);
					}
				}
				BlockState crystal = getRandomAspectCrystal(random);
				if(crystal != null)
				{
					for(BlockPos pos : BlockPos.iterate(origin.add((-x) - 1, (-y) - 1, (-z) - 1), origin.add(x + 1, y + 1, z + 1)))
					{
						if(world.getBlockState(pos).isAir())
						{
							for(Direction direction : Direction.values())
							{
								if(random.nextInt(15) == 0)
								{
									BlockPos test = pos.offset(direction);
									if(world.getBlockState(test).isOf(config.primaryBlock.getBlockState(random, origin).getBlock()))
									{
										world.setBlockState(pos, crystal.with(Properties.FACING, direction.getOpposite()), 4);
										break;
									}
								}
							}
						}
					}
				}
				origin = origin.add(-1 + random.nextInt(2), -random.nextInt(2), -1 + random.nextInt(2));
			}
			return true;
		}
	}
}
