package net.watersfall.thuwumcraft.item.wand;

import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;

import java.util.function.Supplier;

public enum WandCapMaterials implements WandCapMaterial
{
	IRON(Thuwumcraft.getId("iron"), CapRechargeType.ENVIRONMENTAL, 0xA5A5A5, () -> new ItemStack(ThuwumcraftItems.IRON_CAP)),
	BRASS(Thuwumcraft.getId("brass"), CapRechargeType.ENVIRONMENTAL, 0xE7AC56, () -> new ItemStack(ThuwumcraftItems.BRASS_CAP)),
	THUWUMIUM(Thuwumcraft.getId("thuwumium"), CapRechargeType.CHARGER, 0x976CAB, () -> new ItemStack(ThuwumcraftItems.THUWUMIUM_CAP));

	private final CapRechargeType rechargeType;
	private final Identifier id;
	private final int color;
	private final Supplier<ItemStack> stack;
	private String translationKey;

	WandCapMaterials(Identifier id, CapRechargeType rechargeType, int color, Supplier<ItemStack> stack)
	{
		this.id = id;
		this.rechargeType = rechargeType;
		this.color = color;
		this.stack = stack;
		ThuwumcraftRegistry.WAND_CAP.register(id, stack.get().getItem(), this);
	}

	@Override
	public CapRechargeType getRechargeType()
	{
		return this.rechargeType;
	}

	@Override
	public Identifier getId()
	{
		return id;
	}

	@Override
	public int getColor()
	{
		return color;
	}

	@Override
	public String getTranslationKey()
	{
		if(translationKey == null)
		{
			translationKey = Util.createTranslationKey("wand_cap", ThuwumcraftRegistry.WAND_CAP.getId(this));
		}
		return translationKey;
	}

	@Override
	public TranslatableText getName()
	{
		return new TranslatableText(getTranslationKey());
	}

	@Override
	public ItemStack getItemStack()
	{
		return stack.get().copy();
	}
}
