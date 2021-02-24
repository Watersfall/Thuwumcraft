package net.watersfall.alchemy.api.abilities;

import net.minecraft.network.PacketByteBuf;

public interface AbilityClientSerializable<T>
{
	PacketByteBuf toPacket(PacketByteBuf buf);

	void fromPacket(PacketByteBuf buf);

	void sync(T t);

	void sync(T t, PacketByteBuf buf);
}
