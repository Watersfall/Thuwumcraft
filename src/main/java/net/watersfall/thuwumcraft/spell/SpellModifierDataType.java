package net.watersfall.thuwumcraft.spell;

import net.minecraft.nbt.NbtCompound;

public class SpellModifierDataType<T extends SpellModifierData>
{
	private final SpellFactory<T> factory;

	public SpellModifierDataType(SpellFactory<T> factory)
	{
		this.factory = factory;
	}

	public T create(NbtCompound nbt)
	{
		return factory.create(this, nbt);
	}

	public interface SpellFactory<T extends SpellModifierData>
	{
		T create(SpellModifierDataType<T> type, NbtCompound nbt);
	}
}
