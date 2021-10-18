package net.watersfall.thuwumcraft.entity.golem.goal;

import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.watersfall.thuwumcraft.entity.golem.GolemEntity;

import java.util.EnumSet;

public class InsertIntoInventoryGoal extends GolemGoal
{
	private BlockPos pos;

	public InsertIntoInventoryGoal(GolemEntity golem)
	{
		super(golem);
		this.setControls(EnumSet.of(Control.MOVE));
		pos = golem == null ? BlockPos.ORIGIN : golem.getHome();
	}

	@Override
	public GolemGoal create(GolemEntity entity)
	{
		return new InsertIntoInventoryGoal(entity);
	}

	@Override
	public boolean canStart()
	{
		return !golem.getMainHandStack().isEmpty();
	}

	@Override
	public void start()
	{
		if(!golem.getMainHandStack().isEmpty())
		{
			golem.getNavigation().startMovingTo(pos.getX(), pos.getY(), pos.getZ(), 1);
		}
	}

	private int testInventory(BlockPos pos, ItemStack hand)
	{
		BlockEntity test = golem.world.getBlockEntity(pos);
		BlockState state = golem.world.getBlockState(pos);
		if(test instanceof SidedInventory inventory)
		{
			for(int i : inventory.getAvailableSlots(golem.getSide()))
			{
				ItemStack stack = inventory.getStack(i);
				if(inventory.canInsert(i, hand, golem.getSide()) && (stack.isEmpty() || ItemStack.canCombine(hand, stack)))
				{
					return i;
				}
			}
		}
		else if(test instanceof Inventory inventory)
		{
			for(int i = 0; i < inventory.size(); i++)
			{
				ItemStack stack = inventory.getStack(i);
				if(stack.isEmpty() || ItemStack.canCombine(hand, stack))
				{
					return i;
				}
			}
		}
		else if(state.getBlock() instanceof InventoryProvider inventoryProvider)
		{
			SidedInventory inventory = inventoryProvider.getInventory(state, golem.world, pos);
			{
				for(int i : inventory.getAvailableSlots(golem.getSide()))
				{
					ItemStack stack = inventory.getStack(i);
					if(inventory.canInsert(i, hand, golem.getSide()) && (stack.isEmpty() || ItemStack.canCombine(hand, stack)))
					{
						return i;
					}
				}
			}
		}
		return -1;
	}

	private void insertIntoInventory(int index, ItemStack hand)
	{
		BlockEntity test = golem.world.getBlockEntity(pos);
		BlockState state = golem.world.getBlockState(pos);
		if(test instanceof SidedInventory inventory)
		{
			insertStack(inventory, index, hand);
		}
		else if(test instanceof Inventory inventory)
		{
			insertStack(inventory, index, hand);
		}
		else if(state.getBlock() instanceof InventoryProvider inventoryProvider)
		{
			insertStack(inventoryProvider.getInventory(state, golem.world, pos), index, hand);
		}
	}

	private void insertStack(Inventory inventory, int index, ItemStack stack)
	{
		ItemStack inventoryStack = inventory.getStack(index);
		if(inventoryStack.isEmpty())
		{
			ItemStack insertStack = stack.copy();
			insertStack.setCount(Math.min(insertStack.getCount(), inventory.getMaxCountPerStack()));
			inventory.setStack(index, insertStack);
			stack.decrement(insertStack.getCount());
			golem.setStackInHand(Hand.MAIN_HAND, stack);
		}
		else
		{
			int original = stack.getCount();
			int count = Math.min(inventory.getMaxCountPerStack(), stack.getCount() + inventoryStack.getCount());
			inventoryStack.setCount(count);
			stack.decrement(count - original);
			golem.setStackInHand(Hand.MAIN_HAND, stack);
			inventory.setStack(index, inventoryStack);
		}
		inventory.markDirty();
	}

	@Override
	public void tick()
	{
		super.tick();
		start();
		if(golem.world.getTime() % 5 == 0)
		{
			if(golem.getPos().distanceTo(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)) <= 1.5)
			{
				ItemStack stack = golem.getMainHandStack();
				int index = testInventory(pos, stack);
				if(index >= 0)
				{
					insertIntoInventory(index, stack);
				}
			}
		}
	}
}
