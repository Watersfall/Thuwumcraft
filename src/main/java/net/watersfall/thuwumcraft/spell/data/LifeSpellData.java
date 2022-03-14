package net.watersfall.thuwumcraft.spell.data;

import net.minecraft.nbt.NbtCompound;
import net.watersfall.thuwumcraft.api.spell.SpellModifierData;
import net.watersfall.thuwumcraft.api.spell.SpellModifierDataType;
import net.watersfall.thuwumcraft.api.spell.modifier.BooleanSpellModifier;
import net.watersfall.thuwumcraft.api.spell.modifier.IntegerSpellModifier;
import net.watersfall.thuwumcraft.api.spell.modifier.SpellModifier;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSpellData;

import java.util.List;

public class LifeSpellData extends SpellModifierData
{
	private final BooleanSpellModifier projectile;
	private final IntegerSpellModifier amount;

	public LifeSpellData(SpellModifierDataType<? extends SpellModifierData> type, NbtCompound tag)
	{
		super(type, tag);
		this.projectile = new BooleanSpellModifier("spell_modifier.thuwumcraft.projectile", false);
		this.amount = new IntegerSpellModifier("spell_modifier.thuwumcraft.healing_amount", 1, 10, 1);
	}

	public LifeSpellData(int healing, boolean projectile)
	{
		super(ThuwumcraftSpellData.LIFE, new NbtCompound());
		this.projectile = new BooleanSpellModifier("spell_modifier.thuwumcraft.projectile", projectile);
		this.amount = new IntegerSpellModifier("spell_modifier.thuwumcraft.healing_amount", 1, 10, healing);
	}

	public boolean isProjectile()
	{
		return projectile.getValue();
	}

	public int getAmount()
	{
		return amount.getValue();
	}

	@Override
	public List<SpellModifier> getModifiers()
	{
		return List.of(projectile, amount);
	}
}
