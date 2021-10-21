package net.watersfall.thuwumcraft.entity.golem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.client.util.RenderHelper;
import net.watersfall.thuwumcraft.entity.golem.goal.MoveGoal;
import net.watersfall.thuwumcraft.item.golem.GolemBellItem;
import net.watersfall.thuwumcraft.item.golem.GolemSealItem;
import net.watersfall.thuwumcraft.registry.ThuwumcraftEntities;

public class GolemEntity extends PathAwareEntity
{
	private static final TrackedData<ItemStack> SEAL = DataTracker.registerData(GolemEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	private static final TrackedData<ItemStack> WHITELIST = DataTracker.registerData(GolemEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	private static final TrackedData<Integer> COLOR = DataTracker.registerData(GolemEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Direction> SIDE = DataTracker.registerData(GolemEntity.class, TrackedDataHandlerRegistry.FACING);
	private static final TrackedData<BlockPos> HOME = DataTracker.registerData(GolemEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);

	public GolemEntity(World world, BlockPos home, Direction side)
	{
		this(ThuwumcraftEntities.GOLEM, world);
		setHome(home);
		setSide(side);
	}

	public GolemEntity(EntityType<GolemEntity> type, World world)
	{
		super(type, world);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public boolean isGlowing()
	{
		return (RenderHelper.isHoldingBell(MinecraftClient.getInstance().player) && RenderHelper.shouldRenderGolemOutline(MinecraftClient.getInstance().player, this));
	}

	@Override
	public int getTeamColorValue()
	{
		return getColor() == null ? super.getTeamColorValue() : getColor().getFireworkColor();
	}

	@Override
	protected void initGoals()
	{
		this.clearGoalsAndTasks();
		super.initGoals();
		if(!getSeal().isEmpty() && getSeal().getItem() instanceof GolemSealItem seal)
		{
			for(int i = 0; i < seal.getGoals().size(); i++)
			{
				this.goalSelector.add(i, seal.getGoals().get(i).create(this));
			}
			this.goalSelector.add(31, new LookAtEntityGoal(this, PlayerEntity.class, 4));
			this.goalSelector.add(32, new LookAroundGoal(this));
		}
		if(getHome() != null)
		{
			this.goalSelector.add(30, new MoveGoal(this));
		}
	}

	@Override
	protected void initDataTracker()
	{
		super.initDataTracker();
		this.dataTracker.startTracking(SEAL, ItemStack.EMPTY);
		this.dataTracker.startTracking(WHITELIST, ItemStack.EMPTY);
		this.dataTracker.startTracking(COLOR, DyeColor.WHITE.getId());
		this.dataTracker.startTracking(SIDE, Direction.UP);
		this.dataTracker.startTracking(HOME, BlockPos.ORIGIN);
	}

	public ItemStack getSeal()
	{
		return dataTracker.get(SEAL);
	}

	public BlockPos getHome()
	{
		return dataTracker.get(HOME);
	}

	public void setSeal(ItemStack seal)
	{
		dataTracker.set(SEAL, seal);
		this.initGoals();
	}

	@Override
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions)
	{
		return getHeight() * 0.9F;
	}

	@Override
	protected void mobTick()
	{
		super.mobTick();
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt)
	{
		super.writeCustomDataToNbt(nbt);
		nbt.putLong("home_pos", getHome().asLong());
		nbt.putInt("side", getSide().getId());
		if(!getSeal().isEmpty())
		{
			nbt.put("golem_seal", getSeal().writeNbt(new NbtCompound()));
		}
		if(!getWhitelist().isEmpty())
		{
			nbt.put("whitelist", getWhitelist().writeNbt(new NbtCompound()));
		}
		nbt.putInt("color", getColor().getId());
	}

	@Override
	public void pushAwayFrom(Entity entity)
	{
		super.pushAwayFrom(entity);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt)
	{
		super.readCustomDataFromNbt(nbt);
		setHome(BlockPos.fromLong(nbt.getLong("home_pos")));
		setSide(Direction.byId(nbt.getInt("side")));
		if(nbt.contains("golem_seal"))
		{
			setSeal(ItemStack.fromNbt(nbt.getCompound("golem_seal")));
		}
		if(nbt.contains("whitelist"))
		{
			setWhiteList(ItemStack.fromNbt(nbt.getCompound("whitelist")));
		}
		setColor(DyeColor.byId(nbt.getInt("color")));
	}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getStackInHand(hand);
		if(!(stack.getItem() instanceof GolemSealItem))
		{
			if(stack.isEmpty())
			{
				if(!world.isClient)
				{
					setWhiteList(ItemStack.EMPTY);
				}
				return ActionResult.success(world.isClient);
			}
			else if(!(stack.getItem() instanceof GolemBellItem))
			{
				if(stack.getItem() instanceof DyeItem dye)
				{
					if(!world.isClient)
					{
						setColor(dye.getColor());
					}
					return ActionResult.success(world.isClient);
				}
				if(!world.isClient)
				{
					setWhiteList(stack.copy());
				}
				return ActionResult.success(world.isClient);
			}
		}
		return super.interactMob(player, hand);
	}

	@Override
	public boolean cannotDespawn()
	{
		return true;
	}

	public Direction getSide()
	{
		return dataTracker.get(SIDE);
	}

	public DyeColor getColor()
	{
		return DyeColor.byId(dataTracker.get(COLOR));
	}

	public ItemStack getWhitelist()
	{
		return dataTracker.get(WHITELIST);
	}

	public void setSide(Direction direction)
	{
		dataTracker.set(SIDE, direction);
	}

	public void setWhiteList(ItemStack stack)
	{
		dataTracker.set(WHITELIST, stack);
	}

	public void setColor(DyeColor color)
	{
		dataTracker.set(COLOR, color.getId());
	}

	public void setHome(BlockPos pos)
	{
		dataTracker.set(HOME, pos);
	}
}
