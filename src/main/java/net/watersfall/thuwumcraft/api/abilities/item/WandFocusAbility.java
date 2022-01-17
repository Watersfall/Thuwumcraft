package net.watersfall.thuwumcraft.api.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.spell.Spell;
import net.watersfall.wet.api.abilities.Ability;

public interface WandFocusAbility extends Ability<ItemStack>
{
	public static final Identifier ID = new Identifier("thuwumcraft", "wand_focus_ability");

	Spell<?> getSpell();

	void setSpell(Spell<?> spell);
}
