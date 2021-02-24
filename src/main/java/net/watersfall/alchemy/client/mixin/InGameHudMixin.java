package net.watersfall.alchemy.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.watersfall.alchemy.abilities.entity.RunedShieldAbilityEntity;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.common.RunedShieldAbility;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper
{
	@Shadow @Final private MinecraftClient client;

	@Shadow private int scaledWidth;

	@Shadow private int scaledHeight;

	@Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V"))
	public void renderRunedShield(MatrixStack matrices, CallbackInfo info)
	{
		if(this.client.player != null)
		{
			AbilityProvider<Entity> provider = (AbilityProvider<Entity>)this.client.player;
			Optional<Ability<Entity>> optional = provider.getAbility(RunedShieldAbilityEntity.ID);
			int x = this.scaledWidth / 2 - 91;
			int y = this.scaledHeight / 2 + 91;
			if(optional.isPresent())
			{
				RunedShieldAbility<Entity> runedShieldAbilityEntity = ((RunedShieldAbility<Entity>) optional.get());
				if(runedShieldAbilityEntity.getShieldAmount() > 0)
				{
					int hearts = runedShieldAbilityEntity.getShieldAmount() * 20 / runedShieldAbilityEntity.getMaxAmount();
					for(; hearts > 0; hearts -= 2)
					{
						this.drawTexture(matrices, x - hearts * 8 - 9, this.scaledHeight - 49, 16, 18, 9, 9);
					}
				}
			}
		}
	}
}
