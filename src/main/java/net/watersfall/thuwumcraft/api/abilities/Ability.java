package net.watersfall.thuwumcraft.api.abilities;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public interface Ability<T>
{
	Identifier getId();

	NbtCompound toNbt(NbtCompound tag, T t);

	void fromNbt(NbtCompound tag, T t);

	default void tick(T t)
	{

	}

	default boolean copyable()
	{
		return false;
	}

	@FunctionalInterface
	interface FactoryTag<T>
	{
		Ability<T> create(NbtCompound tag, T t);
	}

	@FunctionalInterface
	interface FactoryPacket<T>
	{
		Ability<T> create(PacketByteBuf buf);
	}
}
