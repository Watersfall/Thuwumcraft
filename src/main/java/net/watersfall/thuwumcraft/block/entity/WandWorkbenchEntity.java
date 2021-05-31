package net.watersfall.thuwumcraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.watersfall.thuwumcraft.inventory.BasicInventory;
import net.watersfall.thuwumcraft.gui.WandWorkbenchHandler;
import org.jetbrains.annotations.Nullable;

public class WandWorkbenchEntity extends BlockEntity implements NamedScreenHandlerFactory, BasicInventory
{
	private final DefaultedList<ItemStack> list;
	private static final TranslatableText title = new TranslatableText("container.thuwumcraft.wand_workbench");

	public WandWorkbenchEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.WAND_WORKBENCH, pos, state);
		list = DefaultedList.ofSize(4, ItemStack.EMPTY);
	}

	@Override
	public Text getDisplayName()
	{
		return title;
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
	{
		return new WandWorkbenchHandler(syncId, inv, this);
	}

	@Override
	public DefaultedList<ItemStack> getContents()
	{
		return this.list;
	}
}
