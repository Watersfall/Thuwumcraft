package net.watersfall.alchemy.api.abilities;

import net.minecraft.network.PacketByteBuf;

public interface AbilityClientSerializable
{
	PacketByteBuf toPacket(PacketByteBuf buf);

	void fromPacket(PacketByteBuf buf);
}
