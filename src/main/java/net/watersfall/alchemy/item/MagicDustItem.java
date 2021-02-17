package net.watersfall.alchemy.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Rarity;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.block.AlchemyBlocks;

public class MagicDustItem extends Item
{
	public MagicDustItem()
	{
		super(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP).rarity(Rarity.UNCOMMON));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		BlockState state = context.getWorld().getBlockState(context.getBlockPos());
		if(state.getBlock() == Blocks.CAULDRON)
		{
			if(!context.getWorld().isClient)
			{
				context.getWorld().playSound(null, context.getBlockPos(), AlchemyMod.MAGIC_SOUND, SoundCategory.BLOCKS, 0.5F, 0.8F + ((float)Math.random() * 0.4F));
				context.getWorld().setBlockState(context.getBlockPos(), AlchemyBlocks.CRUCIBLE_BLOCK.getDefaultState());
			}
			return ActionResult.success(context.getWorld().isClient);
		}
		return ActionResult.PASS;
	}
}
