package net.watersfall.thuwumcraft.item.tool;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.entity.WindEntity;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;

public class SpecialSwordItem extends SwordItem
{
	public SpecialSwordItem()
	{
		super(AlchemyToolMaterials.MAGIC, 0, 0, new FabricItemSettings().maxCount(1).group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP));
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		target.takeKnockback(0.5D, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
		return super.postHit(stack, target, attacker);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		user.setCurrentHand(hand);
		return TypedActionResult.consume(user.getStackInHand(hand));
	}

	@Override
	public int getMaxUseTime(ItemStack stack)
	{
		return 20;
	}

	@Override
	public UseAction getUseAction(ItemStack stack)
	{
		return UseAction.BOW;
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user)
	{
		if(user instanceof PlayerEntity player)
		{
			player.getItemCooldownManager().set(this, 1);
		}
		if(!world.isClient)
		{
			WindEntity entity = new WindEntity(world);
			entity.setOwner(user);
			Vec3d position = user.getEyePos().add(user.getRotationVector());
			entity.setPosition(user.getEyePos().add(user.getRotationVector()));
			entity.setVelocity(user.getRotationVector().multiply(0.5));
			entity.refreshPositionAndAngles(position.x, position.y - 0.25F, position.z, 0, user.getPitch());
			world.spawnEntity(entity);
		}
		return super.finishUsing(stack, world, user);
	}

	@Override
	public void onCraft(ItemStack stack, World world, PlayerEntity player)
	{
		if(stack.getNbt() != null && stack.getNbt().contains("RepairCost"))
		{
			stack.getNbt().putInt("RepairCost", 0);
		}
	}
}
