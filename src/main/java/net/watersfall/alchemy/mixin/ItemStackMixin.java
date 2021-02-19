package net.watersfall.alchemy.mixin;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.watersfall.alchemy.item.AlchemyItems;
import net.watersfall.alchemy.item.GlassPhialItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin
{
	@Shadow public abstract CompoundTag getOrCreateTag();

	@Inject(method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V", at = @At("TAIL"))
	public void addData(ItemConvertible item, int count, CallbackInfo info)
	{
		if(item != null && item.asItem() instanceof GlassPhialItem)
		{
			if(item.asItem() != AlchemyItems.EMPTY_PHIAL_ITEM)
			{
				this.getOrCreateTag().putInt("waters_aspect_count", 64);
			}
		}
	}
}
