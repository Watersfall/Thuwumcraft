package net.watersfall.alchemy.api.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MultiBlockRegistry
{
	public static final Types TYPES = new Types();
	public static final Ticking SERVER_TICKER = new Ticking();
	public static final Ticking CLIENT_TICKER = new Ticking();

	public static class Types
	{
		private final List<MultiBlockType> types;

		private Types()
		{
			this.types = new ArrayList<>();
		}

		public void add(MultiBlockType type)
		{
			types.add(type);
		}

		public Pair<MultiBlockType, BlockPos[]> getMatch(PlayerEntity player, World world, BlockState state, BlockPos pos)
		{
			for(MultiBlockType type : this.types)
			{
				if(type.getStartingPoints().contains(state))
				{
					BlockPos[] poses = type.matches(player, world, pos);
					if(poses != MultiBlockType.MISSING)
					{
						return new Pair<>(type, poses);
					}
				}
			}
			return null;
		}
	}

	public static class Ticking
	{
		private final List<MultiBlock> multiBlocks;

		private Ticking()
		{
			multiBlocks = new ArrayList<>();
		}

		public void add(MultiBlock multiBlock)
		{
			this.multiBlocks.add(multiBlock);
		}

		public void remove(MultiBlock multiBlock)
		{
			this.multiBlocks.remove(multiBlock);
		}

		public void tick()
		{
			this.multiBlocks.forEach((MultiBlock::tick));
		}
	}
}
