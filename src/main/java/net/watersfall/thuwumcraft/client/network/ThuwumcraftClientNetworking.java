package net.watersfall.thuwumcraft.client.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.watersfall.thuwumcraft.network.MindMobSpawnS2CPacket;

public class ThuwumcraftClientNetworking
{
	public static void spawnMindMob(MindMobSpawnS2CPacket packet, ClientPlayPacketListener clientPlayPacketListener)
	{
		PlayerEntity player = MinecraftClient.getInstance().player;
		if(player != null && player.getGameProfile().getId().equals(packet.getOwner()))
		{
			clientPlayPacketListener.onMobSpawn(packet);
		}
	}
}
