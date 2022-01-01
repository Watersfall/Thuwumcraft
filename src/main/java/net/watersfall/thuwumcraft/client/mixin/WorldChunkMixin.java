package net.watersfall.thuwumcraft.client.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.chunk.WorldChunk;
import net.watersfall.thuwumcraft.block.entity.BlockEntityClientSerializable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldChunk.class)
public class WorldChunkMixin
{
	@Redirect(method = "method_31716", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
	private void watersrandom$readBlockEntity(BlockEntity entity, NbtCompound nbt)
	{
		if(entity instanceof BlockEntityClientSerializable client)
		{
			client.fromClientTag(nbt);
		}
		else
		{
			entity.readNbt(nbt);
		}
	}
}
