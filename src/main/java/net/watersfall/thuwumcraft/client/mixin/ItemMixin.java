package net.watersfall.thuwumcraft.client.mixin;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.watersfall.thuwumcraft.api.client.item.MultiTooltipComponent;
import net.watersfall.thuwumcraft.client.hooks.ClientHooks;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.Optional;

@Mixin(Item.class)
public class ItemMixin implements MultiTooltipComponent
{
	@Override
	public Optional<List<TooltipComponent>> getTooltipComponents(ItemStack stack)
	{
		return ClientHooks.getTooltipComponents(stack);
	}
}
