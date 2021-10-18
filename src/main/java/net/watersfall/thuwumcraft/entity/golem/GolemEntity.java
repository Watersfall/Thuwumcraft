package net.watersfall.thuwumcraft.entity.golem;

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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.entity.golem.goal.MoveGoal;
import net.watersfall.thuwumcraft.item.golem.GolemSealItem;
import net.watersfall.thuwumcraft.registry.ThuwumcraftEntities;

public class GolemEntity extends PathAwareEntity
{
	private static final TrackedData<ItemStack> SEAL = DataTracker.registerData(GolemEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	private BlockPos home = BlockPos.ORIGIN;
	private Direction side;

	public GolemEntity(World world, BlockPos home, Direction side)
	{
		this(ThuwumcraftEntities.GOLEM, world);
		this.home = home;
		this.side = side;
	}

	public GolemEntity(EntityType<GolemEntity> type, World world)
	{
		super(type, world);
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
		if(home != null)
		{
			this.goalSelector.add(30, new MoveGoal(this));
		}
	}

	@Override
	protected void initDataTracker()
	{
		super.initDataTracker();
		this.dataTracker.startTracking(SEAL, ItemStack.EMPTY);
	}

	public ItemStack getSeal()
	{
		return dataTracker.get(SEAL);
	}

	public BlockPos getHome()
	{
		return home;
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
		nbt.putLong("home_pos", home.asLong());
		nbt.putInt("side", side.getId());
		if(!getSeal().isEmpty())
		{
			nbt.put("golem_seal", getSeal().writeNbt(new NbtCompound()));
		}
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
		home = BlockPos.fromLong(nbt.getLong("home_pos"));
		side = Direction.byId(nbt.getInt("side"));
		if(nbt.contains("golem_seal"))
		{
			setSeal(ItemStack.fromNbt(nbt.getCompound("golem_seal")));
		}
	}

	@Override
	public boolean canEquip(ItemStack stack)
	{
		return super.canEquip(stack);
	}

	@Override
	public boolean cannotDespawn()
	{
		return true;
	}

	public Direction getSide()
	{
		return side;
	}
}
