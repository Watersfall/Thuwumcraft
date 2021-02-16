package net.watersfall.alchemy.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.render.RenderLayer;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.block.AlchemyBlocks;
import net.watersfall.alchemy.block.entity.AlchemyBlockEntities;
import net.watersfall.alchemy.client.gui.AlchemicalFurnaceScreen;
import net.watersfall.alchemy.client.gui.ApothecaryGuideScreen;
import net.watersfall.alchemy.client.renderer.BrewingCauldronEntityRenderer;
import net.watersfall.alchemy.client.renderer.CrucibleEntityRenderer;
import net.watersfall.alchemy.client.renderer.PedestalEntityRenderer;
import net.watersfall.alchemy.multiblock.MultiBlockRegistry;
import net.watersfall.alchemy.util.StatusEffectHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

@Environment(EnvType.CLIENT)
public class AlchemyModClient implements ClientModInitializer
{
	private static void registerEvents()
	{
		ItemTooltipCallback.EVENT.register(((stack, context, tooltip) -> {
			if(stack.getTag() != null && !stack.getTag().isEmpty())
			{
				CompoundTag tag = stack.getTag();
				if(tag.contains(StatusEffectHelper.EFFECTS_LIST))
				{
					ListTag list = tag.getList(StatusEffectHelper.EFFECTS_LIST, NbtType.COMPOUND);
					if(list.size() > 0)
					{
						tooltip.add(StatusEffectHelper.APPLIED_EFFECTS);
						list.forEach((effect) -> {
							tooltip.add(StatusEffectHelper.getEffectText(StatusEffectHelper.getEffectFromTag((CompoundTag) effect), true));
						});
					}
					else
					{
						tooltip.add(StatusEffectHelper.NO_EFFECT);
					}
				}
			}
		}));
	}

	@Override
	public void onInitializeClient()
	{
		ColorProviderRegistry.BLOCK.register(
				(state, view, pos, tintIndex) -> BiomeColors.getWaterColor(view, pos),
				AlchemyBlocks.BREWING_CAULDRON_BLOCK
		);
		BlockEntityRendererRegistry.INSTANCE.register(AlchemyBlockEntities.BREWING_CAULDRON_ENTITY, BrewingCauldronEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(AlchemyBlockEntities.PEDESTAL_ENTITY, PedestalEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(AlchemyBlockEntities.CRUCIBLE_ENTITY, CrucibleEntityRenderer::new);
		ScreenRegistry.register(AlchemyMod.APOTHECARY_GUIDE_HANDLER, ApothecaryGuideScreen::new);
		ScreenRegistry.register(AlchemyMod.ALCHEMICAL_FURNACE_HANDLER, AlchemicalFurnaceScreen::new);
		BlockRenderLayerMap.INSTANCE.putBlock(AlchemyBlocks.CHILD_BLOCK, RenderLayer.getCutout());
		ClientTickEvents.END_CLIENT_TICK.register(client -> MultiBlockRegistry.CLIENT.tick());
		registerEvents();
	}
}
