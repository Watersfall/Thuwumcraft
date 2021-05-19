package net.watersfall.alchemy.api.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.spell.SpellActionInstance;

public interface WandFocusAbility extends Ability<ItemStack>
{
	public static final Identifier ID = new Identifier("waters_alchemy_mod", "wand_focus_ability");

	SpellActionInstance getSpell();

	void setSpell(SpellActionInstance spell);
}
