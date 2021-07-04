package net.watersfall.thuwumcraft.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Rarity;
import net.watersfall.thuwumcraft.api.sound.AlchemySounds;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlocks;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;

public class MagicDustItem extends Item
{
	public MagicDustItem()
	{
		super(new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP).rarity(Rarity.UNCOMMON));
	}

	//Todo: Behavior map like vanilla cauldron
	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		BlockState state = context.getWorld().getBlockState(context.getBlockPos());
		if(state.getBlock() == Blocks.CAULDRON)
		{
			if(!context.getWorld().isClient)
			{
				context.getWorld().playSound(null, context.getBlockPos(), AlchemySounds.USE_DUST_SOUND, SoundCategory.BLOCKS, 0.5F, 0.8F + ((float)Math.random() * 0.4F));
				context.getWorld().setBlockState(context.getBlockPos(), ThuwumcraftBlocks.CRUCIBLE_BLOCK.getDefaultState());
			}
			return ActionResult.success(context.getWorld().isClient);
		}
		return ActionResult.PASS;
	}
}
