package net.watersfall.thuwumcraft.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.api.abilities.item.WandFocusAbility;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.api.spell.Spell;

public class WandFocusAbilityImpl implements WandFocusAbility
{
	private Spell<?> spell;

	public WandFocusAbilityImpl(Spell<?> spell, ItemStack stack)
	{
		setSpell(spell);
	}

	public WandFocusAbilityImpl(NbtCompound tag, ItemStack stack)
	{
		this.fromNbt(tag, stack);
	}

	@Override
	public Identifier getId()
	{
		return WandFocusAbility.ID;
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag, ItemStack stack)
	{
		if(spell != null)
		{
			NbtCompound nbt = new NbtCompound();
			nbt.putString("id", ThuwumcraftRegistry.SPELL.getId(spell.getType()).toString());
			spell.toNbt(nbt);
			tag.put("spell", nbt);
		}
		return tag;
	}

	@Override
	public void fromNbt(NbtCompound tag, ItemStack stack)
	{
		if(tag.contains("spell"))
		{
			NbtCompound nbt = tag.getCompound("spell");
			Spell<?> action = ThuwumcraftRegistry.SPELL.get(new Identifier(nbt.getString("id"))).create(nbt);
			setSpell(action);
		}
	}

	@Override
	public Spell<?> getSpell()
	{
		return this.spell;
	}

	@Override
	public void setSpell(Spell<?> spell)
	{
		this.spell = spell;
	}
}
