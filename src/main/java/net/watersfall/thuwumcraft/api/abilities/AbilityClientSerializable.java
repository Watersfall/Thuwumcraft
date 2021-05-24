package net.watersfall.thuwumcraft.api.abilities;

import net.minecraft.network.PacketByteBuf;

public interface AbilityClientSerializable<T>
{
	PacketByteBuf toPacket(PacketByteBuf buf);

	void fromPacket(PacketByteBuf buf);

	void sync(T t);
}
