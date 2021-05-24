package net.watersfall.thuwumcraft.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.block.AlchemyBlocks;
import net.watersfall.thuwumcraft.util.BlockUtils;

import java.util.ArrayList;
import java.util.List;

public class PortableHoleWand extends Item
{
	public PortableHoleWand(Settings settings)
	{
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		PlayerEntity player = context.getPlayer();
		Hand hand = context.getHand();
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		Direction direction = context.getSide().getOpposite();
		if(world.isClient)
		{
			return ActionResult.SUCCESS;
		}
		List<BlockPos> list = new ArrayList<>();
		BlockPos.Mutable mutable = pos.mutableCopy();
		if(direction.getAxis() != Direction.Axis.Y)
		{
			Direction otherDirection = Direction.UP;
			Vec2f hitCords = BlockUtils.getCoordinatedFromVec3f(context.getHitPos(), pos, direction.getOpposite());
			if(hitCords.y > 0.5)
			{
				otherDirection = Direction.DOWN;
			}
			BlockPos.Mutable mutable2 = pos.mutableCopy().move(otherDirection);
			for(int i = 0; i <= 32; i++)
			{
				BlockState state = world.getBlockState(mutable);
				BlockState state2 = world.getBlockState(mutable2);
				if(state.isAir() && state2.isAir())
				{
					break;
				}
				else if(i == 32)
				{
					player.sendMessage(new LiteralText("404 Air Not Found"), true);
					return ActionResult.FAIL;
				}
				else
				{
					list.add(mutable.toImmutable());
					list.add(mutable2.toImmutable());
					mutable.set(mutable.getX() + direction.getOffsetX(), mutable.getY() + direction.getOffsetY(), mutable.getZ() + direction.getOffsetZ());
					mutable2.set(mutable2.getX() + direction.getOffsetX(), mutable2.getY() + direction.getOffsetY(), mutable2.getZ() + direction.getOffsetZ());
				}
			}
		}
		else
		{
			for(int i = 0; i <= 32; i++)
			{
				BlockState state = world.getBlockState(mutable);
				if(state.isAir())
				{
					break;
				}
				else if(i == 32)
				{
					player.sendMessage(new LiteralText("404 Air Not Found"), true);
					return ActionResult.FAIL;
				}
				else
				{
					list.add(mutable.toImmutable());
					mutable.set(mutable.getX() + direction.getOffsetX(), mutable.getY() + direction.getOffsetY(), mutable.getZ() + direction.getOffsetZ());
				}
			}
		}
		for(int i = 0; i < list.size(); i++)
		{
			BlockState oldState = world.getBlockState(list.get(i));
			BlockEntity oldEntity = world.getBlockEntity(list.get(i));
			world.removeBlockEntity(list.get(i));
			world.setBlockState(list.get(i), AlchemyBlocks.PORTABLE_HOLE_BLOCK.getDefaultState(), 1 << 3 | 1 << 1);
			world.removeBlockEntity(list.get(i));
			world.addBlockEntity(AlchemyBlocks.PORTABLE_HOLE_BLOCK.createBlockEntity(list.get(i), AlchemyBlocks.PORTABLE_HOLE_BLOCK.getDefaultState(), oldState, oldEntity));
		}
		return ActionResult.CONSUME;
	}
}
