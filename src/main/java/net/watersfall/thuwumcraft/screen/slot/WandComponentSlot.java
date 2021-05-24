package net.watersfall.thuwumcraft.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.api.abilities.item.WandAbility;
import net.watersfall.thuwumcraft.api.abilities.item.WandFocusAbility;
import net.watersfall.thuwumcraft.item.wand.WandCapItem;
import net.watersfall.thuwumcraft.item.wand.WandCapMaterial;
import net.watersfall.thuwumcraft.item.wand.WandCoreItem;
import net.watersfall.thuwumcraft.item.wand.WandCoreMaterial;

public class WandComponentSlot extends Slot
{
	private final WandSlot wandSlot;
	private final Class<?> componentType;

	public WandComponentSlot(Inventory inventory, int index, int x, int y, WandSlot wandSlot, Class<?> componentType)
	{
		super(inventory, index, x, y);
		this.wandSlot = wandSlot;
		this.componentType = componentType;
	}

	@Override
	public boolean canInsert(ItemStack stack)
	{
		return !wandSlot.getStack().isEmpty() && stack.getItem().getClass().isAssignableFrom(componentType);
	}

	@Override
	public void onStackChanged(ItemStack originalItem, ItemStack newItem)
	{
		if(newItem.isEmpty())
		{
			AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(wandSlot.getStack());
			provider.getAbility(WandAbility.ID, WandAbility.class).ifPresent(wand -> {
				if(this.getIndex() == 1)
				{
					wand.setWandCore(null);
				}
				else if(this.getIndex() == 2)
				{
					wand.setWandCap(null);
				}
				else if(this.getIndex() == 3)
				{
					wand.setSpell(null);
				}
			});
		}
		else
		{
			AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(wandSlot.getStack());
			provider.getAbility(WandAbility.ID, WandAbility.class).ifPresent(wand -> {
				if(this.getIndex() == 1)
				{
					WandCoreMaterial material = ((WandCoreItem)newItem.getItem()).getMaterial();
					wand.setWandCore(material);
				}
				else if(this.getIndex() == 2)
				{
					WandCapMaterial material = ((WandCapItem)newItem.getItem()).getMaterial();
					wand.setWandCap(material);
				}
				else if(this.getIndex() == 3)
				{
					AbilityProvider<ItemStack> focusProvider = AbilityProvider.getProvider(this.getStack());
					focusProvider.getAbility(WandFocusAbility.ID, WandFocusAbility.class).ifPresent(focus -> {
						wand.setSpell(focus.getSpell());
					});
				}
			});
		}
	}

	@Override
	public void setStack(ItemStack stack)
	{
		ItemStack original = this.getStack().copy();
		super.setStack(stack);
		onStackChanged(original, stack);
	}

	public void setStackNoUpdate(ItemStack stack)
	{
		super.setStack(stack);
	}
}
