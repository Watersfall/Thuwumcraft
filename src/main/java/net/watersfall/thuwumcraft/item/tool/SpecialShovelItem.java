package net.watersfall.thuwumcraft.item.tool;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.item.BeforeActions;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;

public class SpecialShovelItem extends ShovelItem implements BeforeActions
{
	public SpecialShovelItem()
	{
		super(AlchemyToolMaterials.MAGIC, 0, 0, new FabricItemSettings().maxCount(1).group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP));
	}

	@Override
	public boolean beforeMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner)
	{
		if(miner instanceof PlayerEntity player && !player.getAbilities().creativeMode && stack.getItem() == this && state.isIn(BlockTags.SHOVEL_MINEABLE))
		{
			Vec3d cameraPos = player.getCameraPosVec(1);
			Vec3d rotation = player.getRotationVec(1);
			Vec3d combined = cameraPos.add(rotation.x * 5, rotation.y * 5, rotation.z * 5);
			BlockHitResult result = world.raycast(new RaycastContext(cameraPos, combined, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
			Direction.Axis axis = result.getSide().getAxis();
			BlockPos.Mutable mutable = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
			for(int x = -1; x <= 1; x++)
			{
				for(int y = -1; y <= 1; y++)
				{
					for(int z = -1; z <= 1; z++)
					{
						mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
						BlockState test = world.getBlockState(mutable);
						if(test.isIn(BlockTags.SHOVEL_MINEABLE))
						{
							if(!(x == 0 && y == 0 && z == 0))
							{
								boolean broken = false;
								if(axis == Direction.Axis.X && x == 0)
								{
									broken = breakBlock(stack, world, state, mutable, player);
								}
								else if(axis == Direction.Axis.Y && y == 0)
								{
									broken = breakBlock(stack, world, state, mutable, player);
								}
								else if(axis == Direction.Axis.Z && z == 0)
								{
									broken = breakBlock(stack, world, state, mutable, player);
								}
								if(broken)
								{
									return false;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean breakBlock(ItemStack stack, World world, BlockState state, BlockPos pos, PlayerEntity player)
	{
		this.postMine(stack, world, state, pos, player);
		world.breakBlock(pos, true, player);
		return stack.getDamage() >= stack.getMaxDamage();
	}

	@Override
	public void onCraft(ItemStack stack, World world, PlayerEntity player)
	{
		if(stack.getTag() != null && stack.getTag().contains("RepairCost"))
		{
			stack.getTag().putInt("RepairCost", 0);
		}
	}
}
