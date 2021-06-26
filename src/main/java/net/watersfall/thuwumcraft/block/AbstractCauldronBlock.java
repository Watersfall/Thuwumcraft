package net.watersfall.thuwumcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.watersfall.thuwumcraft.api.fluid.ColoredWaterContainer;
import net.watersfall.thuwumcraft.api.fluid.WaterContainer;
import net.watersfall.thuwumcraft.api.sound.AlchemySounds;
import net.watersfall.thuwumcraft.block.entity.AbstractCauldronEntity;
import net.watersfall.thuwumcraft.util.InventoryHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class AbstractCauldronBlock extends Block
{
	protected static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE), BooleanBiFunction.ONLY_FIRST);
	public static final BooleanProperty POWERED = Properties.POWERED;

	public AbstractCauldronBlock(Settings settings)
	{
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(POWERED, false));
	}

	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
	{
		BlockEntity test = world.getBlockEntity(pos);
		if(test instanceof AbstractCauldronEntity cauldron)
		{
			if(entity instanceof LivingEntity living)
			{
				if(!world.isClient)
				{
					if(entity.isOnFire() && cauldron.getWaterLevel() > 333)
					{
						entity.extinguish();
						cauldron.setWaterLevel((short) (cauldron.getWaterLevel() - 333));
						cauldron.sync();
					}
					if(cauldron.getWaterLevel() > 0)
					{
						if(this.isPowered(state))
						{
							if(living.age % 10 == 0)
							{
								living.damage(DamageSource.IN_FIRE, 1);
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(POWERED);
	}

	@Override
	public boolean hasRandomTicks(BlockState state)
	{
		return true;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if(!world.isClient)
		{
			if(!this.isPowered(state))
			{
				BlockState below = world.getBlockState(pos.down());
				if(below.isIn(BlockTags.FIRE) || below.isIn(BlockTags.CAMPFIRES))
				{
					this.setPowered(world, pos, state, true);
				}
			}
			else
			{
				BlockState below = world.getBlockState(pos.down());
				if(!(below.isIn(BlockTags.FIRE) || below.isIn(BlockTags.CAMPFIRES)))
				{
					this.setPowered(world, pos, state, false);
				}
			}
		}
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random)
	{
		if(this.isPowered(state))
		{
			BlockEntity test = world.getBlockEntity(pos);
			if(test instanceof WaterContainer container)
			{
				if(container.getWaterLevel() > 0)
				{
					double x = pos.getX() + 0.25 + random.nextDouble() * 0.5;
					double y = pos.getY() + container.getMaxDisplayWaterLevel() / 1000F * 0.5625F + 0.25F;;
					double z = pos.getZ() + 0.25 + random.nextDouble() * 0.5;
					Particle particle =  MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0, 0, 0);
					if(particle != null)
					{
						Vec3d color;
						if(container instanceof ColoredWaterContainer)
						{
							color = Vec3d.unpackRgb(((ColoredWaterContainer) container).getColor());
						}
						else
						{
							color = Vec3d.unpackRgb(world.getColor(pos, BiomeColors.WATER_COLOR));
						}
						particle.setColor((float)color.getX(), (float)color.getY(), (float)color.getZ());
						if(random.nextDouble() > 0.5)
						{
							world.playSound(pos.getX(),
									pos.getY(),
									pos.getZ(),
									AlchemySounds.BUBBLE_SOUND,
									SoundCategory.BLOCKS,
									0.25F,
									0.9F + (float)(Math.random() * 0.2F),
									false);
						}
					}
				}
			}
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		ItemStack itemStack = player.getStackInHand(hand);
		AbstractCauldronEntity entity = (AbstractCauldronEntity)world.getBlockEntity(pos);
		assert entity != null;
		if(itemStack.isEmpty())
		{
			if(player.isSneaking() && entity.getWaterLevel() > 0)
			{
				if(!world.isClient)
				{
					entity.clear();
					entity.sync();
					world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				return ActionResult.success(world.isClient);
			}
			return ActionResult.PASS;
		}
		Item item = itemStack.getItem();
		if(item == Items.WATER_BUCKET)
		{
			if(!world.isClient)
			{
				if(!player.getAbilities().creativeMode)
				{
					player.setStackInHand(hand, new ItemStack(Items.BUCKET));
				}
				entity.clear();
				entity.setWaterLevel((short) 1000);
				entity.sync();
				world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
			return ActionResult.success(world.isClient);
		}
		else if(item == Items.BUCKET)
		{
			if(!world.isClient)
			{
				InventoryHelper.useItem(itemStack, player, hand, 1, Items.WATER_BUCKET.getDefaultStack());
				entity.setWaterLevel((short) 0);
				entity.clear();
				entity.sync();
				world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
			return ActionResult.success(world.isClient);
		}
		return ActionResult.PASS;
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return OUTLINE_SHAPE;
	}

	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos)
	{
		return RAY_TRACE_SHAPE;
	}

	public boolean isPowered(BlockState state)
	{
		return state.get(POWERED);
	}

	public void setPowered(World world, BlockPos pos, BlockState state, boolean powered)
	{
		world.setBlockState(pos, state.with(POWERED, powered));
		world.updateComparators(pos, this);
	}

	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type)
	{
		return false;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos)
	{
		if(direction == Direction.DOWN)
		{
			if(neighborState.isIn(BlockTags.FIRE) || neighborState.isIn(BlockTags.CAMPFIRES))
			{
				return state.with(POWERED, true);
			}
			else
			{
				return state.with(POWERED, false);
			}
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		BlockState below = ctx.getWorld().getBlockState(ctx.getBlockPos().down());
		if(below.isIn(BlockTags.FIRE) || below.isIn(BlockTags.CAMPFIRES))
		{
			return super.getPlacementState(ctx).with(POWERED, true);
		}
		return super.getPlacementState(ctx);
	}
}
