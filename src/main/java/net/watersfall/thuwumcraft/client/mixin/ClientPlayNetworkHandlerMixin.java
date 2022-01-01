package net.watersfall.thuwumcraft.client.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.watersfall.thuwumcraft.block.entity.BlockEntityClientSerializable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin
{
	@Inject(method = "method_38542", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"), cancellable = true)
	public void thuwumcraft$blockEntitySyncing(BlockEntityUpdateS2CPacket packet, BlockEntity entity, CallbackInfo info)
	{
		if(entity instanceof BlockEntityClientSerializable)
		{
			((BlockEntityClientSerializable)entity).fromClientTag(packet.getNbt());
			info.cancel();
		}
	}
}
