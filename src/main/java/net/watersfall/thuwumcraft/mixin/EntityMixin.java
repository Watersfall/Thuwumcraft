package net.watersfall.thuwumcraft.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.abilities.Ability;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.hooks.AbilityDelegate;
import net.watersfall.thuwumcraft.hooks.AbilityHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Entity.class)
public abstract class EntityMixin implements AbilityProvider<Entity>
{
	@Shadow public abstract int getId();

	private final AbilityDelegate<Entity> delegate = new AbilityDelegate<>();

	@Inject(method = "<init>", at = @At("TAIL"))
	public void thuwumcraft$addToConstructor(EntityType<? extends Entity> type, World world, CallbackInfo info)
	{
		AbilityHooks.addEntityAbilities(type, world, this);
	}

	@Inject(method = "writeNbt", at = @At("RETURN"))
	public void thuwumcraft$writeCustomData(NbtCompound tag, CallbackInfoReturnable<NbtCompound> info)
	{
		AbilityHooks.writeEntityAbilities(tag, this);
	}

	@Inject(method = "readNbt", at = @At("TAIL"))
	public void thuwumcraft$readCustomData(NbtCompound tag, CallbackInfo info)
	{
		AbilityHooks.readEntityAbilities(tag, this);
	}

	@Override
	public void addAbility(Ability<Entity> ability)
	{
		delegate.addAbility(ability);
	}

	@Override
	public void removeAbility(Identifier id)
	{
		delegate.removeAbility(id);
	}

	@Override
	public <R> Optional<R> getAbility(Identifier id, Class<R> clazz)
	{
		return delegate.getAbility(id, clazz);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void thuwumcraft$tick(CallbackInfo info)
	{
		delegate.tick((Entity)(Object)this);
	}

	@Override
	public void copy(Entity to, boolean alive)
	{
		delegate.copy(to, alive);
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag)
	{
		return delegate.toNbt(tag, (Entity)(Object)this);
	}

	@Override
	public void fromNbt(NbtCompound tag)
	{
		delegate.fromNbt(tag, (Entity)(Object)this, this);
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
		delegate.fromPacket(buf, this, (Entity)(Object)this);
	}

	@Override
	public void clear()
	{
		delegate.clear();
	}
}
