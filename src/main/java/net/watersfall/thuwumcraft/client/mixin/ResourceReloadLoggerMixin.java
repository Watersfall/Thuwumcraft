package net.watersfall.thuwumcraft.client.mixin;

import net.minecraft.client.resource.ResourceReloadLogger;
import net.watersfall.thuwumcraft.client.hooks.ClientHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourceReloadLogger.class)
public class ResourceReloadLoggerMixin
{
	@Inject(method = "finish", at = @At("TAIL"))
	private void thuwumcraft$finishResourceReload(CallbackInfo info)
	{
		ClientHooks.onResourceReload();
	}
}
