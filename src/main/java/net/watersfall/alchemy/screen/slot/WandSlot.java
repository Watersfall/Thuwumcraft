package net.watersfall.alchemy.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.watersfall.alchemy.abilities.item.WandFocusAbilityImpl;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.item.WandAbility;
import net.watersfall.alchemy.item.AlchemyItems;

import java.util.function.Supplier;

public class WandSlot extends Slot
{
	Supplier<WandComponentSlot> core, cap, focus;

	public WandSlot(Inventory inventory, int index, int x, int y, Supplier<WandComponentSlot> core, Supplier<WandComponentSlot> cap, Supplier<WandComponentSlot> focus)
	{
		super(inventory, index, x, y);
		this.core = core;
		this.cap = cap;
		this.focus = focus;
	}

	@Override
	public boolean canInsert(ItemStack stack)
	{
		return stack.isOf(AlchemyItems.WAND);
	}

	@Override
	public void onStackChanged(ItemStack originalItem, ItemStack newItem)
	{
		if(originalItem.isEmpty() && !newItem.isEmpty())
		{
			AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(newItem);
			provider.getAbility(WandAbility.ID, WandAbility.class).ifPresent(wand -> {
				if(wand.getWandCore() != null)
				{
					core.get().setStack(wand.getWandCore().getItemStack());
				}
				if(wand.getWandCap() != null)
				{
					cap.get().setStack(wand.getWandCap().getItemStack());
				}
				if(wand.getSpell() != null)
				{
					ItemStack stack = AlchemyItems.WAND_FOCUS.getDefaultStack();
					AbilityProvider<ItemStack> focusProvider = AbilityProvider.getProvider(stack);
					focusProvider.addAbility(new WandFocusAbilityImpl(wand.getSpell().spell(), stack));
					focus.get().setStack(stack);
				}
			});
		}
		else
		{
			core.get().setStackNoUpdate(ItemStack.EMPTY);
			cap.get().setStackNoUpdate(ItemStack.EMPTY);
			focus.get().setStackNoUpdate(ItemStack.EMPTY);
		}
	}

	@Override
	public void setStack(ItemStack stack)
	{
		ItemStack original = this.getStack().copy();
		super.setStack(stack);
		this.onStackChanged(original, stack);
	}
}