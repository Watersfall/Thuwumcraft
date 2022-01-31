package net.watersfall.thuwumcraft.spell.data;

import net.minecraft.nbt.NbtCompound;
import net.watersfall.thuwumcraft.api.spell.SpellModifierData;
import net.watersfall.thuwumcraft.api.spell.SpellModifierDataType;
import net.watersfall.thuwumcraft.api.spell.modifier.IntegerSpellModifier;
import net.watersfall.thuwumcraft.api.spell.modifier.SpellModifier;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSpellData;

import java.util.List;

public class FireSpellData extends SpellModifierData
{
	private final IntegerSpellModifier damage;
	private final IntegerSpellModifier fireTime;

	public FireSpellData(SpellModifierDataType<? extends SpellModifierData> type, NbtCompound tag)
	{
		super(type, tag);
		this.damage = new IntegerSpellModifier(tag.getCompound("spell_modifier.thuwumcraft.damage"));
		this.fireTime = new IntegerSpellModifier(tag.getCompound("spell_modifier.thuwumcraft.fire_time"));
	}

	public FireSpellData(IntegerSpellModifier damage, IntegerSpellModifier fireTime)
	{
		super(ThuwumcraftSpellData.FIRE, new NbtCompound());
		this.damage = damage;
		this.fireTime = fireTime;
	}

	public int getDamage()
	{
		return damage.getValue();
	}

	public int getFireTime()
	{
		return fireTime.getValue();
	}

	@Override
	public List<SpellModifier> getModifiers()
	{
		return List.of(damage, fireTime);
	}
}
