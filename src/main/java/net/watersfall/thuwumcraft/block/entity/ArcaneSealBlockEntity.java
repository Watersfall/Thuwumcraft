package net.watersfall.thuwumcraft.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.inventory.BasicInventory;
import net.watersfall.thuwumcraft.item.ArcaneRuneItem;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArcaneSealBlockEntity extends BlockEntity implements BasicInventory, BlockEntityClientSerializable
{
	public static final Map<Triple<Item, Item, Item>, SealAction> actions = new HashMap<>();

	private PlayerEntity owner;
	private UUID ownerId;
	private DefaultedList<ItemStack> inventory;
	private int[] colors;
	private SealAction cachedAction = null;

	public ArcaneSealBlockEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.ARCANE_SEAL, pos, state);
		this.owner = null;
		this.ownerId = null;
		this.inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
		colors = new int[3];
	}

	public ArcaneSealBlockEntity(BlockPos  pos, BlockState state, PlayerEntity owner)
	{
		this(pos, state);
		this.owner = owner;
		this.ownerId = owner.getUuid();
	}

	public boolean canAddRune(ItemStack rune)
	{
		for(int i = 0; i < this.size(); i++)
		{
			if(this.getStack(i).isEmpty())
			{
				return true;
			}
		}
		return false;
	}

	public void addRune(ItemStack rune)
	{
		for(int i = 0; i < this.size(); i++)
		{
			if(this.getStack(i).isEmpty())
			{
				this.setStack(i, rune);
				this.cachedAction = null;
				this.colors[i] = ((ArcaneRuneItem)rune.getItem()).getAspect().getColor();
				return;
			}
		}
	}

	@Override
	public void clear()
	{
		BasicInventory.super.clear();
		this.cachedAction = null;
	}

	public void setOwner(PlayerEntity player)
	{
		this.owner = player;
		this.ownerId = player.getUuid();
	}

	public PlayerEntity getOwner()
	{
		return this.owner;
	}

	public UUID getOwnerId()
	{
		return ownerId;
	}

	public boolean isOwner(PlayerEntity player)
	{
		return player.getUuid().equals(ownerId);
	}

	@Override
	public DefaultedList<ItemStack> getContents()
	{
		return inventory;
	}

	@Override
	public void readNbt(NbtCompound nbt)
	{
		super.readNbt(nbt);
		if(nbt.contains("owner"))
		{
			this.ownerId = nbt.getUuid("owner");
		}
		Inventories.readNbt(nbt, inventory);
		colors();
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt)
	{
		super.writeNbt(nbt);
		if(ownerId != null)
		{
			nbt.putUuid("owner", ownerId);
		}
		Inventories.writeNbt(nbt, inventory);
		return nbt;
	}

	public int getColor(int index)
	{
		return colors[index];
	}

	public static void tick(World world, BlockPos pos, BlockState state, ArcaneSealBlockEntity entity)
	{
		if(entity.cachedAction == null)
		{
			entity.cachedAction = actions.get(triple(entity.getStack(0).getItem(), entity.getStack(1).getItem(), entity.getStack(2).getItem()));
			if(entity.cachedAction == null)
			{
				entity.cachedAction = SealAction.EMPTY_ACTION;
			}
		}
		entity.cachedAction.run(entity);
	}

	@Override
	public void fromClientTag(NbtCompound tag)
	{
		this.cachedAction = null;
		this.inventory.clear();
		Inventories.readNbt(tag, this.inventory);
		colors();
	}

	@Override
	public NbtCompound toClientTag(NbtCompound tag)
	{
		Inventories.writeNbt(tag, this.inventory);
		return tag;
	}

	private void colors()
	{
		for(int i = 0; i < inventory.size(); i++)
		{
			if(!inventory.get(i).isEmpty())
			{
				colors[i] = ((ArcaneRuneItem)inventory.get(i).getItem()).getAspect().getColor();
			}
			else
			{
				colors[i] = 0;
			}
		}
	}

	static
	{
		actions.put(triple(ThuwumcraftItems.AIR_RUNE, ThuwumcraftItems.DISORDER_RUNE), (blockEntity) -> {
			Vec3d entityVec = new Vec3d(blockEntity.getPos().getX(), blockEntity.getPos().getY(), blockEntity.getPos().getZ()).add(0.5, 0.5, 0.5);
			blockEntity.getWorld().getEntitiesByClass(
					ItemEntity.class,
					new Box(blockEntity.getPos().add(-9, -9, -9), blockEntity.getPos().add(9, 9, 9)),
					(itemEntity -> itemEntity.getPos().distanceTo(entityVec) < 9))
					.forEach(itemEntity -> {
						Vec3d itemVec = itemEntity.getPos();
						Vec3d newVec = itemVec.subtract(entityVec).normalize().multiply(0.05);
						itemEntity.move(MovementType.SELF, newVec);
					});
		});
		actions.put(triple(ThuwumcraftItems.AIR_RUNE, ThuwumcraftItems.ORDER_RUNE), (blockEntity) -> {
			Vec3d entityVec = new Vec3d(blockEntity.getPos().getX(), blockEntity.getPos().getY(), blockEntity.getPos().getZ()).add(0.5, 0.5, 0.5);
			blockEntity.getWorld().getEntitiesByClass(
					ItemEntity.class,
					new Box(blockEntity.getPos().add(-9, -9, -9), blockEntity.getPos().add(9, 9, 9)),
					(itemEntity -> itemEntity.getPos().distanceTo(entityVec) < 9))
					.forEach(itemEntity -> {
						Vec3d itemVec = itemEntity.getPos();
						Vec3d newVec = entityVec.subtract(itemVec).normalize().multiply(0.05);
						itemEntity.move(MovementType.SELF, newVec);
					});
		});
	}

	private static Triple<Item, Item, Item> triple(Item item)
	{
		return new ImmutableTriple<>(item, Items.AIR, Items.AIR);
	}

	private static Triple<Item, Item, Item> triple(Item item, Item item2)
	{
		return new ImmutableTriple<>(item, item2, Items.AIR);
	}

	private static Triple<Item, Item, Item> triple(Item item, Item item2, Item item3)
	{
		return new ImmutableTriple<>(item, item2, item3);
	}

	@FunctionalInterface
	public interface SealAction
	{
		public static SealAction EMPTY_ACTION = (be) -> {};

		void run(ArcaneSealBlockEntity entity);
	}
}
