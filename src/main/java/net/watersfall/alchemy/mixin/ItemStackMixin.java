package net.watersfall.alchemy.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.abilities.item.PhialStorageAbility;
import net.watersfall.alchemy.abilities.item.RunedShieldAbilityItem;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.item.AlchemyItems;
import net.watersfall.alchemy.item.GlassPhialItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements AbilityProvider<ItemStack>
{
	@Shadow public abstract CompoundTag getOrCreateTag();

	@Shadow private CompoundTag tag;

	@Inject(method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V", at = @At("TAIL"))
	public void addData(ItemConvertible item, int count, CallbackInfo info)
	{
		if(item != null && item.asItem() instanceof GlassPhialItem)
		{
			if(item.asItem() != AlchemyItems.EMPTY_PHIAL_ITEM)
			{
				this.addAbility(new PhialStorageAbility(((GlassPhialItem) item.asItem()).getAspect(), 64));
			}
		}
		else if(item != null && item.asItem() == Items.NETHERITE_CHESTPLATE)
		{
			this.addAbility(new RunedShieldAbilityItem(10, 10 ,10));
		}
	}

	public void addAbility(Ability<ItemStack> ability)
	{
		this.getOrCreateTag().put(ability.getId().toString(), ability.toNbt(new CompoundTag()));
	}

	public void removeAbility(Ability<ItemStack> ability)
	{
		removeAbility(ability.getId());
	}

	public void removeAbility(Identifier id)
	{
		this.getOrCreateTag().remove(id.toString());
	}

	public <R> Optional<R> getAbility(Identifier id, Class<R> clazz)
	{
		if(this.getOrCreateTag().contains(id.toString()))
		{
			CompoundTag tag = this.tag.getCompound(id.toString());
			Ability<ItemStack> ability = AbilityProvider.ITEM_REGISTRY.create(id, tag);
			if(clazz.isInstance(ability))
			{
				return Optional.of(clazz.cast(ability));
			}

		}
		return Optional.empty();
	}

	public CompoundTag toNbt(CompoundTag tag)
	{
		return tag;
	}

	public void fromNbt(CompoundTag tag)
	{

	}

	public PacketByteBuf toPacket(PacketByteBuf buf) {return buf;}

	@Environment(EnvType.CLIENT)
	public void fromPacket(PacketByteBuf buf) {}

	public void sync(ItemStack  stack) {}
}
