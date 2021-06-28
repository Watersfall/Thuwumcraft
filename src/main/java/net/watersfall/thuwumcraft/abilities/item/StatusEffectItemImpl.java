package net.watersfall.thuwumcraft.abilities.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.api.abilities.common.StatusEffectItem;
import net.watersfall.thuwumcraft.util.StatusEffectHelper;

import java.util.List;

public class StatusEffectItemImpl implements StatusEffectItem
{
	private List<StatusEffectInstance> effects;
	private int uses;

	public StatusEffectItemImpl(List<StatusEffectInstance> effects, int uses)
	{
		this.effects = effects;
		this.uses = uses;
	}

	public StatusEffectItemImpl(NbtCompound tag, ItemStack stack)
	{
		this.fromNbt(tag, stack);
	}

	@Override
	public Identifier getId()
	{
		return ID;
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag, ItemStack stack)
	{
		//TODO: change a lot of stuff in StatusEffectHelper so I don't have to use this name
		tag.put("waters_effects", StatusEffectHelper.toNbt(getEffects()));
		tag.putInt("uses", getUses());
		return tag;
	}

	@Override
	public void fromNbt(NbtCompound tag, ItemStack stack)
	{
		this.effects = StatusEffectHelper.getEffectsFromTag(tag);
		this.uses = tag.getInt("uses");
	}

	@Override
	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		return buf;
	}

	@Override
	public void fromPacket(PacketByteBuf buf)
	{

	}

	@Override
	public void sync(ItemStack stack)
	{

	}

	@Override
	public List<StatusEffectInstance> getEffects()
	{
		return effects;
	}

	@Override
	public int getUses()
	{
		return uses;
	}

	@Override
	public void setUses(int uses)
	{
		this.uses = uses;
	}
}
