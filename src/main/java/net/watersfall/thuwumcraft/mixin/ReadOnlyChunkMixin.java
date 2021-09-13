package net.watersfall.thuwumcraft.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.*;
import net.watersfall.thuwumcraft.abilities.chunk.VisAbilityImpl;
import net.watersfall.thuwumcraft.api.abilities.Ability;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ReadOnlyChunk.class)
public class ReadOnlyChunkMixin implements AbilityProvider<Chunk>
{
	private AbilityProvider<Chunk> parentProvider;

	@Inject(method = "<init>", at = @At("TAIL"))
	public void thuwumcraft$addAbilities(WorldChunk wrapped, CallbackInfo ci)
	{
		parentProvider = AbilityProvider.getProvider(wrapped);
		parentProvider.addAbility(new VisAbilityImpl());
	}

	@Override
	public void tick(Chunk chunk)
	{
		parentProvider.tick(chunk);
	}

	@Override
	public void addAbility(Ability<Chunk> ability)
	{
		parentProvider.addAbility(ability);
	}

	@Override
	public void removeAbility(Ability<Chunk> ability)
	{
		parentProvider.removeAbility(ability);
	}

	@Override
	public void removeAbility(Identifier id)
	{
		parentProvider.removeAbility(id);
	}

	@Override
	public void copy(Chunk to, boolean alive)
	{
		parentProvider.copy(to, alive);
	}

	@Override
	public <R> Optional<R> getAbility(Identifier id, Class<R> clazz)
	{
		return parentProvider.getAbility(id, clazz);
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag)
	{
		return parentProvider.toNbt(tag);
	}

	@Override
	public void fromNbt(NbtCompound tag)
	{
		parentProvider.fromNbt(tag);
	}

	@Override
	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		return parentProvider.toPacket(buf);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void fromPacket(PacketByteBuf buf)
	{
		parentProvider.fromPacket(buf);
	}

	@Override
	public void clear()
	{
		parentProvider.clear();
	}
}
