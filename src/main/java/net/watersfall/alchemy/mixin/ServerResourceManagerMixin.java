package net.watersfall.alchemy.mixin;

import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.watersfall.alchemy.research.ResearchCategoryLoader;
import net.watersfall.alchemy.research.ResearchLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerResourceManager.class)
public class ServerResourceManagerMixin
{
	@Shadow @Final private ReloadableResourceManager resourceManager;

	@Inject(method = "<init>", at = @At("TAIL"))
	public void registerResearchListener(DynamicRegistryManager registryManager, CommandManager.RegistrationEnvironment registrationEnvironment, int i, CallbackInfo ci)
	{
		this.resourceManager.registerListener(new ResearchCategoryLoader());
		this.resourceManager.registerListener(new ResearchLoader());
	}
}
