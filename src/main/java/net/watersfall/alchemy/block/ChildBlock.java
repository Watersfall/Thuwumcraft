package net.watersfall.alchemy.block;

import net.minecraft.block.*;
import net.minecraft.util.shape.VoxelShape;
import net.watersfall.alchemy.blockentity.ChildBlockEntity;
import net.watersfall.alchemy.multiblock.component.ItemComponent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class ChildBlock extends Block implements BlockEntityProvider, InventoryProvider
{
	public ChildBlock(Settings settings)
	{
		super(settings);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView world)
	{
		return new ChildBlockEntity();
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		BlockEntity entityTest = world.getBlockEntity(pos);
		if(entityTest instanceof ChildBlockEntity)
		{
			ChildBlockEntity entity = (ChildBlockEntity)entityTest;
			if(entity.getComponent() != null)
			{
				if(!world.isClient)
				{
					entity.getComponent().getMultiBlock().onUse(world, pos, player);
				}
			}
		}
		return ActionResult.success(world.isClient);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player)
	{
		BlockEntity entityTest = world.getBlockEntity(pos);
		if(entityTest instanceof ChildBlockEntity)
		{
			ChildBlockEntity entity = (ChildBlockEntity)entityTest;
			if(entity.getComponent() != null)
			{
				if(!world.isClient())
				{
					entity.getComponent().onBreak();
				}
			}
		}
	}

	@Override
	public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos)
	{
		BlockEntity entityTest = world.getBlockEntity(pos);
		if(entityTest instanceof ChildBlockEntity)
		{
			ChildBlockEntity entity = (ChildBlockEntity)entityTest;
			if(entity.getComponent() instanceof ItemComponent)
			{
				ItemComponent component = (ItemComponent)entity.getComponent();
				return component.getInventory();
			}
		}
		return null;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		BlockEntity entityTest = world.getBlockEntity(pos);
		if(entityTest instanceof ChildBlockEntity)
		{
			ChildBlockEntity entity = (ChildBlockEntity)entityTest;
			if(entity.getComponent() != null)
			{
				return entity.getComponent().getOutline();
			}
		}
		return super.getOutlineShape(state, world, pos, context);
	}
}
