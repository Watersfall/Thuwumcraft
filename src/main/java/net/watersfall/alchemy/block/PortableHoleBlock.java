package net.watersfall.alchemy.block;

import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.alchemy.block.entity.AlchemyBlockEntities;
import net.watersfall.alchemy.block.entity.PortableHoleBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PortableHoleBlock extends AirBlock implements BlockEntityProvider
{
	public PortableHoleBlock(Settings settings)
	{
		super(settings);
	}

	public BlockEntity createBlockEntity(BlockPos pos, BlockState state, BlockState original, BlockEntity entity)
	{
		return new PortableHoleBlockEntity(pos, state, original, entity);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random)
	{
		double x = pos.getX() + random.nextDouble();
		double y = pos.getY() + random.nextDouble();
		double z = pos.getZ() + random.nextDouble();
		world.addParticle(ParticleTypes.WHITE_ASH, x, y, z, 0, 0, 0);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new PortableHoleBlockEntity(pos, state);
	}

	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker)
	{
		return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type)
	{
		return world.isClient ? null : checkType(type, AlchemyBlockEntities.PORTABLE_HOLE_ENTITY, PortableHoleBlockEntity::tick);
	}
}
