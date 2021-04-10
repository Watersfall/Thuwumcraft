package net.watersfall.alchemy.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.abilities.chunk.VisAbilityImpl;
import net.watersfall.alchemy.api.abilities.chunk.VisAbility;
import net.watersfall.alchemy.client.accessor.ArmorFeatureRendererAccessor;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.alchemy.api.aspect.AspectInventory;
import net.watersfall.alchemy.api.client.gui.RecipeTabType;
import net.watersfall.alchemy.api.client.item.MultiTooltipComponent;
import net.watersfall.alchemy.api.multiblock.MultiBlockRegistry;
import net.watersfall.alchemy.api.research.Research;
import net.watersfall.alchemy.api.research.ResearchCategory;
import net.watersfall.alchemy.block.AlchemyBlocks;
import net.watersfall.alchemy.block.entity.AlchemyBlockEntities;
import net.watersfall.alchemy.client.gui.*;
import net.watersfall.alchemy.client.gui.element.ItemElement;
import net.watersfall.alchemy.client.gui.element.RecipeElement;
import net.watersfall.alchemy.client.gui.item.AspectTooltipComponent;
import net.watersfall.alchemy.client.item.AspectTooltipData;
import net.watersfall.alchemy.client.renderer.*;
import net.watersfall.alchemy.client.toast.ResearchToast;
import net.watersfall.alchemy.item.AlchemyArmorMaterials;
import net.watersfall.alchemy.recipe.AlchemyRecipes;
import net.watersfall.alchemy.recipe.AspectIngredient;
import net.watersfall.alchemy.recipe.CauldronItemRecipe;
import net.watersfall.alchemy.recipe.PedestalRecipe;
import net.watersfall.alchemy.screen.AlchemyScreenHandlers;
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
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Arrays;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class AlchemyModClient implements ClientModInitializer
{
	private static void registerEvents()
	{
		ItemTooltipCallback.EVENT.register(((stack, context, tooltip) -> {
			if(stack.getTag() != null && !stack.getTag().isEmpty())
			{
				NbtCompound tag = stack.getTag();
				if(tag.contains(StatusEffectHelper.EFFECTS_LIST))
				{
					NbtList list = tag.getList(StatusEffectHelper.EFFECTS_LIST, NbtType.COMPOUND);
					if(list.size() > 0)
					{
						tooltip.add(StatusEffectHelper.APPLIED_EFFECTS);
						list.forEach((effect) -> {
							tooltip.add(StatusEffectHelper.getEffectText(StatusEffectHelper.getEffectFromTag((NbtCompound) effect), true));
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

	private void preRegisterArmorTextures(Identifier name, boolean hasOverlay)
	{
		for (int layer = 1; layer <= 2; layer++)
		{
			preRegisterArmorTexture(name, layer, null);
			if (hasOverlay)
			{
				preRegisterArmorTexture(name, layer, "overlay");
			}
		}
	}

	private void preRegisterArmorTexture(Identifier name, int layer, @Nullable String extra)
	{
		String key = "textures/models/armor/" + name.getPath() + "_layer_" + layer + (extra == null ? "" : "_" + extra) + ".png";
		Identifier value = new Identifier(name.getNamespace(), "textures/models/armor/" + name.getPath() + "_layer_" + layer + (extra == null ? "" : "_" + extra) + ".png");
		ArmorFeatureRendererAccessor.getArmorTextureCache().put(key, value);
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
		BlockEntityRendererRegistry.INSTANCE.register(AlchemyBlockEntities.CRAFTING_HOPPER, CraftingHopperRenderer::new);
		ScreenRegistry.register(AlchemyScreenHandlers.APOTHECARY_GUIDE_HANDLER, ApothecaryGuideScreen::new);
		ScreenRegistry.register(AlchemyScreenHandlers.ALCHEMICAL_FURNACE_HANDLER, AlchemicalFurnaceScreen::new);
		ScreenRegistry.register(AlchemyScreenHandlers.RESEARCH_BOOK_HANDLER, ResearchBookScreen::new);
		ScreenRegistry.register(AlchemyScreenHandlers.NEKOMANCY_TABLE_HANDLER, NekomancyTableScreen::new);
		ScreenRegistry.register(AlchemyScreenHandlers.ASPECT_CRAFTING_HANDLER, AspectCraftingScreen::new);
		ScreenRegistry.register(AlchemyScreenHandlers.POTION_SPRAYER_HANDLER, PotionSprayerScreen::new);
		ScreenRegistry.register(AlchemyScreenHandlers.ESSENTIA_SMELTERY_HANDLER, EssentiaSmelterScreen::new);
		BlockRenderLayerMap.INSTANCE.putBlock(AlchemyBlocks.JAR_BLOCK, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
				AlchemyBlocks.CUSTOM_SPAWNER,
				AlchemyBlocks.CHILD_BLOCK);
		ClientTickEvents.END_CLIENT_TICK.register(client -> MultiBlockRegistry.CLIENT_TICKER.tick());
		ClientChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeInt(chunk.getPos().x);
			buf.writeInt(chunk.getPos().z);
			ClientPlayNetworking.send(AlchemyMod.getId("chunk_packet"), buf);
		});
		HudRenderCallback.EVENT.register((matrices, value) -> {
			World world = MinecraftClient.getInstance().world;
			PlayerEntity player = MinecraftClient.getInstance().player;
			AbilityProvider<Chunk> provider = AbilityProvider.getProvider(world.getChunk(player.getBlockPos()));
			provider.getAbility(VisAbility.ID, VisAbility.class).ifPresent((ability) -> {
				MinecraftClient.getInstance().textRenderer.draw(matrices, new LiteralText("" + ability.getVis()), 0, 0, -1);
			});
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
				AbilityProvider<Entity> provider = AbilityProvider.getProvider(entity);
				provider.fromPacket(buf);
			}
		}));

		ClientPlayNetworking.registerGlobalReceiver(AlchemyMod.getId("abilities_packet_player"), ((client, handler, buf, responseSender) -> {
			PacketByteBuf buf2 = new PacketByteBuf(buf.copy());
			client.execute(() -> {
				buf2.readInt();
				AbilityProvider<Entity> provider = AbilityProvider.getProvider(client.player);
				provider.fromPacket(buf2);
			});
		}));

		ClientPlayNetworking.registerGlobalReceiver(AlchemyMod.getId("research_click"), ((client, handler, buf, responseSender) -> {
			Identifier id = buf.readIdentifier();
			client.getToastManager().add(new ResearchToast(Research.REGISTRY.get(id)));
			AbilityProvider<Entity> provider = AbilityProvider.getProvider(client.player);
			Optional<PlayerResearchAbility> optional = provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class);
			optional.ifPresent((ability) -> {
				ability.fromPacket(buf);
			});
		}));

		ClientPlayNetworking.registerGlobalReceiver(AlchemyMod.getId("research_packet"), (client, handler, buf, responseSender) -> {
			ResearchCategory.REGISTRY.fromPacket(buf);
			Research.REGISTRY.fromPacket(buf);
		});
		ClientPlayNetworking.registerGlobalReceiver(AlchemyMod.getId("chunk_packet"), (client, handler, buf, responseSender) -> {
			ChunkPos pos = new ChunkPos(buf.readInt(), buf.readInt());
			AbilityProvider<Chunk> provider = AbilityProvider.getProvider(client.world.getChunk(pos.getStartPos()));
			provider.fromPacket(buf);
		});
		ClientPlayNetworking.registerGlobalReceiver(AlchemyMod.getId("vis_packet"), (client, handler, buf, responder) -> {
			ChunkPos pos = new ChunkPos(buf.readInt(), buf.readInt());
			AbilityProvider<Chunk> provider = AbilityProvider.getProvider(client.world.getChunk(pos.getStartPos()));
			provider.removeAbility(VisAbility.ID);
			provider.addAbility(AbilityProvider.CHUNK_REGISTRY.create(VisAbility.ID, buf));
		});
		RecipeTabType.REGISTRY.register(RecipeType.CRAFTING, ((recipe, x, y, width, height) -> {
			ItemElement[] items = new ItemElement[recipe.getPreviewInputs().size() + 1];
			int offsetX = x + (width / 2) - 50;
			//TODO make a special recipe type enum or some other solution to this
			if(recipe instanceof ShapedRecipe)
			{
				items = new ItemElement[10];
				int o = 0;
				int input = 0;
				ShapedRecipe recipe1 = (ShapedRecipe)recipe;
				for(int recipeY = 0; recipeY < 3; recipeY++)
				{
					for(int recipeX = 0; recipeX < 3; recipeX++)
					{
						if(recipeX < recipe1.getWidth() && recipeY < recipe1.getHeight())
						{
							items[o] = new ItemElement(recipe.getPreviewInputs().get(input).getMatchingStacksClient(), offsetX + (o % 3) * 20, y + (o / 3) * 20);
							input++;
						}
						else
						{
							items[o] = new ItemElement(new ItemStack[]{ItemStack.EMPTY}, offsetX + (o % 3) * 20, y + (o / 3) * 20);
						}
						o++;
					}
				}
			}
			else
			{
				for(int o = 0; o < recipe.getPreviewInputs().size(); o++)
				{
					items[o] = new ItemElement(recipe.getPreviewInputs().get(o).getMatchingStacksClient(), offsetX + (o % 3) * 20, y + (o / 3) * 20);
				}
			}
			items[items.length - 1] = new ItemElement(new ItemStack[]{recipe.getOutput()}, offsetX + 84, y + 20);
			return new RecipeElement(items, true);
		}));
		RecipeTabType.REGISTRY.register(AlchemyRecipes.PEDESTAL_RECIPE, ((recipe2, x, y, width, height) -> {
			PedestalRecipe recipe = (PedestalRecipe) recipe2;
			ItemElement[] items = new ItemElement[recipe.getPreviewInputs().size() + 1 + recipe.getAspects().size()];
			Point origin = new Point(x + width / 2 - 32, y + height / 2 - 64);
			items[0] = new ItemElement(recipe.getPreviewInputs().get(0).getMatchingStacksClient(), origin.x, origin.y);
			int total = recipe.getPreviewInputs().size();
			for(int o = 1; o < total; o++)
			{
				double angle = Math.PI * 2  / (total - 1) * o;
				int circleX = origin.x + (int)(40 * Math.cos(angle));
				int circleY = origin.y + (int)(40 * Math.sin(angle));
				items[o] = new ItemElement(recipe.getPreviewInputs().get(o).getMatchingStacksClient(), circleX, circleY);
			}
			int startX = x + width / 2 - (recipe.getAspects().size() * 20) / 2;
			if(!recipe.getAspects().isEmpty())
			{
				for(int o = 0; o < recipe.getAspects().size(); o++)
				{
					ItemStack stack = new ItemStack(recipe.getAspects().get(o).getAspect().getItem(), recipe.getAspects().get(o).getCount());
					items[total + o] = new ItemElement(new ItemStack[]{stack}, startX + o * 20, origin.y + 60);
				}
			}
			items[items.length - 1] = new ItemElement(new ItemStack[]{recipe.getOutput()}, origin.x + 84, origin.y);
			return new RecipeElement(items, false);
		}));
		RecipeTabType.REGISTRY.register(AlchemyRecipes.CAULDRON_ITEM_RECIPE, ((recipe2, x, y, width, height) -> {
			CauldronItemRecipe recipe = (CauldronItemRecipe)recipe2;
			ItemElement[] items = new ItemElement[recipe.getInputs().size() + 2];
			int offsetX = x + (width / 2) - 50;
			items[0] = new ItemElement(recipe.getCatalyst().getMatchingStacksClient(), offsetX, y);
			for(int i = 1; i < items.length - 1; i++)
			{
				items[i] = new ItemElement(recipe.getInputs().get(i - 1).getMatchingStacksClient(), offsetX + 24 + i * 24, y + 40);
			}
			items[items.length - 1] = new ItemElement(new ItemStack[]{recipe.getOutput()}, offsetX + 36, y);
			return new RecipeElement(items, true);
		}));
		Arrays.stream(AlchemyArmorMaterials.values()).forEach(item -> {
			preRegisterArmorTextures(AlchemyMod.getId(item.getName()), false);
		});
		AbilityProvider.CHUNK_REGISTRY.registerPacket(AlchemyMod.getId("vis_ability"), VisAbilityImpl::new);
	}
}
