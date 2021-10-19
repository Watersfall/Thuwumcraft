package net.watersfall.thuwumcraft.entity.golem.goal;

import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.abilities.chunk.GolemMarkersAbility;
import net.watersfall.thuwumcraft.api.golem.GolemMarker;
import net.watersfall.thuwumcraft.entity.golem.GolemEntity;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.util.EnumSet;
import java.util.Optional;

public class ExtractFromInventoryGoal extends GolemGoal
{
	private GolemMarker currentMarker;

	public ExtractFromInventoryGoal(GolemEntity golem)
	{
		super(golem);
		this.setControls(EnumSet.of(Control.MOVE));
	}

	@Override
	public GolemGoal create(GolemEntity entity)
	{
		return new ExtractFromInventoryGoal(entity);
	}

	@Override
	public boolean canStart()
	{
		if(!golem.getMainHandStack().isEmpty())
		{
			return false;
		}
		GolemMarker marker = getFirstMarker();
		if(marker != null)
		{
			return testInventory(marker.pos(), marker.side()) >= 0;
		}
		return false;
	}

	@Override
	public void start()
	{
		super.start();
		currentMarker = null;
		GolemMarker marker = getFirstMarker();
		if(marker != null)
		{
			currentMarker = marker;
			golem.getNavigation().startMovingTo(marker.pos().getX(), marker.pos().getY(), marker.pos().getZ(), 1);
		}
	}

	@Override
	public void tick()
	{
		super.tick();
		start();
		if(currentMarker != null)
		{
			if(golem.getPos().distanceTo(new Vec3d(currentMarker.pos().getX(), currentMarker.pos().getY(), currentMarker.pos().getZ())) < 1.5)
			{
				int index = testInventory(currentMarker.pos(), currentMarker.side());
				if(index >= 0)
				{
					extractFromInventory(index);
				}
			}
		}
	}

	private GolemMarker getFirstMarker()
	{
		World world = golem.world;
		GolemMarker closest = null;
		ChunkPos center = golem.getChunkPos();
		BlockPos golemPos = golem.getBlockPos();
		for(int x = -1; x <= 1; x++)
		{
			for(int z = -1; z <= 1; z++)
			{
				GolemMarkersAbility ability = AbilityProvider.getAbility(world.getChunk(center.x + x, center.z + z), GolemMarkersAbility.ID, GolemMarkersAbility.class).get();
				Optional<GolemMarker> optional = ability.getClosestMarker(golem.getBlockPos(), DyeColor.BLUE);
				if(optional.isPresent())
				{
					GolemMarker check = optional.get();
					if(testInventory(check.pos(), check.side()) >= 0)
					{
						if(closest == null)
						{
							closest = check;
						}
						else if(closest.pos().getSquaredDistance(golemPos) > check.pos().getSquaredDistance(golemPos))
						{
							closest = check;
						}
					}
				}
			}
		}
		return closest;
	}

	private int testInventory(BlockPos pos, Direction side)
	{
		BlockEntity test = golem.world.getBlockEntity(pos);
		BlockState state = golem.world.getBlockState(pos);
		if(test instanceof SidedInventory inventory)
		{
			for(int i : inventory.getAvailableSlots(side))
			{
				ItemStack stack = inventory.getStack(i);
				if(inventory.canExtract(i, stack, side) && !stack.isEmpty())
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
				if(!stack.isEmpty())
				{
					return i;
				}
			}
		}
		else if(state.getBlock() instanceof InventoryProvider inventoryProvider)
		{
			SidedInventory inventory = inventoryProvider.getInventory(state, golem.world, pos);
			{
				for(int i : inventory.getAvailableSlots(side))
				{
					ItemStack stack = inventory.getStack(i);
					if(inventory.canExtract(i, stack, side) && !stack.isEmpty())
					{
						return i;
					}
				}
			}
		}
		return -1;
	}

	private void extractFromInventory(int index)
	{
		BlockEntity test = golem.world.getBlockEntity(currentMarker.pos());
		BlockState state = golem.world.getBlockState(currentMarker.pos());
		if(test instanceof SidedInventory inventory)
		{
			extractStack(inventory, index);
		}
		else if(test instanceof Inventory inventory)
		{
			extractStack(inventory, index);
		}
		else if(state.getBlock() instanceof InventoryProvider inventoryProvider)
		{
			extractStack(inventoryProvider.getInventory(state, golem.world, currentMarker.pos()), index);
		}
	}

	private void extractStack(Inventory inventory, int index)
	{
		ItemStack stack = inventory.getStack(index).copy();
		ItemStack newStack = stack.copy();
		stack.setCount(Math.min(16, stack.getCount()));
		newStack.decrement(stack.getCount());
		inventory.setStack(index, newStack);
		golem.setStackInHand(Hand.MAIN_HAND, stack);
	}
}
