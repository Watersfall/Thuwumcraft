package net.watersfall.thuwumcraft.api.spell;

import net.minecraft.nbt.NbtCompound;

public class SpellType<T extends Spell<?>>
{
	private final SpellFactory<T> factory;
	private final DefaultSpellFactory<T> defaultFactory;

	public SpellType(SpellFactory<T> factory, DefaultSpellFactory<T> defaultFactory)
	{
		this.factory = factory;
		this.defaultFactory = defaultFactory;
	}

	public T create(NbtCompound nbt)
	{
		return factory.create(this, nbt);
	}

	public T create()
	{
		return defaultFactory.create();
	}

	public interface SpellFactory<T extends Spell<?>>
	{
		T create(SpellType<T> type, NbtCompound nbt);
	}

	public interface DefaultSpellFactory<T extends Spell<?>>
	{
		T create();
	}
}
