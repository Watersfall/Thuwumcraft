package net.watersfall.thuwumcraft.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.TickScheduler;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.chunk.WorldChunk;
import net.watersfall.thuwumcraft.api.abilities.Ability;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.hooks.AbilityDelegate;
import net.watersfall.thuwumcraft.hooks.AbilityHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin implements AbilityProvider<Chunk>
{
	private final AbilityDelegate<Chunk> delegate = new AbilityDelegate<>();

	@Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/biome/source/BiomeArray;Lnet/minecraft/world/chunk/UpgradeData;Lnet/minecraft/world/TickScheduler;Lnet/minecraft/world/TickScheduler;J[Lnet/minecraft/world/chunk/ChunkSection;Ljava/util/function/Consumer;)V", at = @At("TAIL"))
	public void thuwumcraft$addAbilities(World world, ChunkPos pos, BiomeArray biomes, UpgradeData upgradeData, TickScheduler<Block> blockTickScheduler, TickScheduler<Fluid> fluidTickScheduler, long inhabitedTime, ChunkSection[] sections, Consumer<WorldChunk> loadToWorldConsumer, CallbackInfo ci)
	{
		AbilityHooks.addChunkAbilities(this);
	}

	@Override
	public void addAbility(Ability<Chunk> ability)
	{
		delegate.addAbility(ability);
	}

	@Override
	public void removeAbility(Identifier id)
	{
		delegate.removeAbility(id);
	}

	@Override
	public void copy(Chunk to, boolean alive)
	{
		delegate.copy(to, alive);
	}

	@Override
	public <R> Optional<R> getAbility(Identifier id, Class<R> clazz)
	{
		return delegate.getAbility(id, clazz);
	}

	@Override
	public void tick(Chunk chunk)
	{
		delegate.tick(chunk);
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag)
	{
		return delegate.toNbt(tag, (Chunk)this);
	}

	@Override
	public void fromNbt(NbtCompound tag)
	{
		delegate.fromNbt(tag, (Chunk)this, this);
	}

	@Override
	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		return delegate.toPacket(buf);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void fromPacket(PacketByteBuf buf)
	{
		delegate.fromPacket(buf, this, (Chunk)this);
	}

	@Override
	public void clear()
	{
		delegate.clear();
	}
}
