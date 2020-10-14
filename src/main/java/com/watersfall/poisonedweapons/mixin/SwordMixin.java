package com.watersfall.poisonedweapons.mixin;

import com.watersfall.poisonedweapons.api.Poisonable;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SwordItem.class)
public abstract class SwordMixin extends ToolItem implements Poisonable
{
	public SwordMixin(ToolMaterial material, Settings settings)
	{
		super(material, settings);
	}
}
