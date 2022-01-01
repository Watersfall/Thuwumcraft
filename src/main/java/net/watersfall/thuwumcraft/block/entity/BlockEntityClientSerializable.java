package net.watersfall.thuwumcraft.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public interface BlockEntityClientSerializable
{
	NbtCompound toClientTag(NbtCompound nbt);

	void fromClientTag(NbtCompound nbt);

	default void sync()
	{
		World world = ((BlockEntity) this).getWorld();
		if (!(world instanceof ServerWorld)) throw new IllegalStateException("Cannot call sync() on the logical client! Did you check world.isClient first?");
		((ServerWorld) world).getChunkManager().markForUpdate(((BlockEntity) this).getPos());
	}
}
