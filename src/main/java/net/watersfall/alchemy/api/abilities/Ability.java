package net.watersfall.alchemy.api.abilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public interface Ability<T>
{
	Identifier getId();

	CompoundTag toNbt(CompoundTag tag, T t);

	void fromNbt(CompoundTag tag, T t);

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
		Ability<T> create(CompoundTag tag, T t);
	}

	@FunctionalInterface
	interface FactoryPacket<T>
	{
		Ability<T> create(PacketByteBuf buf);
	}
}
