package net.watersfall.thuwumcraft.api.golem;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;

public record GolemMarker(DyeColor color, BlockPos pos, ChunkPos chunk, Direction side)
{
	public GolemMarker(NbtCompound nbt)
	{
		this(DyeColor.byId(nbt.getInt("color")), BlockPos.fromLong(nbt.getLong("pos")), new ChunkPos(nbt.getLong("chunk")), Direction.byId(nbt.getInt("direction")));
	}

	public GolemMarker(PacketByteBuf buf)
	{
		this(DyeColor.byId(buf.readInt()), BlockPos.fromLong(buf.readLong()), new ChunkPos(buf.readLong()), Direction.byId(buf.readInt()));
	}

	public NbtCompound toNbt(NbtCompound nbt)
	{
		nbt.putLong("pos", pos.asLong());
		nbt.putInt("color", color.getId());
		nbt.putLong("chunk", chunk.toLong());
		nbt.putInt("direction", side.getId());
		return nbt;
	}

	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeInt(color.getId());
		buf.writeLong(pos.asLong());
		buf.writeLong(chunk.toLong());
		buf.writeInt(side.getId());
		return buf;
	}
}
