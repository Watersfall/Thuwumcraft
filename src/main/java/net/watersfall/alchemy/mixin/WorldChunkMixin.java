package net.watersfall.alchemy.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.TickScheduler;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.chunk.WorldChunk;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.abilities.chunk.VisAbilityImpl;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.api.abilities.AbilityClientSerializable;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.chunk.VisAbility;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin implements AbilityProvider<Chunk>
{
	@Shadow @Final private ChunkPos pos;

	@Shadow public abstract void setShouldSave(boolean shouldSave);

	private final HashMap<Identifier, Ability<Chunk>> abilities = new HashMap<>();

	@Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/biome/source/BiomeArray;Lnet/minecraft/world/chunk/UpgradeData;Lnet/minecraft/world/TickScheduler;Lnet/minecraft/world/TickScheduler;J[Lnet/minecraft/world/chunk/ChunkSection;Ljava/util/function/Consumer;)V", at = @At("TAIL"))
	public void addAbilities(World world, ChunkPos pos, BiomeArray biomes, UpgradeData upgradeData, TickScheduler<Block> blockTickScheduler, TickScheduler<Fluid> fluidTickScheduler, long inhabitedTime, ChunkSection[] sections, Consumer<WorldChunk> loadToWorldConsumer, CallbackInfo ci)
	{
		AbilityProvider<Chunk> provider = AbilityProvider.getProvider((Chunk)(Object)this);
		provider.addAbility(new VisAbilityImpl());
	}

	@Override
	public void addAbility(Ability<Chunk> ability)
	{
		this.abilities.put(ability.getId(), ability);
	}

	@Override
	public void removeAbility(Ability<Chunk> ability)
	{
		this.abilities.remove(ability.getId());
	}

	@Override
	public void removeAbility(Identifier id)
	{
		this.abilities.remove(id);
	}

	@Override
	public void copy(Chunk to, boolean alive)
	{

	}

	@Override
	public <R> Optional<R> getAbility(Identifier id, Class<R> clazz)
	{
		if(this.abilities.containsKey(id))
		{
			Ability<Chunk> ability = this.abilities.get(id);
			if(clazz.isInstance(ability))
			{
				return Optional.of(clazz.cast(ability));
			}
		}
		return Optional.empty();
	}

	@Override
	public void tick(Chunk chunk)
	{
		this.abilities.values().forEach((ability) -> {
			ability.tick(chunk);
		});
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag)
	{
		NbtCompound compound = new NbtCompound();
		HashMap<Identifier, Ability<Chunk>> abilities = this.abilities;
		for(Ability<Chunk> ability : abilities.values())
		{
			compound.put(ability.getId().toString(), ability.toNbt(new NbtCompound(), (Chunk)(Object)this));
		}
		tag.put("waters_abilities", compound);
		return tag;
	}

	@Override
	public void fromNbt(NbtCompound tag)
	{
		NbtCompound compound = tag.getCompound("waters_abilities");
		HashMap<Identifier, Ability<Chunk>> abilities = this.abilities;
		for(String key : compound.getKeys())
		{
			abilities.put(Identifier.tryParse(key), AbilityProvider.CHUNK_REGISTRY.create(Identifier.tryParse(key), compound.getCompound(key), (Chunk)(Object)this));
		}
	}

	@Override
	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeInt(this.abilities.size());
		for(Ability<Chunk> ability : abilities.values())
		{
			if(ability instanceof AbilityClientSerializable)
			{
				buf.writeIdentifier(ability.getId());
				((AbilityClientSerializable)ability).toPacket(buf);
			}
		}
		return buf;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void fromPacket(PacketByteBuf buf)
	{
		HashMap<Identifier, Ability<Chunk>> temp = new HashMap<>();
		int size = buf.readInt();
		for(int i = 0; i < size; i++)
		{
			Identifier id = buf.readIdentifier();
			temp.put(id, AbilityProvider.CHUNK_REGISTRY.create(id, buf));
		}
		MinecraftClient.getInstance().execute(() -> {
			abilities.clear();
			abilities.putAll(temp);
		});
	}

	@Override
	public void sync(Chunk chunk)
	{

	}

	@Override
	public void clear()
	{
		this.abilities.clear();
	}
}
