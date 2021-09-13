package net.watersfall.thuwumcraft.mixin;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.watersfall.thuwumcraft.hooks.AbilityHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkSerializer.class)
public abstract class ChunkSerializerMixin
{
	@Inject(method = "deserialize", at = @At(value = "RETURN", ordinal = 0))
	private static void thuwumcraft$readAbilities(ServerWorld world, StructureManager structureManager, PointOfInterestStorage poiStorage, ChunkPos pos, NbtCompound tag, CallbackInfoReturnable<ProtoChunk> cir)
	{
		AbilityHooks.readChunkAbilities(cir, tag);
	}

	@Inject(method = "serialize", at = @At("RETURN"))
	private static void thuwumcraft$writeAbilities(ServerWorld world, Chunk chunk, CallbackInfoReturnable<NbtCompound> cir)
	{
		AbilityHooks.writeChunkAbilities(cir, chunk);
	}
}
