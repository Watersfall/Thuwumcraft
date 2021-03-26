package net.watersfall.alchemy.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.World;
import net.watersfall.alchemy.block.entity.AlchemyBlockEntities;
import net.watersfall.alchemy.block.entity.CustomSpawnerEntity;
import org.jetbrains.annotations.Nullable;

public class CustomSpawnerBlock extends SpawnerBlock
{
	public CustomSpawnerBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack)
	{
		super.onPlaced(world, pos, state, entity, stack);
		if(stack.getTag() != null)
		{
			CustomSpawnerEntity spawner = (CustomSpawnerEntity)world.getBlockEntity(pos);
			if(spawner != null)
			{
				spawner.getLogic().readNbt(world, pos, stack.getTag());
			}
		}
	}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack)
	{
		if(blockEntity instanceof CustomSpawnerEntity)
		{
			CustomSpawnerEntity entity = (CustomSpawnerEntity)blockEntity;
			entity.getLogic().writeNbt(world, pos, stack.getOrCreateTag());
		}
		dropStacks(state, world, pos, blockEntity, player, stack);
	}

	@Override
	public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack)
	{

	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new CustomSpawnerEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type)
	{
		return world.isClient ? checkType(type, AlchemyBlockEntities.CUSTOM_SPAWNER_ENTITY, CustomSpawnerEntity::clientTick) : checkType(type, AlchemyBlockEntities.CUSTOM_SPAWNER_ENTITY, CustomSpawnerEntity::serverTick);
	}
}
