package net.watersfall.thuwumcraft.block;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.block.entity.AspectCraftingEntity;
import net.watersfall.thuwumcraft.gui.AspectCraftingHandler;
import org.jetbrains.annotations.Nullable;

public class AspectCraftingBlock extends Block implements BlockEntityProvider
{
	public static final TranslatableText SCREEN_NAME = new TranslatableText("screen.thuwumcraft.crystal_crafting_table");

	public AspectCraftingBlock(Settings settings)
	{
		super(settings);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new AspectCraftingEntity(pos, state);
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
		BlockEntity test = world.getBlockEntity(pos);
		if(test instanceof AspectCraftingEntity entity)
		{
			return new ExtendedScreenHandlerFactory()
			{
				@Override
				public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf buf)
				{
					buf.writeBlockPos(pos);
					buf.writeBlockPos(pos);
				}

				@Override
				public Text getDisplayName()
				{
					return SCREEN_NAME;
				}

				@Override
				public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
				{
					return new AspectCraftingHandler(syncId, inv, entity, ScreenHandlerContext.create(world, pos), pos);
				}
			};
		}
		return super.createScreenHandlerFactory(state, world, pos);
	}
}
