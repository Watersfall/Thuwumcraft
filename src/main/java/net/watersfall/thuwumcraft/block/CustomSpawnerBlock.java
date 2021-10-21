package net.watersfall.thuwumcraft.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.block.entity.CustomSpawnerEntity;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;
import net.watersfall.thuwumcraft.world.CustomMobSpawnerLogic;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
		if(stack.getNbt() != null)
		{
			CustomSpawnerEntity spawner = (CustomSpawnerEntity)world.getBlockEntity(pos);
			if(spawner != null)
			{
				spawner.getLogic().readNbt(world, pos, stack.getNbt());
			}
		}
	}

	@Override
	public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack)
	{

	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext context)
	{
		CustomMobSpawnerLogic.toTooltip(stack, world, tooltip, context);
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
		return world.isClient ? checkType(type, ThuwumcraftBlockEntities.CUSTOM_SPAWNER, CustomSpawnerEntity::clientTick) : checkType(type, ThuwumcraftBlockEntities.CUSTOM_SPAWNER, CustomSpawnerEntity::serverTick);
	}
}
