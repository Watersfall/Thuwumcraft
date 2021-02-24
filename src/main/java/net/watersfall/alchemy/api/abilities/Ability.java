package net.watersfall.alchemy.api.abilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public interface Ability<T>
{
	Identifier getId();

	CompoundTag toNbt(CompoundTag tag);

	void fromNbt(CompoundTag tag);

	default void tick(T t)
	{

	}

	interface Factory<K, T>
	{
		Ability<T> create(K t);
	}

	@FunctionalInterface
	interface FactoryTag<T> extends Factory<CompoundTag, T>
	{
		@Override
		Ability<T> create(CompoundTag tag);
	}

	@FunctionalInterface
	interface FactoryPacket<T> extends Factory<PacketByteBuf, T>
	{
		@Override
		Ability<T> create(PacketByteBuf buf);
	}
}
