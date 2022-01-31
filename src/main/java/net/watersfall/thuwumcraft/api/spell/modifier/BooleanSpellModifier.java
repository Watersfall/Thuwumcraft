package net.watersfall.thuwumcraft.api.spell.modifier;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.watersfall.thuwumcraft.client.gui.button.SpellToggleButton;
import net.watersfall.thuwumcraft.client.gui.screen.FocalManipulatorScreen;

import java.util.List;

public class BooleanSpellModifier extends SpellModifier
{
	private boolean value;

	public BooleanSpellModifier(NbtCompound nbt)
	{
		super(nbt);
	}

	public BooleanSpellModifier(String name, boolean value)
	{
		super(name);
		this.value = value;
	}

	@Override
	public void fromNbt(NbtCompound nbt)
	{
		super.fromNbt(nbt);
		this.value = nbt.getBoolean("value");
	}

	@Override
	public NbtCompound toNbt(NbtCompound nbt)
	{
		nbt.putBoolean("value", value);
		return super.toNbt(nbt);
	}

	public boolean getValue()
	{
		return value;
	}

	public void setValue(boolean value)
	{
		this.value = value;
	}

	@Override
	public List<ClickableWidget> getGuiElements(FocalManipulatorScreen screen)
	{
		SpellToggleButton checkbox = new SpellToggleButton(screen.getX() + screen.getWidth() / 2 - 10, screen.getY() + 24, 20, 20, new LiteralText(""), value, false, this);
		return List.of(checkbox);
	}
}
