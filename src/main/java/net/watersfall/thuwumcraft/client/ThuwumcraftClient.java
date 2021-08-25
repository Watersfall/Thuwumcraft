package net.watersfall.thuwumcraft.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.abilities.chunk.VisAbilityImpl;
import net.watersfall.thuwumcraft.abilities.entity.PlayerResearchAbilityImpl;
import net.watersfall.thuwumcraft.abilities.entity.PlayerUnknownAbilityImpl;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.api.abilities.chunk.VisAbility;
import net.watersfall.thuwumcraft.api.abilities.common.StatusEffectItem;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerUnknownAbility;
import net.watersfall.thuwumcraft.api.abilities.item.WandAbility;
import net.watersfall.thuwumcraft.api.abilities.item.WandFocusAbility;
import net.watersfall.thuwumcraft.api.aspect.AspectInventory;
import net.watersfall.thuwumcraft.api.client.gui.BookRecipeTypes;
import net.watersfall.thuwumcraft.api.client.gui.RecipeTabType;
import net.watersfall.thuwumcraft.api.client.item.MultiTooltipComponent;
import net.watersfall.thuwumcraft.api.multiblock.MultiBlockRegistry;
import net.watersfall.thuwumcraft.api.research.Research;
import net.watersfall.thuwumcraft.api.research.ResearchCategory;
import net.watersfall.thuwumcraft.client.accessor.ArmorFeatureRendererAccessor;
import net.watersfall.thuwumcraft.client.gui.*;
import net.watersfall.thuwumcraft.client.gui.element.ItemElement;
import net.watersfall.thuwumcraft.client.gui.element.RecipeElement;
import net.watersfall.thuwumcraft.client.gui.item.AspectTooltipComponent;
import net.watersfall.thuwumcraft.client.item.AspectTooltipData;
import net.watersfall.thuwumcraft.client.particle.FireParticle;
import net.watersfall.thuwumcraft.client.particle.MagicForestParticle;
import net.watersfall.thuwumcraft.client.particle.WaterParticle;
import net.watersfall.thuwumcraft.client.renderer.block.*;
import net.watersfall.thuwumcraft.client.renderer.entity.WaterEntityRenderer;
import net.watersfall.thuwumcraft.client.renderer.entity.WindEntityRenderer;
import net.watersfall.thuwumcraft.client.toast.ResearchToast;
import net.watersfall.thuwumcraft.entity.WindEntity;
import net.watersfall.thuwumcraft.gui.ThuwumcraftScreenHandlers;
import net.watersfall.thuwumcraft.item.armor.AlchemyArmorMaterials;
import net.watersfall.thuwumcraft.particle.ThuwumcraftParticles;
import net.watersfall.thuwumcraft.recipe.AspectIngredient;
import net.watersfall.thuwumcraft.recipe.CauldronItemRecipe;
import net.watersfall.thuwumcraft.recipe.PedestalRecipe;
import net.watersfall.thuwumcraft.registry.*;
import net.watersfall.thuwumcraft.util.StatusEffectHelper;
import net.watersfall.thuwumcraft.world.ThuwumcraftWorlds;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ThuwumcraftClient implements ClientModInitializer
{
	private static final Identifier UNKNOWN_VIGNETTE = Thuwumcraft.getId("textures/misc/unknown_vignette.png");
	private static final Identifier VANILLA_VIGNETTE = new Identifier("textures/misc/vignette.png");
	public static final Identifier ALCHEMY_TEXTURE = Thuwumcraft.getId("textures/gui/research/recipe/alchemy.png");
	public static final Identifier CRAFTING_TEXTURE = Thuwumcraft.getId("textures/gui/research/recipe/crafting.png");
	public static final KeyBinding WAND_FOCUS_KEY = new KeyBinding("key.thuwumcraft.wand_focus", GLFW.GLFW_KEY_C, KeyBinding.UI_CATEGORY);
	public static boolean wandFocusKeyPressed = false;

	private static void registerEvents()
	{
		ItemTooltipCallback.EVENT.register(((stack, context, tooltip) -> {
			if(stack.getNbt() != null && !stack.getNbt().isEmpty())
			{
				NbtCompound tag = stack.getNbt();
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
			AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
			provider.getAbility(StatusEffectItem.ID, StatusEffectItem.class).ifPresent(ability -> {
				if(ability.getEffects().size() > 0)
				{
					tooltip.add(StatusEffectHelper.APPLIED_EFFECTS);
					ability.getEffects().forEach(effect -> {
						tooltip.add(StatusEffectHelper.getEffectText(effect, true));
					});
				}
				else
				{
					tooltip.add(StatusEffectHelper.NO_EFFECT);
				}
			});
		}));
		HudRenderCallback.EVENT.register((matrices, delta) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			PlayerEntity player = client.player;
			if(player.hasStatusEffect(ThuwumcraftStatusEffects.BERSERK))
			{
				StatusEffectInstance effect = player.getStatusEffect(ThuwumcraftStatusEffects.BERSERK);
				int height = client.getWindow().getScaledHeight();
				int width = client.getWindow().getScaledWidth();
				float color = (effect.getAmplifier() + 1) / 2F;
				RenderSystem.enableBlend();
				RenderSystem.setShaderColor(color, color, color, 1F);
				RenderSystem.disableDepthTest();
				RenderSystem.depthMask(false);
				RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				RenderSystem.setShaderTexture(0, VANILLA_VIGNETTE);
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder bufferBuilder = tessellator.getBuffer();
				bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
				bufferBuilder.vertex(0.0D, height, -90.0D).texture(0.0F, 1.0F).next();
				bufferBuilder.vertex(width, height, -90.0D).texture(1.0F, 1.0F).next();
				bufferBuilder.vertex(width, 0.0D, -90.0D).texture(1.0F, 0.0F).next();
				bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();
				tessellator.draw();
				RenderSystem.depthMask(true);
				RenderSystem.enableDepthTest();
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.defaultBlendFunc();
				RenderSystem.disableBlend();
			}
			if(player.world.getRegistryKey() == ThuwumcraftWorlds.THE_UNKNOWN)
			{
				AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
				provider.getAbility(PlayerUnknownAbility.ID, PlayerUnknownAbility.class).ifPresent(ability -> {
					if(ability.isTemporary())
					{
						int height = client.getWindow().getScaledHeight();
						int width = client.getWindow().getScaledWidth();
						float color = (ability.getTicksInUnknown()) / 150F;
						RenderSystem.enableBlend();
						RenderSystem.setShaderColor(color, color, color, 1F);
						RenderSystem.disableDepthTest();
						RenderSystem.depthMask(false);
						RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
						RenderSystem.setShader(GameRenderer::getPositionTexShader);
						RenderSystem.setShaderTexture(0, UNKNOWN_VIGNETTE);
						Tessellator tessellator = Tessellator.getInstance();
						BufferBuilder bufferBuilder = tessellator.getBuffer();
						bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
						bufferBuilder.vertex(0.0D, height, -90.0D).texture(0.0F, 1.0F).next();
						bufferBuilder.vertex(width, height, -90.0D).texture(1.0F, 1.0F).next();
						bufferBuilder.vertex(width, 0.0D, -90.0D).texture(1.0F, 0.0F).next();
						bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();
						tessellator.draw();
						RenderSystem.depthMask(true);
						RenderSystem.enableDepthTest();
						RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
						RenderSystem.defaultBlendFunc();
						RenderSystem.disableBlend();
					}
				});
			}
		});
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

	public static void setupFluidRendering(final Fluid still, final Fluid flowing, final Identifier textureFluidId, final int color)
	{
		final Identifier stillSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_still");
		final Identifier flowingSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_flow");

		// If they're not already present, add the sprites to the block atlas
		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
			registry.register(stillSpriteId);
			registry.register(flowingSpriteId);
		});

		final Identifier fluidId = Registry.FLUID.getId(still);
		final Identifier listenerId = new Identifier(fluidId.getNamespace(), fluidId.getPath() + "_reload_listener");

		final Sprite[] fluidSprites = { null, null };

		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return listenerId;
			}

			/**
			 * Get the sprites from the block atlas when resources are reloaded
			 */
			@Override
			public void reload(ResourceManager resourceManager) {
				final Function<Identifier, Sprite> atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
				fluidSprites[0] = atlas.apply(stillSpriteId);
				fluidSprites[1] = atlas.apply(flowingSpriteId);
			}
		});

		// The FluidRenderer gets the sprites and color from a FluidRenderHandler during rendering
		final FluidRenderHandler renderHandler = new FluidRenderHandler()
		{
			@Override
			public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state) {
				return fluidSprites;
			}

			@Override
			public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state) {
				return color;
			}
		};

		FluidRenderHandlerRegistry.INSTANCE.register(still, renderHandler);
		FluidRenderHandlerRegistry.INSTANCE.register(flowing, renderHandler);
	}

	private void checkKeys(MinecraftClient client)
	{
		if(client.player != null)
		{
			if(WAND_FOCUS_KEY.isPressed())
			{
				ItemStack main = client.player.getMainHandStack();
				if(!wandFocusKeyPressed && main.getItem() == ThuwumcraftItems.WAND)
				{
					client.setScreen(new FocusChangeScreen(main));
					wandFocusKeyPressed = true;
				}
			}
			else if(wandFocusKeyPressed)
			{
				wandFocusKeyPressed = false;
			}
		}
	}

	@Override
	public void onInitializeClient()
	{
		FabricModelPredicateProviderRegistry.register(ThuwumcraftItems.WAND, Thuwumcraft.getId("has_focus"), ((stack, world, entity, seed) -> {
			AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
			Optional<WandAbility> optional = provider.getAbility(WandAbility.ID, WandAbility.class);
			if(optional.isPresent())
			{
				WandAbility ability = optional.get();
				if(ability.getSpell() != null && ability.getSpell().spell() != null)
				{
					return 1;
				}
			}
			return 0;
		}));
		ColorProviderRegistry.BLOCK.register(
				(state, view, pos, tintIndex) -> BiomeColors.getWaterColor(view, pos),
				ThuwumcraftBlocks.BREWING_CAULDRON_BLOCK
		);
		ColorProviderRegistry.BLOCK.register(
				((state, world, pos, tintIndex) -> 0x000000),
				ThuwumcraftBlocks.DIMENSIONAL_FLUID_BLOCK
		);
		ColorProviderRegistry.BLOCK.register(
				(state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getColor(0.5D, 1.0D),
				ThuwumcraftBlocks.DEEPSLATE_GRASS
		);
		ColorProviderRegistry.ITEM.register(
				(stack, tintIndex) -> GrassColors.getColor(0.5D, 1.0D),
				ThuwumcraftBlocks.DEEPSLATE_GRASS
		);
		ColorProviderRegistry.BLOCK.register(
				(state, world, pos, tintIndex) -> 0x00AAFF,
				ThuwumcraftBlocks.SILVERWOOD_LEAVES
		);
		ColorProviderRegistry.ITEM.register(
				(stack, tintIndex) -> {
					AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
					Optional<WandAbility> optional = provider.getAbility(WandAbility.ID, WandAbility.class);
					if(optional.isPresent())
					{
						WandAbility ability = optional.get();
						if(tintIndex == 0)
						{
							return ability.getWandCore() != null ? ability.getWandCore().getColor() : 0;
						}
						else if(tintIndex == 1)
						{
							return ability.getWandCap() != null ? ability.getWandCap().getColor() : 0;
						}
						else
						{
							if(ability.getSpell() != null && ability.getSpell().spell() != null)
							{
								return ability.getSpell().spell().color();
							}
							return 0xFFFFFF;
						}
					}
					return 0;
				},
				ThuwumcraftItems.WAND
		);
		ColorProviderRegistry.ITEM.register(
				(stack, tintIndex) -> {
					AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
					Optional<WandFocusAbility> optional = provider.getAbility(WandFocusAbility.ID, WandFocusAbility.class);
					if(optional.isPresent())
					{
						WandFocusAbility ability = optional.get();
						if(ability.getSpell() != null && ability.getSpell().spell() != null)
						{
							return ability.getSpell().spell().color();
						}
					}
					return 0xFFFFFF;
				},
				ThuwumcraftItems.WAND_FOCUS
		);
		ColorProviderRegistry.ITEM.register(
				(stack, tintIndex) -> ((ItemColorProvider)stack.getItem()).getColor(stack, tintIndex),
				ThuwumcraftItems.AIR_RUNE,
				ThuwumcraftItems.WATER_RUNE,
				ThuwumcraftItems.FIRE_RUNE,
				ThuwumcraftItems.EARTH_RUNE,
				ThuwumcraftItems.ORDER_RUNE,
				ThuwumcraftItems.DISORDER_RUNE
		);
		ColorProviderRegistry.BLOCK.register(
				((state, world, pos, tintIndex) -> ((BlockColorProvider)state.getBlock()).getColor(state, world, pos, tintIndex)),
				ThuwumcraftBlocks.ARCANE_SEAL
		);
		ClientTickEvents.START_CLIENT_TICK.register(this::checkKeys);
		KeyBindingHelper.registerKeyBinding(WAND_FOCUS_KEY);
		setupFluidRendering(ThuwumcraftFluids.DIMENSIONAL_STILL, ThuwumcraftFluids.DIMENSIONAL_FLOWING, new Identifier("water"), 0x000000);
		BlockEntityRendererRegistry.INSTANCE.register(ThuwumcraftBlockEntities.BREWING_CAULDRON_ENTITY, BrewingCauldronEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(ThuwumcraftBlockEntities.PEDESTAL_ENTITY, PedestalEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(ThuwumcraftBlockEntities.CRUCIBLE_ENTITY, CrucibleEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(ThuwumcraftBlockEntities.JAR_ENTITY, JarEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(ThuwumcraftBlockEntities.PHIAL_SHELF_ENTITY, PhialShelfEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(ThuwumcraftBlockEntities.CRAFTING_HOPPER, CraftingHopperRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(ThuwumcraftBlockEntities.ESSENTIA_REFINERY, EssentiaRefineryRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(ThuwumcraftBlockEntities.PORTABLE_HOLE_ENTITY, PortableHoleRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(ThuwumcraftBlockEntities.ARCANE_SEAL, ArcaneSealRenderer::new);
		ScreenRegistry.register(ThuwumcraftScreenHandlers.APOTHECARY_GUIDE_HANDLER, ApothecaryGuideScreen::new);
		ScreenRegistry.register(ThuwumcraftScreenHandlers.ALCHEMICAL_FURNACE_HANDLER, AlchemicalFurnaceScreen::new);
		ScreenRegistry.register(ThuwumcraftScreenHandlers.RESEARCH_BOOK_HANDLER, ResearchBookScreen::new);
		ScreenRegistry.register(ThuwumcraftScreenHandlers.NEKOMANCY_TABLE_HANDLER, NekomancyTableScreen::new);
		ScreenRegistry.register(ThuwumcraftScreenHandlers.ASPECT_CRAFTING_HANDLER, AspectCraftingScreen::new);
		ScreenRegistry.register(ThuwumcraftScreenHandlers.POTION_SPRAYER_HANDLER, PotionSprayerScreen::new);
		ScreenRegistry.register(ThuwumcraftScreenHandlers.ESSENTIA_SMELTERY_HANDLER, EssentiaSmelterScreen::new);
		ScreenRegistry.register(ThuwumcraftScreenHandlers.WAND_WORKBENCH, WandWorkbenchScreen::new);
		ScreenRegistry.register(ThuwumcraftScreenHandlers.THAUMATORIUM, ThaumatoriumScreen::new);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(),
				ThuwumcraftBlocks.JAR_BLOCK,
				ThuwumcraftBlocks.ARCANE_SEAL
		);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
				ThuwumcraftBlocks.CUSTOM_SPAWNER,
				ThuwumcraftBlocks.CHILD_BLOCK,
				ThuwumcraftBlocks.SILVERWOOD_SAPLING
		);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(),
				ThuwumcraftBlocks.DEEPSLATE_GRASS
		);
		ClientTickEvents.END_CLIENT_TICK.register(client -> MultiBlockRegistry.CLIENT_TICKER.tick());
		ClientChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeInt(chunk.getPos().x);
			buf.writeInt(chunk.getPos().z);
			ClientPlayNetworking.send(Thuwumcraft.getId("chunk_packet"), buf);
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
				Optional<AspectIngredient> ingredientOptional = manager.getFirstMatch(ThuwumcraftRecipes.ASPECT_INGREDIENTS, new AspectInventory.Impl(stack), MinecraftClient.getInstance().world);
				if(ingredientOptional.isPresent())
				{
					return new AspectTooltipComponent(new AspectTooltipData(ingredientOptional.get().getAspects()));
				}
				return null;
			});
		}
		registerEvents();
		ClientPlayNetworking.registerGlobalReceiver(Thuwumcraft.getId("abilities_packet"), ((client, handler, buf, responseSender) -> {
			PacketByteBuf buf2 = PacketByteBufs.copy(buf);
			client.execute(() -> {
				Entity entity = handler.getWorld().getEntityById(buf2.readInt());
				if(entity instanceof AbilityProvider)
				{
					AbilityProvider<Entity> provider = AbilityProvider.getProvider(entity);
					provider.fromPacket(buf2);
				}
			});
		}));

		ClientPlayNetworking.registerGlobalReceiver(Thuwumcraft.getId("abilities_packet_player"), ((client, handler, buf, responseSender) -> {
			PacketByteBuf buf2 = new PacketByteBuf(buf.copy());
			client.execute(() -> {
				buf2.readInt();
				AbilityProvider<Entity> provider = AbilityProvider.getProvider(client.player);
				provider.fromPacket(buf2);
			});
		}));

		ClientPlayNetworking.registerGlobalReceiver(Thuwumcraft.getId("research_click"), ((client, handler, buf, responseSender) -> {
			Identifier id = buf.readIdentifier();
			client.getToastManager().add(new ResearchToast(Research.REGISTRY.get(id)));
			AbilityProvider<Entity> provider = AbilityProvider.getProvider(client.player);
			Optional<PlayerResearchAbility> optional = provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class);
			optional.ifPresent((ability) -> {
				ability.fromPacket(buf);
			});
		}));

		ClientPlayNetworking.registerGlobalReceiver(Thuwumcraft.getId("research_packet"), (client, handler, buf, responseSender) -> {
			ResearchCategory.REGISTRY.fromPacket(buf);
			Research.REGISTRY.fromPacket(buf);
		});
		ClientPlayNetworking.registerGlobalReceiver(Thuwumcraft.getId("chunk_packet"), (client, handler, buf, responseSender) -> {
			ChunkPos pos = new ChunkPos(buf.readInt(), buf.readInt());
			AbilityProvider<Chunk> provider = AbilityProvider.getProvider(client.world.getChunk(pos.getStartPos()));
			provider.fromPacket(buf);
		});
		ClientPlayNetworking.registerGlobalReceiver(Thuwumcraft.getId("vis_packet"), (client, handler, buf, responder) -> {
			ChunkPos pos = new ChunkPos(buf.readInt(), buf.readInt());
			AbilityProvider<Chunk> provider = AbilityProvider.getProvider(client.world.getChunk(pos.getStartPos()));
			provider.removeAbility(VisAbility.ID);
			provider.addAbility(AbilityProvider.CHUNK_REGISTRY.create(VisAbility.ID, buf));
		});
		ClientPlayNetworking.registerGlobalReceiver(Thuwumcraft.getId("unknown_ability_packet"), (client, handler, buf, responder) -> {
			PacketByteBuf buf2 = PacketByteBufs.copy(buf);
			client.execute(() -> {
				if(client.player != null)
				{
					AbilityProvider<Entity> provider = AbilityProvider.getProvider(client.player);
					Optional<PlayerUnknownAbility> optional = provider.getAbility(PlayerUnknownAbility.ID, PlayerUnknownAbility.class);
					if(optional.isPresent())
					{
						optional.get().fromPacket(buf2);
					}
				}
			});
		});
		ClientPlayNetworking.registerGlobalReceiver(Thuwumcraft.getId("spawn_packet"), (client, handler, buf, response) -> {
			EntityType<?> type = Registry.ENTITY_TYPE.get(buf.readVarInt());
			UUID uuid = buf.readUuid();
			int id = buf.readVarInt();
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			float pitch = buf.readFloat();
			float yaw = buf.readFloat();
			int owner = buf.readVarInt();
			client.execute(() -> {
				Entity entity = type.create(client.world);
				entity.updatePositionAndAngles(x, y, z, yaw, pitch);
				entity.updateTrackedPosition(x, y, z);
				entity.setId(id);
				entity.setUuid(uuid);
				if(owner != -1 && entity instanceof ProjectileEntity)
				{
					((ProjectileEntity)entity).setOwner(client.world.getEntityById(owner));
				}
				if(owner != 1 && entity instanceof WindEntity wind)
				{
					wind.setOwner(client.world.getEntityById(owner));
				}
				client.world.addEntity(id, entity);
			});
		});
		RecipeTabType.REGISTRY.register(BookRecipeTypes.CRAFTING, ((recipe, x, y, width, height) -> {
			RecipeElement.Background background = new RecipeElement.Background(x + 48, y + 80, 96, 96, CRAFTING_TEXTURE);
			ItemElement[] items = new ItemElement[recipe.getIngredients().size() + 1];
			int offsetX = x + 48 + 8;
			int offsetY = y + 80 + 8;
			//TODO make a special recipe type enum or some other solution to this
			if(recipe instanceof ShapedRecipe recipe1)
			{
				items = new ItemElement[10];
				int o = 0;
				int input = 0;
				for(int recipeY = 0; recipeY < 3; recipeY++)
				{
					for(int recipeX = 0; recipeX < 3; recipeX++)
					{
						if(recipeX < recipe1.getWidth() && recipeY < recipe1.getHeight())
						{
							items[o] = new ItemElement(recipe.getIngredients().get(input).getMatchingStacks(), offsetX + (o % 3) * 32, offsetY + (o / 3) * 32);
							input++;
						}
						else
						{
							items[o] = new ItemElement(new ItemStack[]{ItemStack.EMPTY}, offsetX + (o % 3) * 32, offsetY + (o / 3) * 32);
						}
						o++;
					}
				}
			}
			else
			{
				for(int o = 0; o < recipe.getIngredients().size(); o++)
				{
					items[o] = new ItemElement(recipe.getIngredients().get(o).getMatchingStacks(), offsetX + (o % 3) * 32, offsetY + (o / 3) * 32);
				}
			}
			items[items.length - 1] = new ItemElement(new ItemStack[]{recipe.getOutput()}, x + 88, y + 32);
			return new RecipeElement(items, background);
		}));
		RecipeTabType.REGISTRY.register(BookRecipeTypes.INFUSION, ((recipe2, x, y, width, height) -> {
			PedestalRecipe recipe = (PedestalRecipe) recipe2;
			ItemElement[] items = new ItemElement[recipe.getIngredients().size() + 1 + recipe.getAspects().size()];
			Point origin = new Point(x + width / 2 - 8, y + height / 2 - 12);
			items[0] = new ItemElement(recipe.getIngredients().get(0).getMatchingStacks(), origin.x, origin.y);
			int total = recipe.getIngredients().size();
			for(int o = 1; o < total; o++)
			{
				double angle = Math.PI * 2  / (total - 1) * o;
				int circleX = origin.x + (int)(40 * Math.cos(angle));
				int circleY = origin.y + (int)(40 * Math.sin(angle));
				items[o] = new ItemElement(recipe.getIngredients().get(o).getMatchingStacks(), circleX, circleY);
			}
			int startX = x + width / 2 - (recipe.getAspects().size() * 20) / 2;
			if(!recipe.getAspects().isEmpty())
			{
				for(int o = 0; o < recipe.getAspects().size(); o++)
				{
					ItemStack stack = new ItemStack(recipe.getAspects().get(o).getAspect().getItem(), recipe.getAspects().get(o).getCount());
					items[total + o] = new ItemElement(new ItemStack[]{stack}, startX + o * 20, origin.y + 56);
				}
			}
			items[items.length - 1] = new ItemElement(new ItemStack[]{recipe.getOutput()}, origin.x, origin.y - 108);
			return new RecipeElement(items, RecipeElement.Background.EMPTY);
		}));
		RecipeTabType.REGISTRY.register(BookRecipeTypes.CAULDRON, ((recipe2, x, y, width, height) -> {
			RecipeElement.Background background = new RecipeElement.Background(x + 12, y + 32, 128, 128, ThuwumcraftClient.ALCHEMY_TEXTURE);
			CauldronItemRecipe recipe = (CauldronItemRecipe)recipe2;
			ItemElement[] items = new ItemElement[recipe.getInputs().size() + 2];
			int offsetX = x + (width / 2) - 50;
			items[0] = new ItemElement(recipe.getCatalyst().getMatchingStacks(), offsetX - 19, y + 34);
			int size = recipe.getInputs().size();
			for(int i = 1; i < items.length - 1; i++)
			{
				items[i] = new ItemElement(recipe.getInputs().get(i - 1).getMatchingStacks(), offsetX + i * 24 - (i + size * 12) + 32, y + 128);
			}
			items[items.length - 1] = new ItemElement(new ItemStack[]{recipe.getOutput()}, offsetX + 42, y + 24);
			return new RecipeElement(items, background);
		}));
		Arrays.stream(AlchemyArmorMaterials.values()).forEach(item -> {
			preRegisterArmorTextures(Thuwumcraft.getId(item.getName()), false);
		});
		AbilityProvider.CHUNK_REGISTRY.registerPacket(Thuwumcraft.getId("vis_ability"), VisAbilityImpl::new);
		AbilityProvider.ENTITY_REGISTRY.registerPacket(PlayerUnknownAbility.ID, PlayerUnknownAbilityImpl::new);
		AbilityProvider.ENTITY_REGISTRY.registerPacket(Thuwumcraft.getId("player_research_ability"), PlayerResearchAbilityImpl::new);
		ParticleFactoryRegistry.getInstance().register(ThuwumcraftParticles.MAGIC_FOREST, MagicForestParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ThuwumcraftParticles.WATER, WaterParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ThuwumcraftParticles.FIRE, FireParticle.Factory::new);
		EntityRendererRegistry.INSTANCE.register(ThuwumcraftEntities.ICE_PROJECTILE, FlyingItemEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(ThuwumcraftEntities.WATER_ENTITY, WaterEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(ThuwumcraftEntities.FIRE_ENTITY, WaterEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(ThuwumcraftEntities.SAND_ENTITY, WaterEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(ThuwumcraftEntities.WIND, WindEntityRenderer::new);
		FabricModelPredicateProviderRegistry.register(ThuwumcraftItems.SPECIAL_BATTLEAXE_ITEM, Thuwumcraft.getId("level"), ((stack, world, entity, seed) -> {
			return ThuwumcraftItems.SPECIAL_BATTLEAXE_ITEM.getLevel(stack);
		}));
	}
}
