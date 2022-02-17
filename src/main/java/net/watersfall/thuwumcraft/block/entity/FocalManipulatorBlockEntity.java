package net.watersfall.thuwumcraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.api.spell.SpellType;
import net.watersfall.thuwumcraft.gui.FocalManipulatorHandler;
import net.watersfall.thuwumcraft.inventory.FocalManipulatorInventory;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FocalManipulatorBlockEntity extends SyncableBlockEntity implements FocalManipulatorInventory, NamedScreenHandlerFactory
{
	private final DefaultedList<ItemStack> inventory;

	public FocalManipulatorBlockEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.FOCAL_MANIPULATOR, pos, state);
		inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
	}

	@Override
	public NbtCompound toClientTag(NbtCompound nbt)
	{
		Inventories.writeNbt(nbt, inventory);
		return nbt;
	}

	@Override
	public void fromClientTag(NbtCompound nbt)
	{
		inventory.clear();
		Inventories.readNbt(nbt, inventory);
	}

	@Override
	public void readNbt(NbtCompound nbt)
	{
		super.readNbt(nbt);
		Inventories.readNbt(nbt, inventory);
	}

	@Override
	protected void writeNbt(NbtCompound nbt)
	{
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, inventory);
	}

	@Override
	public DefaultedList<ItemStack> getContents()
	{
		return inventory;
	}

	@Override
	public Text getDisplayName()
	{
		return new LiteralText("");
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
	{
		List<SpellType<?>> spells = new ArrayList<>();
		for(SpellType<?> type : ThuwumcraftRegistry.SPELL.values())
		{
			if(type.create().isAvailable((ServerPlayerEntity)player, this, player.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).get()))
			{
				spells.add(type);
			}
		}
		return new FocalManipulatorHandler(syncId, inv, this, spells);
	}
}
