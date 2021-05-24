package net.watersfall.thuwumcraft.item.tool;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.watersfall.thuwumcraft.item.ThuwumcraftItems;

public class SpecialSwordItem extends SwordItem
{
	public SpecialSwordItem()
	{
		super(AlchemyToolMaterials.MAGIC, 0, 0, new FabricItemSettings().maxCount(1).group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP));
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		return super.postHit(stack, target, attacker);
	}
}
