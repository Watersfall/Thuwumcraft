package net.watersfall.thuwumcraft.api.client.item;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface MultiTooltipComponent
{
	public static final Registry REGISTRY = new Registry();

	Optional<List<TooltipComponent>> getTooltipComponents(ItemStack stack);

	class Registry
	{
		private final HashMap<Item, List<Function<ItemStack, TooltipComponent>>> components;
		private final HashMap<Item, List<Function<ItemStack, TooltipComponent>>> reloadRemovedComponents;

		private Registry()
		{
			components = new HashMap<>();
			reloadRemovedComponents = new HashMap<>();
		}

		public void reload()
		{
			reloadRemovedComponents.clear();
		}

		public void register(Item item, Function<ItemStack, TooltipComponent> component)
		{
			if(components.containsKey(item))
			{
				components.get(item).add(component);
			}
			else
			{
				List<Function<ItemStack, TooltipComponent>> list = new ArrayList<>();
				list.add(component);
				components.put(item, list);
			}
		}

		public void registerReloadRemoved(Item item, Function<ItemStack, TooltipComponent> component)
		{
			if(reloadRemovedComponents.containsKey(item))
			{
				reloadRemovedComponents.get(item).add(component);
			}
			else
			{
				List<Function<ItemStack, TooltipComponent>> list = new ArrayList<>();
				list.add(component);
				reloadRemovedComponents.put(item, list);
			}
		}

		public List<Function<ItemStack, TooltipComponent>> get(Item item)
		{
			List<Function<ItemStack, TooltipComponent>> list = new ArrayList<>();
			if(components.containsKey(item))
			{
				list.addAll(components.get(item));
			}
			if(reloadRemovedComponents.containsKey(item))
			{
				list.addAll(reloadRemovedComponents.get(item));
			}
			return list;
		}
	}
}
