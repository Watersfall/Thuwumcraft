package net.watersfall.alchemy.client.mixin;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.watersfall.alchemy.api.client.item.MultiTooltipComponent;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mixin(Item.class)
public class ItemMixin implements MultiTooltipComponent
{
	@Override
	public Optional<List<TooltipComponent>> getTooltipComponents(ItemStack stack)
	{
		if(MultiTooltipComponent.REGISTRY.get(stack.getItem()) != null)
		{
			List<Function<ItemStack, TooltipComponent>> list = MultiTooltipComponent.REGISTRY.get(stack.getItem());
			List<TooltipComponent> list2 = new ArrayList<>();
			for(int i = 0; i < list.size(); i++)
			{
				if(list.get(i) != null)
				{
					TooltipComponent component = list.get(i).apply(stack);
					if(component != null)
					{
						list2.add(list.get(i).apply(stack));
					}
				}
			}
			return Optional.of(list2);
		}
		return Optional.empty();
	}
}
