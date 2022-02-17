package net.watersfall.thuwumcraft.block;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.api.spell.SpellType;
import net.watersfall.thuwumcraft.block.entity.FocalManipulatorBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FocalManipulatorBlock extends BlockWithEntity
{
	public FocalManipulatorBlock(Settings settings)
	{
		super(settings);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new FocalManipulatorBlockEntity(pos, state);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if(!world.isClient)
		{
			player.openHandledScreen(new ExtendedScreenHandlerFactory()
			{
				@Override
				public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf)
				{
					if(world.getBlockEntity(pos) instanceof FocalManipulatorBlockEntity entity)
					{
						int size = 0;
						List<SpellType<?>> spells = new ArrayList<>();
						for(SpellType<?> type : ThuwumcraftRegistry.SPELL.values())
						{
							if(type.create().isAvailable(player, entity, player.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).get()))
							{
								spells.add(type);
								size++;
							}
						}
						buf.writeInt(size);
						spells.forEach(spell -> buf.writeIdentifier(ThuwumcraftRegistry.SPELL.getId(spell)));
					}
				}

				@Override
				public Text getDisplayName()
				{
					return FocalManipulatorBlock.this.getName();
				}

				@Nullable
				@Override
				public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
				{
					if(world.getBlockEntity(pos) instanceof FocalManipulatorBlockEntity entity)
					{
						return entity.createMenu(syncId, inv, player);
					}
					return null;
				}
			});
		}
		return ActionResult.success(world.isClient);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return Blocks.END_PORTAL_FRAME.getOutlineShape(Blocks.END_PORTAL_FRAME.getDefaultState(), world, pos, context);
	}
}
