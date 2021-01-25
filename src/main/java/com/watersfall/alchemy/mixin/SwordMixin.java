package com.watersfall.alchemy.mixin;

import com.watersfall.alchemy.api.Poisonable;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SwordItem.class)
public abstract class SwordMixin extends ToolItem implements Poisonable
{
	public SwordMixin(ToolMaterial material, Settings settings)
	{
		super(material, settings);
	}
}
