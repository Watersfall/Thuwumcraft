package net.watersfall.alchemy.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.abilities.item.PhialStorageAbility;
import net.watersfall.alchemy.abilities.item.RunedShieldAbilityItem;
import net.watersfall.alchemy.abilities.item.WandAbilityImpl;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.item.AlchemyItems;
import net.watersfall.alchemy.item.GlassPhialItem;
import net.watersfall.alchemy.item.wand.WandItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements AbilityProvider<ItemStack>
{
	@Shadow public abstract NbtCompound getOrCreateTag();

	@Shadow private NbtCompound tag;

	@Shadow @Nullable public abstract NbtCompound getTag();

	@Shadow public abstract NbtCompound getOrCreateSubTag(String key);

	private final Map<Identifier, Ability<ItemStack>> abilities = new HashMap<>();

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
		else if(item != null && item.asItem() instanceof WandItem)
		{
			this.addAbility(new WandAbilityImpl());
		}
	}

	@Inject(method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;postProcessNbt(Lnet/minecraft/nbt/NbtCompound;)Z"))
	public void  addData(NbtCompound tag, CallbackInfo info)
	{
		this.fromNbt(this.tag);
	}

	@Inject(method = "writeNbt", at = @At(value = "RETURN"))
	public void writeData(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir)
	{
		NbtCompound tag = nbt.getCompound("tag");
		NbtCompound abilitiesTag = new NbtCompound();
		this.abilities.forEach((k, v) -> {
			abilitiesTag.put(k.toString(), v.toNbt(new NbtCompound(), (ItemStack)(Object)this));
		});
		if(!abilitiesTag.isEmpty())
		{
			tag.put("waters_alchemy_mod:abilities", abilitiesTag);
			nbt.put("tag", tag);
		}
	}

	@Override
	public void addAbility(Ability<ItemStack> ability)
	{
		NbtCompound tag = this.getOrCreateSubTag("waters_alchemy_mod:abilities");
		tag.put(ability.getId().toString(), ability.toNbt(new NbtCompound(), (ItemStack)(Object)this));
		this.abilities.put(ability.getId(), ability);
	}

	@Override
	public void removeAbility(Ability<ItemStack> ability)
	{
		removeAbility(ability.getId());
	}

	@Override
	public void removeAbility(Identifier id)
	{
		this.abilities.remove(id);
		this.getOrCreateTag().remove(id.toString());
	}

	@Override
	public <R> Optional<R> getAbility(Identifier id, Class<R> clazz)
	{
		if(this.abilities.containsKey(id))
		{
			Ability<ItemStack> ability = this.abilities.get(id);
			if(clazz.isInstance(ability))
			{
				return Optional.of(clazz.cast(ability));
			}
		}
		return Optional.empty();
	}

	@Override
	public void tick(ItemStack stack)
	{

	}

	@Inject(method = "copy", at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
	public void copy(CallbackInfoReturnable<ItemStack> info, ItemStack to)
	{
		this.copy(to, true);
	}

	@Inject(method = "setTag", at = @At("TAIL"))
	public void setTag(NbtCompound tag, CallbackInfo info)
	{
		this.abilities.clear();
		this.fromNbt(tag);
	}

	@Override
	public void copy(ItemStack to, boolean alive)
	{
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(to);
		this.abilities.forEach((id, ability) -> {
			provider.addAbility(ability);
		});
		if(!this.abilities.isEmpty())
		{
			to.getOrCreateTag().put("waters_alchemy_mod:abilities", provider.toNbt(new NbtCompound()));
		}
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag)
	{
		this.abilities.forEach((key, ability) -> {
			tag.put(key.toString(), ability.toNbt(new NbtCompound(), (ItemStack)(Object)this));
		});
		return tag;
	}

	@Override
	public void fromNbt(NbtCompound tag)
	{
		if(tag != null)
		{
			NbtCompound abilitiesTag = tag.getCompound("waters_alchemy_mod:abilities");
			if(!abilitiesTag.isEmpty())
			{
				abilitiesTag.getKeys().forEach(key -> {
					this.abilities.put(Identifier.tryParse(key), AbilityProvider.ITEM_REGISTRY.create(Identifier.tryParse(key), abilitiesTag.getCompound(key), (ItemStack)(Object)this));
				});
			}
		}
	}

	@Override
	public PacketByteBuf toPacket(PacketByteBuf buf) {return buf;}

	@Override
	@Environment(EnvType.CLIENT)
	public void fromPacket(PacketByteBuf buf) {}

	@Override
	public void sync(ItemStack  stack) {}
}
