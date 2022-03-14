package net.watersfall.thuwumcraft.client.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.particle.ParticleTypes;
import net.watersfall.thuwumcraft.network.s2c.HealingParticleS2CPacket;
import net.watersfall.thuwumcraft.network.s2c.MindMobSpawnS2CPacket;

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

	public static void spawnHealingParticles(HealingParticleS2CPacket packet)
	{
		Entity entity = MinecraftClient.getInstance().world.getEntityById(packet.getId());
		MinecraftClient.getInstance().particleManager.addEmitter(entity, ParticleTypes.HEART);
	}
}
