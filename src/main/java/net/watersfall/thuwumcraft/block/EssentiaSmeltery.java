package net.watersfall.thuwumcraft.block;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.block.entity.ThuwumcraftBlockEntities;
import net.watersfall.thuwumcraft.block.entity.EssentiaSmelteryEntity;
import org.jetbrains.annotations.Nullable;

public class EssentiaSmeltery extends AbstractFurnaceBlock implements BlockEntityProvider
{
	public EssentiaSmeltery(Settings settings)
	{
		super(settings);
	}

	@Override
	protected void openScreen(World world, BlockPos pos, PlayerEntity player)
	{
		BlockEntity entity = world.getBlockEntity(pos);
		if(entity instanceof NamedScreenHandlerFactory)
		{
			player.openHandledScreen((NamedScreenHandlerFactory)entity);
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new EssentiaSmelteryEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type)
	{
		return world.isClient ? null : checkType(type, ThuwumcraftBlockEntities.ESSENTIA_SMELTERY_ENTITY, EssentiaSmelteryEntity::tick);
	}
}
