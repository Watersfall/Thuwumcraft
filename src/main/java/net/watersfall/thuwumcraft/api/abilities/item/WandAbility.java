package net.watersfall.thuwumcraft.api.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.item.wand.CapRechargeType;
import net.watersfall.thuwumcraft.item.wand.WandCapMaterial;
import net.watersfall.thuwumcraft.item.wand.WandCoreMaterial;
import net.watersfall.thuwumcraft.api.spell.Spell;
import net.watersfall.wet.api.abilities.Ability;

/**
 * TODO: Wands
 *
 * 		Wands serve as a thaumaturges way of channeling vis into
 * something useful. Even highly attuned thaumaturges capable of
 * using their very bodies for this purpose, will choose wands
 * due to ease of use and placing less stress on the mind and body.
 *
 */
public interface WandAbility extends Ability<ItemStack>
{
	public static final Identifier ID = new Identifier("thuwumcraft", "wand_ability");
	@Override
	default Identifier getId()
	{
		return ID;
	}

	WandCapMaterial getWandCap();

	WandCoreMaterial getWandCore();

	Spell<?> getSpell();

	double getVis();

	void setWandCap(WandCapMaterial cap);

	void setWandCore(WandCoreMaterial core);

	void setSpell(Spell<?> spell);

	void setVis(double vis);

	boolean canCast();

	boolean canCharge(CapRechargeType type);
}
