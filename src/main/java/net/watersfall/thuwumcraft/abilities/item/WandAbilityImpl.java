package net.watersfall.thuwumcraft.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.api.abilities.item.WandAbility;
import net.watersfall.thuwumcraft.item.wand.*;
import net.watersfall.thuwumcraft.spell.Spell;
import net.watersfall.thuwumcraft.spell.SpellActionInstance;

public class WandAbilityImpl implements WandAbility
{
	private WandCapMaterial cap;
	private WandCoreMaterial core;
	private SpellActionInstance spell;
	private double vis;
	private NbtCompound tag;

	public WandAbilityImpl()
	{
		this.tag = new NbtCompound();
		setWandCap(WandCapMaterials.IRON);
		setWandCore(WandCoreMaterials.WOOD);
		setSpell(null);
		setVis(getWandCore().getMaxVis());
	}

	public WandAbilityImpl(NbtCompound tag, ItemStack stack)
	{
		this.fromNbt(tag, stack);
	}

	@Override
	public WandCapMaterial getWandCap()
	{
		return cap;
	}

	@Override
	public WandCoreMaterial getWandCore()
	{
		return core;
	}

	@Override
	public SpellActionInstance getSpell()
	{
		return spell;
	}

	@Override
	public double getVis()
	{
		return vis;
	}

	@Override
	public void setWandCap(WandCapMaterial cap)
	{
		this.cap = cap;
		if(cap == null)
		{
			tag.putString("cap", "");
		}
		else
		{
			tag.putString("cap", cap.getId().toString());
		}
	}

	@Override
	public void setWandCore(WandCoreMaterial core)
	{
		this.core = core;
		if(core == null)
		{
			tag.putString("core", "");
		}
		else
		{
			tag.putString("core", core.getId().toString());
		}
	}

	@Override
	public void setSpell(SpellActionInstance spell)
	{
		this.spell = spell;
		if(spell == null)
		{
			tag.putString("spell", "");
		}
		else
		{
			tag.putString("spell", Spell.REGISTRY.getId(spell.spell()).toString());
		}
	}

	@Override
	public void setVis(double vis)
	{
		this.vis = vis;
		tag.putDouble("vis", vis);
	}

	@Override
	public boolean canCast()
	{
		return this.getWandCore() != null
				&& this.getWandCap() != null
				&& this.getSpell() != null
				&& this.getSpell().spell() != null
				&& this.getVis() >= this.getSpell().spell().visCost();
	}

	@Override
	public boolean canCharge(CapRechargeType type)
	{
		return getWandCap() != null && getWandCore() != null && type == getWandCap().getRechargeType() && getVis() < getWandCore().getMaxVis();
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag, ItemStack itemStack)
	{
		return this.tag;
	}

	@Override
	public void fromNbt(NbtCompound tag, ItemStack itemStack)
	{
		this.tag = tag;
		this.cap = WandCapMaterial.REGISTRY.get(new Identifier(tag.getString("cap")));
		this.core = WandCoreMaterial.REGISTRY.get(new Identifier(tag.getString("core")));
		Spell action = Spell.REGISTRY.get(new Identifier(tag.getString("spell")));
		this.spell = new SpellActionInstance(action, 0, 0);
		this.vis = tag.getDouble("vis");
	}
}
