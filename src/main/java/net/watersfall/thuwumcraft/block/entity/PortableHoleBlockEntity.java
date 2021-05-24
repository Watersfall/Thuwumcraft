package net.watersfall.thuwumcraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PortableHoleBlockEntity extends BlockEntity
{
	private final BlockState original;
	private final BlockEntity originalEntity;
	private int age;

	public PortableHoleBlockEntity(BlockPos pos, BlockState state)
	{
		this(pos, state, Blocks.AIR.getDefaultState(), null);
	}

	public PortableHoleBlockEntity(BlockPos pos, BlockState state, BlockState original, BlockEntity originalEntity)
	{
		super(ThuwumcraftBlockEntities.PORTABLE_HOLE_ENTITY, pos, state);
		this.original = original;
		this.originalEntity = originalEntity;
	}

	public static void tick(World world, BlockPos pos, BlockState state, PortableHoleBlockEntity entity)
	{
		if(entity.age++ > 100)
		{
			world.getServer().execute(() -> {
				world.setBlockState(pos, entity.original);
				entity.original.getBlock().onPlaced(world, pos, entity.original, null, ItemStack.EMPTY);
				if(entity.originalEntity != null)
				{
					entity.originalEntity.cancelRemoval();
					world.addBlockEntity(entity.originalEntity);
				}
			});
		}
	}
}
