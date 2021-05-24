package net.watersfall.thuwumcraft.api.aspect;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * A sided inventory that also contains AspectStacks.
 * The default implementation is an inventory that has
 * no ItemStacks and only contains AspectStacks
 */
public interface AspectInventory extends SidedInventory
{
	public static void writeNbt(NbtCompound tag, AspectInventory inventory)
	{
		Inventories.writeNbt(tag, inventory.getContents());
		NbtList aspects = new NbtList();
		inventory.getAspects().values().forEach((aspect) -> {
			NbtCompound aspectTag = new NbtCompound();
			aspectTag.putString("aspect", aspect.getAspect().getId().toString());
			aspectTag.putInt("count", aspect.getCount());
			aspects.add(aspectTag);
		});
		tag.put("aspects", aspects);
	}

	public static void readNbt(NbtCompound tag, AspectInventory inventory)
	{
		Inventories.readNbt(tag, inventory.getContents());
		NbtList aspects = tag.getList("aspects", NbtType.COMPOUND);
		aspects.forEach((aspectTag) -> {
			NbtCompound compound = (NbtCompound)aspectTag;
			AspectStack stack = new AspectStack(Aspects.getAspectById(Identifier.tryParse(compound.getString("aspect"))), compound.getInt("count"));
			inventory.addAspect(stack);
		});
	}

	/**
	 * Gets the map of all AspectStacks in this inventory
	 * @return the aspects map
	 */
	HashMap<Aspect, AspectStack> getAspects();

	/**
	 * Gets the currently inputted item into this inventory.
	 * Used for processing the AspectIngredient recipe and the
	 * CrucibleRecipe
	 * @return The currently inputted stack
	 */
	ItemStack getCurrentInput();

	/**
	 * Sets the current input item for recipe processing
	 * @param stack The stack to process
	 */
	void setCurrentInput(ItemStack stack);

	/**
	 * Gets the AspectStack for the specific Aspect
	 * @param aspect the Aspect instance
	 * @return the AspectStack
	 */
	default AspectStack getAspect(Aspect aspect)
	{
		if(getAspects().containsKey(aspect))
		{
			return getAspects().get(aspect);
		}
		return AspectStack.EMPTY;
	}

	/**
	 * Removes the AspectStack of the specific aspect from the inventory
	 * @param aspect The Aspect instance
	 * @return the AspectStack that was removed
	 */
	default AspectStack removeAspect(Aspect aspect)
	{
		return getAspects().remove(aspect);
	}

	/**
	 * Removes a specific amount of an Aspect from the inventory
	 * and returns a new AspectStack with the removed Aspects
	 * @param aspect the Aspect instance to remove
	 * @param amount The amount to remove
	 * @return A new AspectStack with the size of amount
	 */
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

	/**
	 * Adds the AspectStack
	 * @param aspect the AspectStack to add
	 */
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

	/**
	 * Gets the size of the aspects map
	 * @return the size
	 */
	default int aspectSize()
	{
		return getAspects().size();
	}

	default boolean containsAspect(Aspect aspect)
	{
		return getAspects().containsKey(aspect) && !getAspects().get(aspect).isEmpty();
	}

	default void setAspect(Aspect aspect, int amount)
	{
		getAspects().put(aspect, new AspectStack(aspect, amount));
	}

	/**
	 * Gets the total amount of all aspects contained in this inventory
	 * @return the total aspect count
	 */
	default int totalAspectCount()
	{
		int count = 0;
		for(AspectStack stack : getAspects().values())
		{
			count += stack.getCount();
		}
		return count;
	}

	default boolean canInsert(AspectStack stack, Direction direction)
	{
		return false;
	}

	default boolean canExtract(Direction direction)
	{
		return false;
	}

	/**
	 * Saves the entire inventory to a CompoundTag and returns the tag. <br>
	 * This default implementation saves only the aspects and input stack,
	 * and none of the standard inventory items
	 * @param tag The CompoundTag to write the inventory to
	 * @return The CompoundTag written to
	 */
	default NbtCompound toInventoryTag(NbtCompound tag)
	{
		NbtList list = new NbtList();
		this.getAspects().keySet().forEach((key) -> {
			NbtCompound aspect = new NbtCompound();
			aspect.putString("aspect", key.getId().toString());
			aspect.putInt("count", this.getAspect(key).getCount());
			list.add(aspect);
		});
		tag.put("aspects", list);
		if(!getCurrentInput().isEmpty())
		{
			NbtCompound input = new NbtCompound();
			input.putString("item", Registry.ITEM.getId(getCurrentInput().getItem()).toString());
			input.putInt("count", getCurrentInput().getCount());
			tag.put("input", input);
		}
		return tag;
	}

	/**
	 * Reads the entire inventory from the passed in CompoundTag. <br>
	 * This default implementation only reads AspectStacks and the
	 * Input ItemStack
	 * @param tag The CompoundTag to read from
	 */
	default void fromInventoryTag(NbtCompound tag)
	{
		this.getAspects().clear();
		NbtList list = tag.getList("aspects", NbtType.COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			NbtCompound aspect = (NbtCompound) list.get(i);
			AspectStack stack = new AspectStack(Aspects.ASPECTS.get(Identifier.tryParse(aspect.getString("aspect"))), aspect.getInt("count"));
			setAspect(stack.getAspect(), stack.getCount());
		}
		if(tag.contains("input"))
		{
			NbtCompound input = tag.getCompound("input");
			ItemStack stack = new ItemStack(Registry.ITEM.get(Identifier.tryParse(input.getString("item"))), input.getInt("count"));
			this.setCurrentInput(stack);
		}
	}

	default DefaultedList<ItemStack> getContents()
	{
		return DefaultedList.of();
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

	/**
	 * A default implementation of AspectInventory that does nothing special.
	 * Meant for checking against the recipe type
	 */
	public static final class Impl implements AspectInventory
	{
		private ItemStack stack;
		private final HashMap<Aspect, AspectStack> aspects;

		public Impl(ItemStack stack)
		{
			this.stack = stack;
			this.aspects = new HashMap<>();
		}

		@Override
		public HashMap<Aspect, AspectStack> getAspects()
		{
			return aspects;
		}

		@Override
		public ItemStack getCurrentInput()
		{
			return stack;
		}

		@Override
		public void setCurrentInput(ItemStack stack)
		{
			this.stack = stack;
		}

		@Override
		public void markDirty()
		{

		}
	}
}
