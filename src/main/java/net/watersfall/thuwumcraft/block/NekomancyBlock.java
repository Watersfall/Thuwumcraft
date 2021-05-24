package net.watersfall.thuwumcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.inventory.NekomancerTableInventory;
import net.watersfall.thuwumcraft.screen.NekomancyTableHandler;
import org.jetbrains.annotations.Nullable;

public class NekomancyBlock extends Block
{
	public static final TranslatableText SCREEN_NAME = new TranslatableText("screen.waters_alchemy_mod.nekomancy_table");

	public NekomancyBlock(Settings settings)
	{
		super(settings);
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
				return new NekomancyTableHandler(syncId, inv, new NekomancerTableInventory());
			}
		};
	}
}
