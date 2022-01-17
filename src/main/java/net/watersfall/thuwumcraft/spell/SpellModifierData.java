package net.watersfall.thuwumcraft.spell;

import net.minecraft.nbt.NbtCompound;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.spell.modifier.SpellModifier;

import java.util.List;

public abstract class SpellModifierData
{
	protected SpellModifierDataType<? extends SpellModifierData> type;

	public SpellModifierData(SpellModifierDataType<? extends SpellModifierData> type, NbtCompound tag)
	{
		this.type = type;
	}

	public NbtCompound toNbt(NbtCompound nbt)
	{
		nbt.putString("id", ThuwumcraftRegistry.SPELL_DATA.getId(type).toString());
		getModifiers().forEach(modifier -> modifier.toNbt(nbt));
		return nbt;
	}

	public abstract List<SpellModifier> getModifiers();
}
