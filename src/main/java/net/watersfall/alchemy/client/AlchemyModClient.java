package net.watersfall.alchemy.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.abilities.item.RunedShieldAbilityItem;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.aspect.AspectInventory;
import net.watersfall.alchemy.api.aspect.AspectStack;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.api.client.item.MultiTooltipComponent;
import net.watersfall.alchemy.api.multiblock.MultiBlockRegistry;
import net.watersfall.alchemy.block.AlchemyBlocks;
import net.watersfall.alchemy.block.entity.AlchemyBlockEntities;
import net.watersfall.alchemy.client.gui.AlchemicalFurnaceScreen;
import net.watersfall.alchemy.client.gui.ApothecaryGuideScreen;
import net.watersfall.alchemy.client.gui.item.AspectTooltipComponent;
import net.watersfall.alchemy.client.gui.item.GlassPhialTooltipComponent;
import net.watersfall.alchemy.client.item.AspectTooltipData;
import net.watersfall.alchemy.client.item.GlassPhialTooltipData;
import net.watersfall.alchemy.client.renderer.*;
import net.watersfall.alchemy.item.AlchemyItems;
import net.watersfall.alchemy.item.GlassPhialItem;
import net.watersfall.alchemy.recipe.AlchemyRecipes;
import net.watersfall.alchemy.recipe.AspectIngredient;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
				else if(stack.getItem() == Items.NETHERITE_CHESTPLATE)
				{
					AbilityProvider<ItemStack> provider = (AbilityProvider<ItemStack>)stack;
					Optional<Ability<ItemStack>> abilityOptional = provider.getAbility(AlchemyMod.getId("runed_shield_ability"));
					if(abilityOptional.isPresent())
					{
						RunedShieldAbilityItem ability = (RunedShieldAbilityItem) abilityOptional.get();
						tooltip.add(new LiteralText("Amount: " + ability.getShieldAmount() + " Max: " + ability.getMaxAmount()));
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
		BlockEntityRendererRegistry.INSTANCE.register(AlchemyBlockEntities.JAR_ENTITY, JarEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(AlchemyBlockEntities.PHIAL_SHELF_ENTITY, PhialShelfEntityRenderer::new);
		ScreenRegistry.register(AlchemyMod.APOTHECARY_GUIDE_HANDLER, ApothecaryGuideScreen::new);
		ScreenRegistry.register(AlchemyMod.ALCHEMICAL_FURNACE_HANDLER, AlchemicalFurnaceScreen::new);
		BlockRenderLayerMap.INSTANCE.putBlock(AlchemyBlocks.CHILD_BLOCK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(AlchemyBlocks.JAR_BLOCK, RenderLayer.getTranslucent());
		ClientTickEvents.END_CLIENT_TICK.register(client -> MultiBlockRegistry.CLIENT_TICKER.tick());
		MultiTooltipComponent.REGISTRY.register(AlchemyItems.EMPTY_PHIAL_ITEM, (stack) -> {
			return new GlassPhialTooltipComponent(new GlassPhialTooltipData(new AspectStack(Aspects.AIR, 64)));
		});
		MultiTooltipComponent.REGISTRY.register(AlchemyItems.EMPTY_PHIAL_ITEM, (stack) -> {
			return new GlassPhialTooltipComponent(new GlassPhialTooltipData(new AspectStack(Aspects.FIRE, 64)));
		});
		for(Item item : Registry.ITEM)
		{
			MultiTooltipComponent.REGISTRY.register(item, (stack) -> {
				RecipeManager manager = MinecraftClient.getInstance().world.getRecipeManager();
				Optional<AspectIngredient> ingredientOptional = manager.getFirstMatch(AlchemyRecipes.ASPECT_INGREDIENTS, new AspectInventory.Impl(stack), MinecraftClient.getInstance().world);
				if(ingredientOptional.isPresent())
				{
					return new AspectTooltipComponent(new AspectTooltipData(ingredientOptional.get().getAspects()));
				}
				return null;
			});
		}
		registerEvents();
		ClientPlayNetworking.registerGlobalReceiver(AlchemyMod.getId("abilities_packet"), ((client, handler, buf, responseSender) -> {
			Entity entity = handler.getWorld().getEntityById(buf.readInt());
			if(entity instanceof AbilityProvider)
			{
				AbilityProvider<Entity> provider = (AbilityProvider<Entity>)entity;
				provider.fromPacket(buf);
			}
		}));

		ClientPlayNetworking.registerGlobalReceiver(AlchemyMod.getId("abilities_packet_player"), ((client, handler, buf, responseSender) -> {
			PacketByteBuf buf2 = new PacketByteBuf(buf.copy());
			client.execute(() -> {
				buf2.readInt();
				AbilityProvider<Entity> provider = (AbilityProvider<Entity>)MinecraftClient.getInstance().player;
				provider.fromPacket(buf2);
			});
		}));
	}
}
