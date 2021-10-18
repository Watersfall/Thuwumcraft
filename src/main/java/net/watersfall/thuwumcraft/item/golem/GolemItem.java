package net.watersfall.thuwumcraft.item.golem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import net.watersfall.thuwumcraft.entity.golem.GolemEntity;

public class GolemItem extends Item
{
	public GolemItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		if(!context.getWorld().isClient)
		{
			GolemEntity golem = new GolemEntity(context.getWorld(), context.getBlockPos(), context.getSide());
			Direction direction = context.getSide().getOpposite();
			golem.setPosition(context.getHitPos().subtract(direction.getOffsetX() / 2F, direction.getOffsetY() / 2F, direction.getOffsetZ() / 2F));
			context.getWorld().spawnEntity(golem);
			context.getStack().decrement(1);
		}
		return ActionResult.success(context.getWorld().isClient);
	}
}