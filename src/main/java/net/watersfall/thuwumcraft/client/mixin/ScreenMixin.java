package net.watersfall.thuwumcraft.client.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.watersfall.thuwumcraft.client.hooks.ClientHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(Screen.class)
public abstract class ScreenMixin
{
	@Shadow public abstract List<Text> getTooltipFromItem(ItemStack stack);


	@SuppressWarnings("UnresolvedMixinReference") //lambda method on line 180
	@Inject(method = "method_32635", at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void thuwumcraft$onComponentConstruct(List<TooltipComponent> list, TooltipData data, CallbackInfo info)
	{
		ClientHooks.screenCreateItemTooltipData(data, list, info);
	}

	@Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At("HEAD"), cancellable = true)
	public void thuwumcraft$renderTooltipFix(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo info)
	{
		ClientHooks.screenRenderTooltip(stack, (Screen)(Object)this, this.getTooltipFromItem(stack), matrices, x, y,info);
	}
}
