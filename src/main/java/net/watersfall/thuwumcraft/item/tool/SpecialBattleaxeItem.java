package net.watersfall.thuwumcraft.item.tool;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.watersfall.thuwumcraft.item.ThuwumcraftItems;

public class SpecialBattleaxeItem extends OpenAxeItem
{
	public SpecialBattleaxeItem()
	{
		super(AlchemyToolMaterials.MAGIC, 9, -3.2F, new FabricItemSettings().maxCount(1).group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP));
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		if(target.isDead() && target.getType() == EntityType.WITHER_SKELETON)
		{
			target.dropItem(Items.WITHER_SKELETON_SKULL);
		}
		return super.postHit(stack, target, attacker);
	}
}
