package net.watersfall.alchemy.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.api.abilities.item.WandAbility;
import net.watersfall.alchemy.item.wand.WandCapMaterial;
import net.watersfall.alchemy.item.wand.WandCapMaterials;
import net.watersfall.alchemy.item.wand.WandCoreMaterial;
import net.watersfall.alchemy.item.wand.WandCoreMaterials;
import net.watersfall.alchemy.spell.SpellAction;
import net.watersfall.alchemy.spell.SpellActionInstance;

public class WandAbilityImpl implements WandAbility
{
	private WandCapMaterial cap;
	private WandCoreMaterial core;
	private SpellActionInstance spell;
	private NbtCompound tag;

	public WandAbilityImpl()
	{
		this.tag = new NbtCompound();
		setWandCap(WandCapMaterials.IRON);
		setWandCore(WandCoreMaterials.WOOD);
		setSpell(new SpellActionInstance(SpellAction.SAND, 0, 0));
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
	public void setWandCap(WandCapMaterial cap)
	{
		this.cap = cap;
		tag.putString("cap", cap.getId().toString());
	}

	@Override
	public void setWandCore(WandCoreMaterial core)
	{
		this.core = core;
		tag.putString("core", core.getId().toString());
	}

	@Override
	public void setSpell(SpellActionInstance spell)
	{
		this.spell = spell;
		tag.putString("spell", SpellAction.REGISTRY.getId(spell.action()).toString());
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
		SpellAction action = SpellAction.REGISTRY.get(new Identifier(tag.getString("spell")));
		this.spell = new SpellActionInstance(action, 0, 0);
	}
}
