package net.watersfall.thuwumcraft.client.gui.button;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.watersfall.thuwumcraft.spell.modifier.ValueGetter;

public class NumberButton extends PressableWidget
{
	private final ValueGetter<Number> value;

	public NumberButton(int x, int y, int width, int height, ValueGetter<Number> valueGetter)
	{
		super(x, y, width, height, new LiteralText(valueGetter.getValue() + ""));
		this.value = valueGetter;
	}

	@Override
	public void onPress()
	{

	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		ClickableWidget.drawCenteredTextWithShadow(matrices, MinecraftClient.getInstance().textRenderer, new LiteralText(value.getValue() + "").asOrderedText(), this.x + this.width / 2, this.y + (this.height - 8) / 2, 0xFFFFFF);
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder)
	{
		builder.put(NarrationPart.TITLE, value.getValue() + "");
	}

	public Number getValue()
	{
		return value.getValue();
	}
}
