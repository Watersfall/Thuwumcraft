package net.watersfall.alchemy.client.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.watersfall.alchemy.api.client.item.CustomTooltipDataComponent;
import net.watersfall.alchemy.api.client.item.MultiTooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mixin(Screen.class)
public abstract class ScreenMixin
{
	@Shadow protected abstract void renderTooltipFromComponents(MatrixStack matrices, List<TooltipComponent> components, int x, int y);

	@Shadow public abstract List<Text> getTooltipFromItem(ItemStack stack);

	//Yeah I can't find it either
	@Inject(method = "method_32635", at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void onComponentConstruct(List<TooltipComponent> list, TooltipData data, CallbackInfo info)
	{
		if (data instanceof CustomTooltipDataComponent)
		{
			list.add(((CustomTooltipDataComponent)data).getComponent());
			info.cancel();
		}
	}

	@Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V", at = @At("HEAD"), cancellable = true)
	public void renderTooltipFix(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo info)
	{
		this.renderTooltip(matrices, this.getTooltipFromItem(stack), stack.getTooltipData(), stack, x, y);
		info.cancel();
	}

	public void renderTooltip(MatrixStack matrices, List<Text> lines, Optional<TooltipData> data, ItemStack stack, int x, int y)
	{
		List<TooltipComponent> list = lines.stream().map(Text::asOrderedText).map(TooltipComponent::of).collect(Collectors.toList());
		data.ifPresent((tooltipData) ->
		{
			if (tooltipData instanceof CustomTooltipDataComponent)
			{
				list.add(((CustomTooltipDataComponent)tooltipData).getComponent());
			}
			else
			{
				list.add(1, TooltipComponent.of(tooltipData));
			}
		});
		if(MultiTooltipComponent.REGISTRY.get(stack.getItem()) != null)
		{
			MultiTooltipComponent.REGISTRY.get(stack.getItem()).forEach((function) -> {
				TooltipComponent component = function.apply(stack);
				if(component != null)
				{
					list.add(function.apply(stack));
				}
			});
		}
		this.renderTooltipFromComponents(matrices, list, x, y);
	}
}
