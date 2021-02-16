package net.watersfall.alchemy.api.aspect;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public interface AspectInventory extends SidedInventory
{
	HashMap<Aspect, AspectStack> getAspects();

	ItemStack getCurrentInput();

	void setCurrentInput(ItemStack stack);

	default AspectStack getAspect(Aspect aspect)
	{
		return getAspects().get(aspect);
	}

	default AspectStack removeAspect(Aspect aspect)
	{
		return getAspects().remove(aspect);
	}

	default AspectStack removeAspect(Aspect aspect, int amount)
	{
		AspectStack stack = getAspects().get(aspect);
		if(amount >= stack.getCount())
		{
			return getAspects().remove(aspect);
		}
		else
		{
			stack.decrement(amount);
			return new AspectStack(aspect, amount);
		}
	}

	default void addAspect(AspectStack aspect)
	{
		if(getAspects().containsKey(aspect.getAspect()))
		{
			AspectStack stack = getAspects().get(aspect.getAspect());
			stack.increment(aspect.getCount());
		}
		else
		{
			getAspects().put(aspect.getAspect(), aspect);
		}
	}

	default int aspectSize()
	{
		return getAspects().size();
	}

	default int aspectCount(Aspect aspect)
	{
		int count = 0;
		for(int i = 0; i < aspectSize(); i++)
		{
			if(getAspect(aspect).getAspect() == aspect)
			{
				count += getAspect(aspect).getCount();
			}
		}
		return count;
	}

	default boolean containsAspect(Aspect aspect)
	{
		for(int i = 0; i < aspectSize(); i++)
		{
			if(getAspect(aspect).getAspect() == aspect)
			{
				return true;
			}
		}
		return false;
	}

	void setAspect(Aspect aspect, int amount);

	default CompoundTag toInventoryTag(CompoundTag tag)
	{
		ListTag list = new ListTag();
		this.getAspects().keySet().forEach((key) -> {
			CompoundTag aspect = new CompoundTag();
			aspect.putString("aspect", key.getName());
			aspect.putInt("count", this.getAspect(key).getCount());
			list.add(aspect);
		});
		tag.put("aspects", list);
		if(!getCurrentInput().isEmpty())
		{
			CompoundTag input = new CompoundTag();
			input.putString("item", Registry.ITEM.getId(getCurrentInput().getItem()).toString());
			input.putInt("count", getCurrentInput().getCount());
			tag.put("input", input);
		}
		return tag;
	}

	default void fromInventoryTag(CompoundTag tag)
	{
		this.getAspects().clear();
		ListTag list = tag.getList("aspects", NbtType.COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			CompoundTag aspect = (CompoundTag) list.get(i);
			AspectStack stack = new AspectStack(Aspect.ASPECTS.get(Identifier.tryParse(aspect.getString("aspect"))), aspect.getInt("count"));
			setAspect(stack.getAspect(), stack.getCount());
		}
		if(tag.contains("input"))
		{
			CompoundTag input = tag.getCompound("input");
			ItemStack stack = new ItemStack(Registry.ITEM.get(Identifier.tryParse(input.getString("item"))), input.getInt("count"));
			this.setCurrentInput(stack);
		}
	}

	@Override
	default int[] getAvailableSlots(Direction side)
	{
		return new int[]{};
	}

	@Override
	default boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir)
	{
		return false;
	}

	@Override
	default boolean canExtract(int slot, ItemStack stack, Direction dir)
	{
		return false;
	}

	@Override
	default int size()
	{
		return 0;
	}

	@Override
	default boolean isEmpty()
	{
		return true;
	}

	@Override
	default ItemStack getStack(int slot)
	{
		return ItemStack.EMPTY;
	}

	@Override
	default ItemStack removeStack(int slot, int amount)
	{
		return ItemStack.EMPTY;
	}

	@Override
	default ItemStack removeStack(int slot)
	{
		return ItemStack.EMPTY;
	}

	@Override
	default void setStack(int slot, ItemStack stack)
	{

	}

	@Override
	default boolean canPlayerUse(PlayerEntity player)
	{
		return true;
	}

	@Override
	default void clear()
	{
		getAspects().clear();
	}
}
