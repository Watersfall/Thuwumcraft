package net.watersfall.thuwumcraft.api.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.item.wand.CapRechargeType;
import net.watersfall.thuwumcraft.item.wand.WandCapMaterial;
import net.watersfall.thuwumcraft.item.wand.WandCoreMaterial;
import net.watersfall.thuwumcraft.spell.SpellActionInstance;
import net.watersfall.wet.api.abilities.Ability;

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

	SpellActionInstance getSpell();

	double getVis();

	void setWandCap(WandCapMaterial cap);

	void setWandCore(WandCoreMaterial core);

	void setSpell(SpellActionInstance spell);

	void setVis(double vis);

	boolean canCast();

	boolean canCharge(CapRechargeType type);
}
