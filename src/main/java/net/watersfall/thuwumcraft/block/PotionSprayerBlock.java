package net.watersfall.thuwumcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.block.entity.PotionSprayerEntity;
import net.watersfall.thuwumcraft.gui.PotionSprayerHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PotionSprayerBlock extends Block implements BlockEntityProvider
{
	public static final BooleanProperty TRIGGERED = Properties.TRIGGERED;
	public static final TranslatableText SCREEN_NAME = new TranslatableText("screen.thuwumcraft.potion_sprayer");

	public PotionSprayerBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState(this.getDefaultState().with(TRIGGERED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(TRIGGERED);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random)
	{
		super.randomDisplayTick(state, world, pos, random);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		BlockEntity test = world.getBlockEntity(pos);
		if(test instanceof PotionSprayerEntity)
		{
			PotionSprayerEntity entity = (PotionSprayerEntity)test;
			ItemStack potion = entity.getPotionItem();
			if(!potion.isEmpty())
			{
				PotionEntity potionEntity = new PotionEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
				potionEntity.setItem(potion);
				potion.decrement(1);
				world.spawnEntity(potionEntity);
			}
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify)
	{
		super.neighborUpdate(state, world, pos, block, fromPos, notify);
		boolean isTriggered = state.get(TRIGGERED);
		if(world.isReceivingRedstonePower(pos) && !isTriggered)
		{
			world.setBlockState(pos, state.with(TRIGGERED, true));
			world.getBlockTickScheduler().schedule(pos, this, 4);
		}
		else if(isTriggered)
		{
			world.setBlockState(pos, state.with(TRIGGERED, false), 4);
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new PotionSprayerEntity(pos, state);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if(!world.isClient)
		{
			player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
		}
		return ActionResult.success(world.isClient);
	}

	@Nullable
	@Override
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos)
	{
		return new NamedScreenHandlerFactory()
		{
			@Override
			public Text getDisplayName()
			{
				return SCREEN_NAME;
			}

			@Override
			public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
			{
				BlockEntity test = world.getBlockEntity(pos);
				if(test instanceof PotionSprayerEntity)
				{
					PotionSprayerEntity entity = (PotionSprayerEntity)test;
					return new PotionSprayerHandler(syncId, inv, entity, entity.getPotionInventory());
				}
				return null;
			}
		};
	}
}
