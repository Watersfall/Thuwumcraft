package net.watersfall.thuwumcraft.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.api.abilities.Ability;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.hooks.AbilityDelegate;
import net.watersfall.thuwumcraft.hooks.AbilityHooks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements AbilityProvider<ItemStack>
{
	@Shadow @Nullable public abstract NbtCompound getNbt();

	private final AbilityDelegate<ItemStack> delegate = new AbilityDelegate<>();

	@Inject(method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V", at = @At("TAIL"))
	public void thuwumcraft$addData(ItemConvertible item, int count, CallbackInfo info)
	{
		AbilityHooks.addItemStackAbilities(item, this);
	}

	@Inject(method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;postProcessNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
	public void thuwumcraft$addData(NbtCompound tag, CallbackInfo info)
	{
		AbilityHooks.readItemStackAbilities(tag, this);
	}

	@Inject(method = "writeNbt", at = @At(value = "RETURN"))
	public void thuwumcraft$writeData(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir)
	{
		AbilityHooks.writeItemStackAbilities(nbt, this);
	}

	@Inject(method = "copy", at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
	public void thuwumcraft$copy(CallbackInfoReturnable<ItemStack> info, ItemStack to)
	{
		AbilityHooks.copyItemStackAbilities(to, this);
	}

	@Override
	public void addAbility(Ability<ItemStack> ability)
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

	@Override
	public void tick(ItemStack stack)
	{
		delegate.tick(stack);
	}

	@Override
	public void copy(ItemStack to, boolean alive)
	{
		delegate.copy(to, alive);
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag)
	{
		return delegate.toNbt(tag, (ItemStack)(Object)this);
	}

	@Override
	public void fromNbt(NbtCompound tag)
	{
		delegate.fromNbt(tag, (ItemStack)(Object)this, this);
	}

	@Override
	public PacketByteBuf toPacket(PacketByteBuf buf) {return buf;}

	@Override
	@Environment(EnvType.CLIENT)
	public void fromPacket(PacketByteBuf buf) {}
}
