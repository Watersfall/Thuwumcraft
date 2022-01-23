package net.watersfall.thuwumcraft.api.spell.modifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.nbt.NbtCompound;
import net.watersfall.thuwumcraft.client.gui.screen.FocalManipulatorScreen;

import java.util.List;

public abstract class SpellModifier
{
	private String name;

	public SpellModifier(String name)
	{
		this.name = name;
	}

	public SpellModifier(NbtCompound nbt)
	{
		fromNbt(nbt);
	}

	public void fromNbt(NbtCompound nbt)
	{
		this.name = nbt.getString("name");
	}

	public NbtCompound toNbt(NbtCompound nbt)
	{
		nbt.putString("name", this.name);
		return nbt;
	}

	@Environment(EnvType.CLIENT)
	public abstract List<ClickableWidget> getGuiElements(FocalManipulatorScreen screen);

	public String getName()
	{
		return name;
	}
}
