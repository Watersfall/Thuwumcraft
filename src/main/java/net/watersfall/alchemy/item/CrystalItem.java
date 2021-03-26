package net.watersfall.alchemy.item;

import com.google.common.collect.Lists;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.AspectStack;
import net.watersfall.alchemy.client.item.AspectTooltipData;

import java.util.Optional;

public class CrystalItem extends Item
{
	private final Aspect aspect;

	public CrystalItem(Settings settings, Aspect aspect)
	{
		super(settings);
		this.aspect = aspect;
	}

	@Override
	public Optional<TooltipData> getTooltipData(ItemStack stack)
	{
		return Optional.of(new AspectTooltipData(Lists.newArrayList(new AspectStack(this.aspect, 1))));
	}

	@Override
	public String getTranslationKey()
	{
		return "item.waters_alchemy_mod.crystal";
	}

	public Aspect getAspect()
	{
		return this.aspect;
	}
}
