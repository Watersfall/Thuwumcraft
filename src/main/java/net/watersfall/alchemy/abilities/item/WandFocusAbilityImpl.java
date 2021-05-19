package net.watersfall.alchemy.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.api.abilities.item.WandFocusAbility;
import net.watersfall.alchemy.spell.Spell;
import net.watersfall.alchemy.spell.SpellActionInstance;

public class WandFocusAbilityImpl implements WandFocusAbility
{
	private NbtCompound tag;
	private SpellActionInstance spell;

	public WandFocusAbilityImpl(Spell spell, ItemStack stack)
	{
		this.tag = new NbtCompound();
		setSpell(new SpellActionInstance(spell, 0, 0));
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
		return this.tag;
	}

	@Override
	public void fromNbt(NbtCompound tag, ItemStack stack)
	{
		this.tag = tag;
		Spell action = Spell.REGISTRY.get(new Identifier(tag.getString("spell")));
		setSpell(new SpellActionInstance(action, 0, 0));
	}

	@Override
	public SpellActionInstance getSpell()
	{
		return this.spell;
	}

	@Override
	public void setSpell(SpellActionInstance spell)
	{
		this.spell = spell;
		tag.putString("spell", Spell.REGISTRY.getId(spell.spell()).toString());
	}
}
