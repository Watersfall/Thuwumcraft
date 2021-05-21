package net.watersfall.alchemy.api.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.item.wand.WandCapMaterial;
import net.watersfall.alchemy.item.wand.WandCoreMaterial;
import net.watersfall.alchemy.spell.SpellActionInstance;

public interface WandAbility extends Ability<ItemStack>
{
	public static final Identifier ID = new Identifier("waters_alchemy_mod", "wand_ability");
	@Override
	default Identifier getId()
	{
		return ID;
	}

	WandCapMaterial getWandCap();

	WandCoreMaterial getWandCore();

	SpellActionInstance getSpell();

	void setWandCap(WandCapMaterial cap);

	void setWandCore(WandCoreMaterial core);

	void setSpell(SpellActionInstance spell);

	boolean canCast();
}
