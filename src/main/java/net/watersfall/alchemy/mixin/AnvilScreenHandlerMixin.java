package net.watersfall.alchemy.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin
{
	@Inject(method = "onTakeOutput", at = @At("RETURN"))
	public void modifyOnTakeout(PlayerEntity player, ItemStack stack, CallbackInfo info)
	{
		stack.onCraft(player.getEntityWorld(), player, stack.getCount());
	}
}
