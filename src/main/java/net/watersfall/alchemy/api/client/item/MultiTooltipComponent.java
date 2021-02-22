package net.watersfall.alchemy.api.client.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface MultiTooltipComponent
{
	public static final Registry REGISTRY = new Registry();

	Optional<List<TooltipComponent>> getTooltipComponents(ItemStack stack);

	class Registry
	{
		private final HashMap<Item, List<Function<ItemStack, TooltipComponent>>> components;

		private Registry()
		{
			components = new HashMap<>();
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

		public List<Function<ItemStack, TooltipComponent>> get(Item item)
		{
			return components.get(item);
		}
	}
}
