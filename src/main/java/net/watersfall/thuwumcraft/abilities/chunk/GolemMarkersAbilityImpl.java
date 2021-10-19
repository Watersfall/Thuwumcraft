package net.watersfall.thuwumcraft.abilities.chunk;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.abilities.chunk.GolemMarkersAbility;
import net.watersfall.thuwumcraft.api.golem.GolemMarker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GolemMarkersAbilityImpl implements GolemMarkersAbility
{
	private final List<GolemMarker> markers;

	public GolemMarkersAbilityImpl()
	{
		markers = new ArrayList<>();
	}

	public GolemMarkersAbilityImpl(NbtCompound nbt, Chunk chunk)
	{
		markers = new ArrayList<>();
		this.fromNbt(nbt, chunk);
	}

	public GolemMarkersAbilityImpl(PacketByteBuf buf)
	{
		markers = new ArrayList<>();
		this.fromPacket(buf);
	}

	@Override
	public void removeMarker(BlockPos pos, Direction dir)
	{
		for(int i = 0; i < markers.size(); i++)
		{
			if(markers.get(i).pos().equals(pos) && markers.get(i).side() == dir)
			{
				markers.remove(i);
				break;
			}
		}
	}

	@Override
	public void removeMarkers(BlockPos pos)
	{
		for(int i = 0; i < markers.size(); i++)
		{
			if(markers.get(i).pos().equals(pos))
			{
				markers.remove(i);
				i--;
			}
		}
	}

	@Override
	public void addMarker(GolemMarker marker)
	{
		this.markers.add(marker);
	}

	@Override
	public Optional<GolemMarker> getMarker(BlockPos pos, Direction dir)
	{
		for(int i = 0; i < markers.size(); i++)
		{
			if(markers.get(i).pos().equals(pos) && markers.get(i).side() == dir)
			{
				return Optional.of(markers.get(i));
			}
		}
		return Optional.empty();
	}

	@Override
	public List<GolemMarker> getMarkers(BlockPos pos)
	{
		List<GolemMarker> list = new ArrayList<>();
		for(int i = 0; i < markers.size(); i++)
		{
			if(markers.get(i).pos().equals(pos))
			{
				list.add(markers.get(i));
			}
		}
		return list;
	}

	@Override
	public Optional<GolemMarker> getClosestMarker(BlockPos pos, DyeColor color, boolean exclude)
	{
		double distance = Integer.MAX_VALUE;
		GolemMarker closest = null;
		for(GolemMarker marker : markers)
		{
			if(exclude && marker.pos().equals(pos))
			{
				continue;
			}
			if(marker.color() == color || marker.color() == DyeColor.WHITE || color == DyeColor.WHITE)
			{
				double test = pos.getSquaredDistance(marker.pos());
				if(test < distance)
				{
					distance = test;
					closest = marker;
				}
			}
		}
		if(closest == null)
		{
			return Optional.empty();
		}
		return Optional.of(closest);
	}

	@Override
	public List<GolemMarker> getAllMarkers()
	{
		return markers;
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag, Chunk chunk)
	{
		int i = 0;
		for(GolemMarker marker : markers)
		{
			tag.put(i + "", marker.toNbt(new NbtCompound()));
		}
		return tag;
	}

	@Override
	public void fromNbt(NbtCompound tag, Chunk chunk)
	{
		int i = 0;
		NbtCompound element;
		while((element = tag.getCompound(i + "")) != null && !element.isEmpty())
		{
			markers.add(new GolemMarker(element));
			i++;
		}
	}

	@Override
	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeInt(markers.size());
		for(GolemMarker marker : markers)
		{
			marker.toPacket(buf);
		}
		return buf;
	}

	@Override
	public void fromPacket(PacketByteBuf buf)
	{
		markers.clear();
		int size = buf.readInt();
		for(int i = 0; i < size; i++)
		{
			markers.add(new GolemMarker(buf));
		}
	}

	@Override
	public void sync(Chunk chunk)
	{
		if(chunk instanceof WorldChunk worldChunk)
		{
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeLong(chunk.getPos().toLong());
			this.toPacket(buf);
			for(ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld)worldChunk.getWorld(), chunk.getPos()))
			{
				ServerPlayNetworking.send(player, Thuwumcraft.getId("golem_markers"), buf);
			}
		}
	}
}
