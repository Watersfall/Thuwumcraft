package net.watersfall.thuwumcraft.block.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestLidAnimator;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.inventory.BasicInventory;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;
import net.watersfall.thuwumcraft.util.InventoryHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HungryChestBlockEntity extends BlockEntity implements ChestAnimationProgress, BasicInventory, NamedScreenHandlerFactory
{
	static void playSound(World world, BlockPos pos, BlockState state, SoundEvent soundEvent)
	{
		double x = pos.getX() + 0.5;
		double y = pos.getY() + 0.5;
		double z = pos.getZ() + 0.5;
		world.playSound(null, x, y, z, soundEvent, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
	}

	private final ChestLidAnimator lidAnimator;
	private final DefaultedList<ItemStack> contents;
	private final ViewerCountManager stateManager = new ViewerCountManager()
	{
		@Override
		protected void onContainerOpen(World world, BlockPos pos, BlockState state)
		{
			HungryChestBlockEntity.playSound(world, pos, state, SoundEvents.BLOCK_CHEST_OPEN);
		}

		@Override
		protected void onContainerClose(World world, BlockPos pos, BlockState state)
		{
			HungryChestBlockEntity.playSound(world, pos, state, SoundEvents.BLOCK_CHEST_CLOSE);
		}

		@Override
		protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount)
		{
			HungryChestBlockEntity.this.onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
		}

		@Override
		protected boolean isPlayerViewing(PlayerEntity player)
		{
			if(player.currentScreenHandler instanceof GenericContainerScreenHandler screen)
			{
				Inventory inventory = screen.getInventory();
				return inventory == HungryChestBlockEntity.this;
			}
			return false;
		}
	};

	public HungryChestBlockEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.HUNGRY_CHEST, pos, state);
		lidAnimator = new ChestLidAnimator();
		contents = DefaultedList.ofSize(27, ItemStack.EMPTY);
	}

	@Override
	public float getAnimationProgress(float tickDelta)
	{
		if(world == null)
		{
			tickDelta = MinecraftClient.getInstance().getTickDelta();
			float previous = MathHelper.sin((MinecraftClient.getInstance().world.getTime() - 1) / 25F) / 16F;
			float progress = MathHelper.sin(MinecraftClient.getInstance().world.getTime() / 25F) / 16F;
			return Math.max(0, MathHelper.lerp(tickDelta, previous, progress));
		}
		else
		{
			return lidAnimator.getProgress(tickDelta);
		}
	}

	@Override
	public boolean onSyncedBlockEvent(int type, int data)
	{
		if (type == 1)
		{
			this.lidAnimator.setOpen(data > 0);
			if(data > 0)
			{
				world.createAndScheduleBlockTick(pos, getCachedState().getBlock(), 2);
			}
			return true;
		}
		else
		{
			return super.onSyncedBlockEvent(type, data);
		}
	}

	@Override
	public void readNbt(NbtCompound nbt)
	{
		super.readNbt(nbt);
		Inventories.readNbt(nbt, contents);
	}

	@Override
	public void writeNbt(NbtCompound nbt)
	{
		Inventories.writeNbt(nbt, contents);
		super.writeNbt(nbt);
	}

	public static void serverTick(World world, BlockPos pos, BlockState state, HungryChestBlockEntity chest)
	{
		List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, new Box(pos, pos.add(1, 1, 1)), (entity) -> true);
		if(!items.isEmpty())
		{
			boolean itemEaten = false;
			int itemId = -1;
			int count = 0;
			for(ItemEntity entity : items)
			{
				ItemStack originalStack = entity.getStack().copy();
				ItemStack newStack = InventoryHelper.insert(chest, entity.getStack());
				if(!ItemStack.areEqual(originalStack, newStack))
				{
					count = originalStack.getCount() - newStack.getCount();
					entity.setStack(newStack);
					itemEaten = true;
					itemId = entity.getId();
					break;
				}
			}
			if(itemEaten)
			{
				world.addSyncedBlockEvent(pos, state.getBlock(), 1, 1);
				world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 1.0F, 1.0F);
				PacketByteBuf buf = PacketByteBufs.create();
				buf.writeInt(itemId);
				buf.writeLong(chest.getPos().asLong());
				buf.writeInt(count);
				for(ServerPlayerEntity player : PlayerLookup.tracking(chest))
				{
					ServerPlayNetworking.send(player, Thuwumcraft.getId("block_entity_pickup"), buf);
				}
			}
		}
	}

	public static void clientTick(World world, BlockPos pos, BlockState state, HungryChestBlockEntity chest)
	{
		chest.lidAnimator.step();
	}

	@Override
	public DefaultedList<ItemStack> getContents()
	{
		return contents;
	}

	@Override
	public Text getDisplayName()
	{
		return new TranslatableText("block.thuwumcraft.hungry_chest");
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
	{
		return GenericContainerScreenHandler.createGeneric9x3(syncId, inv, this);
	}

	public void close()
	{
		if(stateManager.getViewerCount() <= 0)
		{
			world.addSyncedBlockEvent(pos, getCachedState().getBlock(), 1, 0);
		}
	}

	protected void onInvOpenOrClose(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount)
	{
		Block block = state.getBlock();
		world.addSyncedBlockEvent(pos, block, 1, newViewerCount);
	}

	@Override
	public void onOpen(PlayerEntity player)
	{
		if (!this.removed && !player.isSpectator())
		{
			this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
		}
	}

	@Override
	public void onClose(PlayerEntity player)
	{
		if (!this.removed && !player.isSpectator())
		{
			this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
		}
	}
}
