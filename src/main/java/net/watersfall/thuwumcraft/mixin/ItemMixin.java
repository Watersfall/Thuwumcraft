package net.watersfall.thuwumcraft.mixin;

import net.minecraft.item.Item;
import net.watersfall.wet.api.item.BeforeActions;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin implements BeforeActions
{ }
