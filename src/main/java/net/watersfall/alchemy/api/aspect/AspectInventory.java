package net.watersfall.alchemy.api.aspect;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;

public interface AspectInventory extends SidedInventory
{
	HashMap<Aspect, AspectStack> getAspects();

	ItemStack getCurrentInput();

	void setCurrentInput(ItemStack stack);

	AspectStack getAspect(Aspect aspect);

	AspectStack removeAspect(Aspect aspect);

	AspectStack removeAspect(Aspect aspect, int amount);

	void addAspect(AspectStack aspect);

	int aspectSize();

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
		ListTag list = tag.getList("aspects", NbtType.COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			CompoundTag aspect = (CompoundTag) list.get(i);
			AspectStack stack = new AspectStack(Aspect.ASPECTS.get(Identifier.tryParse(aspect.getString("aspect"))), aspect.getInt("count"));
			addAspect(stack);
		}
		if(tag.contains("input"))
		{
			CompoundTag input = tag.getCompound("input");
			ItemStack stack = new ItemStack(Registry.ITEM.get(Identifier.tryParse(input.getString("item"))), input.getInt("count"));
			this.setCurrentInput(stack);
		}
	}
}
