package net.watersfall.thuwumcraft.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.api.abilities.item.WandAbility;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.item.wand.*;
import net.watersfall.thuwumcraft.api.spell.Spell;

public class WandAbilityImpl implements WandAbility
{
	private WandCapMaterial cap;
	private WandCoreMaterial core;
	private Spell<?> spell;
	private double vis;

	public WandAbilityImpl()
	{
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
	public Spell<?> getSpell()
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
	}

	@Override
	public void setWandCore(WandCoreMaterial core)
	{
		this.core = core;
	}

	@Override
	public void setSpell(Spell<?> spell)
	{
		this.spell = spell;
	}

	@Override
	public void setVis(double vis)
	{
		this.vis = vis;
	}

	@Override
	public boolean canCast()
	{
		return this.getWandCore() != null
				&& this.getWandCap() != null
				&& this.getSpell() != null;
	}

	@Override
	public boolean canCharge(CapRechargeType type)
	{
		return getWandCap() != null && getWandCore() != null && type == getWandCap().getRechargeType() && getVis() < getWandCore().getMaxVis();
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag, ItemStack itemStack)
	{
		if(cap != null)
		{
			tag.putString("cap", cap.getId().toString());
		}
		if(core != null)
		{
			tag.putString("core", core.getId().toString());
		}
		if(spell != null)
		{
			NbtCompound nbt = new NbtCompound();
			nbt.putString("id", ThuwumcraftRegistry.SPELL.getId(spell.getType()).toString());
			spell.toNbt(nbt);
			tag.put("spell", nbt);
		}
		tag.putDouble("vis", vis);
		return tag;
	}

	@Override
	public void fromNbt(NbtCompound tag, ItemStack itemStack)
	{
		if(tag.contains("cap"))
		{
			this.cap = ThuwumcraftRegistry.WAND_CAP.get(new Identifier(tag.getString("cap")));
		}
		if(tag.contains("core"))
		{
			this.core = ThuwumcraftRegistry.WAND_CORE.get(new Identifier(tag.getString("core")));
		}
		if(tag.contains("spell"))
		{
			NbtCompound nbt = tag.getCompound("spell");
			this.spell = ThuwumcraftRegistry.SPELL.get(new Identifier(nbt.getString("id"))).create(nbt);
		}
		this.vis = tag.getDouble("vis");
	}
}
