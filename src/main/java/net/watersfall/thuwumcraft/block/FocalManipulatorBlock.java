package net.watersfall.thuwumcraft.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.block.entity.FocalManipulatorBlockEntity;
import org.jetbrains.annotations.Nullable;

public class FocalManipulatorBlock extends BlockWithEntity
{
	public FocalManipulatorBlock(Settings settings)
	{
		super(settings);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new FocalManipulatorBlockEntity(pos, state);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if(!world.isClient)
		{
			player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
		}
		return ActionResult.success(world.isClient);
	}
}
