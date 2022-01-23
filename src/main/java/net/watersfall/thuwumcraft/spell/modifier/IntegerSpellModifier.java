package net.watersfall.thuwumcraft.spell.modifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.watersfall.thuwumcraft.client.gui.FocalManipulatorScreen;
import net.watersfall.thuwumcraft.client.gui.button.NumberButton;

import java.util.List;

public class IntegerSpellModifier extends SpellModifier implements ValueGetter<Integer>
{
	private int min;
	private int max;
	private int value;

	public IntegerSpellModifier(String name, int min, int max, int value)
	{
		super(name);
		this.min = min;
		this.max = max;
		this.value = value;
	}

	public IntegerSpellModifier(NbtCompound nbt)
	{
		super(nbt);
	}

	@Override
	public void fromNbt(NbtCompound nbt)
	{
		super.fromNbt(nbt);
		this.min = nbt.getInt("min");
		this.max = nbt.getInt("max");
		this.value = nbt.getInt("value");
	}

	@Override
	public NbtCompound toNbt(NbtCompound nbt)
	{
		super.toNbt(nbt);
		nbt.putInt("min", min);
		nbt.putInt("max", max);
		nbt.putInt("value", value);
		return nbt;
	}

	public int getMin()
	{
		return min;
	}

	public IntegerSpellModifier setMin(int min)
	{
		this.min = min;
		return this;
	}

	public int getMax()
	{
		return max;
	}

	public IntegerSpellModifier setMax(int max)
	{
		this.max = max;
		return this;
	}

	public Integer getValue()
	{
		return value;
	}

	public IntegerSpellModifier setValue(int value)
	{
		this.value = value;
		return this;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public List<ClickableWidget> getGuiElements(FocalManipulatorScreen screen)
	{
		int x = screen.getX();
		int y = screen.getY();
		int width = screen.getWidth();
		int centerX = x + (width / 2);
		NumberButton numberButton = new NumberButton(centerX, y + 24, 24, 24, this::getValue);
		ButtonWidget next = new ButtonWidget(centerX + 24, y + 24, 20, 20, new LiteralText(">"), button -> {
			this.setValue(this.getValue() + 1);
			if(value >= this.getMax())
			{
				this.setValue(this.getMax());
			}
		});
		ButtonWidget previous = new ButtonWidget(centerX - 24, y + 24, 20, 20, new LiteralText("<"), button -> {
			this.setValue(this.getValue() - 1);
			if(value <= this.getMin())
			{
				this.setValue(this.getMin());
			}
		});
		return List.of(numberButton, next, previous);
	}
}
