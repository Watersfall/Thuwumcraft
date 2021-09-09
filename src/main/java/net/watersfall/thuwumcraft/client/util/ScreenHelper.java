package net.watersfall.thuwumcraft.client.util;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.watersfall.thuwumcraft.api.client.item.MultiTooltipComponent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class ScreenHelper
{
	private static Method method_32635;

	static
	{
		try
		{
			method_32635 = Screen.class.getDeclaredMethod("method_32635", List.class, TooltipData.class);
			method_32635.setAccessible(true);
		}
		catch(NoSuchMethodException e)
		{
			e.printStackTrace();
		}
	}

	private static void invokeMethod32635(Screen screen, List<TooltipComponent> list, TooltipData data)
	{
		try
		{
			method_32635.invoke(screen, list, data);
		}
		catch(IllegalAccessException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}

	public static void renderTooltip(Screen screen, MatrixStack matrices, List<Text> lines, Optional<TooltipData> data, ItemStack stack, int x, int y)
	{
		List<TooltipComponent> list = lines.stream().map(Text::asOrderedText).map(TooltipComponent::of).collect(Collectors.toList());
		data.ifPresent((data1) -> {
			invokeMethod32635(screen, list, data1);
		});
		if(MultiTooltipComponent.REGISTRY.get(stack.getItem()) != null)
		{
			((MultiTooltipComponent)stack.getItem()).getTooltipComponents(stack).ifPresent(list::addAll);
		}
		screen.renderTooltipFromComponents(matrices, list, x, y);
	}
}
