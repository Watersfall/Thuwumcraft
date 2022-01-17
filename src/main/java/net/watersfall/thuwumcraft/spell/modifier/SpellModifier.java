package net.watersfall.thuwumcraft.spell.modifier;

import net.minecraft.nbt.NbtCompound;

public abstract class SpellModifier
{
	public SpellModifier() { }

	public SpellModifier(NbtCompound nbt)
	{
		fromNbt(nbt);
	}

	public abstract void fromNbt(NbtCompound nbt);

	public abstract NbtCompound toNbt(NbtCompound nbt);
}
