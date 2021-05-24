package net.watersfall.thuwumcraft.block.entity;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.watersfall.thuwumcraft.inventory.BrewingCauldronInventory;
import net.watersfall.thuwumcraft.util.StatusEffectHelper;

import java.util.Set;

//TODO this
public class PotionSprayerEntity extends BlockEntity implements BrewingCauldronInventory
{
	private final DefaultedList<ItemStack> contents;
	private final DefaultedList<ItemStack> input = DefaultedList.ofSize(1, ItemStack.EMPTY);
	private final SimpleInventory potion = new SimpleInventory(1);

	public PotionSprayerEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.POTION_SPRAYER, pos, state);
		contents = DefaultedList.ofSize(3, ItemStack.EMPTY);
	}

	public ItemStack getPotionItem()
	{
		if(potion.getStack(0).isEmpty())
		{
			Set<StatusEffectInstance> effects = StatusEffectHelper.getEffects(this, world.getRecipeManager(), world);
			if(effects == StatusEffectHelper.INVALID_RECIPE)
			{
				return ItemStack.EMPTY;
			}
			ItemStack stack = Items.SPLASH_POTION.getDefaultStack();
			PotionUtil.setCustomPotionEffects(stack, effects);
			for(int i = 0; i < contents.size(); i++)
			{
				if(!contents.get(i).isEmpty())
				{
					contents.get(i).decrement(1);
				}
			}
			stack.setCount(4);
			potion.setStack(0, stack);
			return stack;
		}
		else
		{
			return potion.getStack(0);
		}
	}

	public Inventory getPotionInventory()
	{
		return potion;
	}

	@Override
	public DefaultedList<ItemStack> getContents()
	{
		return contents;
	}

	@Override
	public DefaultedList<ItemStack> getInput()
	{
		return input;
	}

	@Override
	public void readNbt(NbtCompound tag)
	{
		super.readNbt(tag);
		Inventories.readNbt(tag, contents);
		potion.readNbtList(tag.getList("potion", NbtType.COMPOUND));
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag)
	{
		super.writeNbt(tag);
		Inventories.writeNbt(tag, contents);
		tag.put("potion", potion.toNbtList());
		return tag;
	}
}
