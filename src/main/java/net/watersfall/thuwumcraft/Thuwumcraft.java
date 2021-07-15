package net.watersfall.thuwumcraft;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.watersfall.thuwumcraft.abilities.chunk.VisAbilityImpl;
import net.watersfall.thuwumcraft.abilities.entity.PlayerResearchAbilityImpl;
import net.watersfall.thuwumcraft.abilities.entity.PlayerUnknownAbilityImpl;
import net.watersfall.thuwumcraft.abilities.entity.RunedShieldAbilityEntity;
import net.watersfall.thuwumcraft.abilities.item.*;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.api.abilities.common.StatusEffectItem;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerUnknownAbility;
import net.watersfall.thuwumcraft.api.abilities.item.BerserkerWeapon;
import net.watersfall.thuwumcraft.api.abilities.item.WandAbility;
import net.watersfall.thuwumcraft.api.abilities.item.WandFocusAbility;
import net.watersfall.thuwumcraft.api.aspect.Aspects;
import net.watersfall.thuwumcraft.api.lookup.AspectContainer;
import net.watersfall.thuwumcraft.api.multiblock.MultiBlockRegistry;
import net.watersfall.thuwumcraft.api.research.Research;
import net.watersfall.thuwumcraft.api.research.ResearchCategory;
import net.watersfall.thuwumcraft.api.sound.AlchemySounds;
import net.watersfall.thuwumcraft.api.tag.AlchemyBlockTags;
import net.watersfall.thuwumcraft.api.tag.AlchemyEntityTags;
import net.watersfall.thuwumcraft.block.EssentiaSmeltery;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlocks;
import net.watersfall.thuwumcraft.block.entity.PedestalEntity;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;
import net.watersfall.thuwumcraft.registry.ThuwumcraftStatusEffects;
import net.watersfall.thuwumcraft.registry.ThuwumcraftFluids;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;
import net.watersfall.thuwumcraft.multiblock.type.AlchemicalFurnaceType;
import net.watersfall.thuwumcraft.recipe.PedestalRecipe;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRecipes;
import net.watersfall.thuwumcraft.research.ResearchCategoryLoader;
import net.watersfall.thuwumcraft.research.ResearchLoader;
import net.watersfall.thuwumcraft.world.biome.ThuwumcraftBiomes;
import net.watersfall.thuwumcraft.world.feature.ThuwumcraftFeatures;
import net.watersfall.thuwumcraft.world.feature.structure.ThuwumcraftStructureFeatures;
import net.watersfall.thuwumcraft.world.structure.ThuwumcraftStructurePieceTypes;
import net.watersfall.thuwumcraft.world.village.VillageAdditions;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Thuwumcraft implements ModInitializer
{
	public static final String MOD_ID = "thuwumcraft";
	private static Tag<Item> INGREDIENT_TAG;

	private static Set<Item> getAllIngredients(MinecraftServer server)
	{
		Set<Item> set = new HashSet<>();
		server.getRecipeManager().listAllOfType(ThuwumcraftRecipes.CAULDRON_INGREDIENTS).forEach((item) -> set.add(item.getInput().getItem()));
		return set;
	}

	public static Identifier getId(String id)
	{
		return new Identifier(MOD_ID, id);
	}

	public static Tag<Item> getIngredientTag()
	{
		return INGREDIENT_TAG;
	}

	private static void setIngredientTag(Tag<Item> ingredientTag)
	{
		INGREDIENT_TAG = ingredientTag;
	}

	/**
	 * Network
	 */
	private static void registerNetwork()
	{
		ServerPlayNetworking.registerGlobalReceiver(getId("research_click"), ((server, player, handler, buf, responseSender) -> {
			AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
			Optional<PlayerResearchAbility> optional = provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class);
			optional.ifPresent((ability) -> {
				Research research = Research.REGISTRY.get(buf.readIdentifier());
				if(!ability.hasResearch(research) && research.isAvailable(ability) && research.hasItems(player))
				{
					ability.addResearch(research.getId());
					PacketByteBuf buf2 = PacketByteBufs.create();
					buf2.writeIdentifier(research.getId());
					research.consumeItems(player);
					responseSender.sendPacket(getId("research_click"), ability.toPacket(buf2));
				}
			});
		}));
		ServerPlayNetworking.registerGlobalReceiver(getId("chunk_packet"), (server, player, handler, buf, responseSender) -> {
			ChunkPos pos = new ChunkPos(buf.readInt(), buf.readInt());
			AbilityProvider<Chunk> provider = AbilityProvider.getProvider(player.getServerWorld().getChunk(pos.getStartPos()));
			PacketByteBuf buf2 = PacketByteBufs.create();
			buf2.writeInt(pos.x);
			buf2.writeInt(pos.z);
			provider.toPacket(buf2);
			responseSender.sendPacket(getId("chunk_packet"), buf2);
		});
		ServerPlayNetworking.registerGlobalReceiver(getId("focus_click"), (server, player, handler, buf, responseSender) -> {
			int index = buf.readInt();
			ItemStack hand = player.getMainHandStack();
			ItemStack other = player.getInventory().getStack(index);
			AbilityProvider<ItemStack> handProvider = AbilityProvider.getProvider(hand);
			AbilityProvider<ItemStack> otherProvider = AbilityProvider.getProvider(other);
			handProvider.getAbility(WandAbility.ID, WandAbility.class).ifPresent(wand -> {
				otherProvider.getAbility(WandFocusAbility.ID, WandFocusAbility.class).ifPresent(focus -> {
					if(wand.getSpell() != null && wand.getSpell().spell() != null)
					{
						ItemStack newFocus = new ItemStack(ThuwumcraftItems.WAND_FOCUS);
						AbilityProvider<ItemStack> newFocusProvider = AbilityProvider.getProvider(newFocus);
						newFocusProvider.addAbility(new WandFocusAbilityImpl(wand.getSpell().spell(), newFocus));
						player.getInventory().setStack(index, newFocus);
					}
					else
					{
						player.getInventory().setStack(index, ItemStack.EMPTY);
					}
					wand.setSpell(focus.getSpell());
				});
			});
		});
		ServerPlayNetworking.registerGlobalReceiver(getId("focus_remove_click"), (server, player, handler, buf, responseSender) -> {
			ItemStack hand = player.getMainHandStack();
			AbilityProvider<ItemStack> handProvider = AbilityProvider.getProvider(hand);
			handProvider.getAbility(WandAbility.ID, WandAbility.class).ifPresent(wand -> {
				if(wand.getSpell() != null && wand.getSpell().spell() != null)
				{
					ItemStack stack = new ItemStack(ThuwumcraftItems.WAND_FOCUS);
					AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
					provider.addAbility(new WandFocusAbilityImpl(wand.getSpell().spell(), stack));
					if(!player.giveItemStack(stack))
					{
						player.dropItem(stack, true);
					}
					wand.setSpell(null);
				}
			});
		});
		ServerPlayNetworking.registerGlobalReceiver(getId("player_close_research_book"), (server, player, handler, buf, responseSender) -> {
			float x = buf.readFloat();
			float y = buf.readFloat();
			float scale = buf.readFloat();
			Identifier category = buf.readIdentifier();
			AbilityProvider.getProvider(player).getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).ifPresent(ability -> {
				ability.setLastCategory(category);
				ability.setX(x);
				ability.setY(y);
				ability.setScale(scale);
			});
		});
	}

	/**
	 * Events
	 */
	private static void registerEvents()
	{
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if(!world.isClient && entity instanceof LivingEntity living)
			{
				AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(player.getStackInHand(hand));
				provider.getAbility(StatusEffectItem.ID, StatusEffectItem.class).ifPresent(ability -> {
					ability.use(living, player.getStackInHand(hand));
				});
			}
			return ActionResult.PASS;
		});
		ServerLifecycleEvents.SERVER_STARTED.register((server -> setIngredientTag(Tag.of(getAllIngredients(server)))));
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> {
			if(success)
			{
				setIngredientTag(Tag.of(getAllIngredients(server)));
			}
		});
		ServerTickEvents.END_SERVER_TICK.register(server -> MultiBlockRegistry.SERVER_TICKER.tick());
		DispenserBlock.registerBehavior(ThuwumcraftItems.WITCHY_SPOON_ITEM, ((pointer, stack) -> {
			Direction direction = pointer.getWorld().getBlockState(pointer.getPos()).get(Properties.FACING);
			BlockEntity test = pointer.getWorld().getBlockEntity(pointer.getPos().offset(direction));
			if(test instanceof PedestalEntity entity)
			{
				World world = pointer.getWorld();
				Optional<PedestalRecipe> recipeOptional = pointer.getWorld().getRecipeManager().getFirstMatch(ThuwumcraftRecipes.PEDESTAL_RECIPE, entity, world);
				recipeOptional.ifPresent(entity::beginCraft);
			}
			return stack;
		}));
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			AbilityProvider<Entity> provider = AbilityProvider.getProvider(entity);
			PacketByteBuf buf = PacketByteBufs.create();
			provider.toPacket(buf);
			for(ServerPlayerEntity player : PlayerLookup.tracking(entity))
			{
				ServerPlayNetworking.send(player, getId("abilities_packet"), buf);
			}
			if(entity.getType() == EntityType.PLAYER && FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
			{
				PacketByteBuf research = PacketByteBufs.create();
				ResearchCategory.REGISTRY.toPacket(research);
				Research.REGISTRY.toPacket(research);
				ServerPlayNetworking.send((ServerPlayerEntity)entity, getId("research_packet"), research);
			}
		});
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> {
			if(success)
			{
				for(ServerPlayerEntity player : PlayerLookup.all(server))
				{
					PacketByteBuf research = PacketByteBufs.create();
					ResearchCategory.REGISTRY.toPacket(research);
					Research.REGISTRY.toPacket(research);
					ServerPlayNetworking.send(player, getId("research_packet"), research);
					AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
					provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).ifPresent((ability) -> {
						NbtCompound tag = ability.toNbt(new NbtCompound(), player);
						ability.fromNbt(tag, player);
					});
				}
			}
		});
		LootTableLoadingCallback.EVENT.register(((resourceManager, manager, id, supplier, setter) -> {
			if(id.equals(LootTables.SIMPLE_DUNGEON_CHEST))
			{
				FabricLootPoolBuilder pool = FabricLootPoolBuilder.builder()
						.rolls(ConstantLootNumberProvider.create(1))
						.withEntry(ItemEntry.builder(ThuwumcraftItems.SNOW_STAFF)
								.conditionally(RandomChanceLootCondition.builder(0.25F))
								.build()
						);
				supplier.withPool(pool.build());
			}
			if(id.getPath().startsWith("entities/"))
			{
				String[] path = id.getPath().split("/");
				Optional<EntityType<?>> optional = EntityType.get(path[path.length - 1]);
				if(optional.isPresent())
				{
					generateNecromancyDrop(ThuwumcraftItems.NECROMANCY_SKULL, supplier, AlchemyEntityTags.DROPS_HEAD);
					generateNecromancyDrop(ThuwumcraftItems.NECROMANCY_ARM, supplier, AlchemyEntityTags.DROPS_ARM);
					generateNecromancyDrop(ThuwumcraftItems.NECROMANCY_LEG, supplier, AlchemyEntityTags.DROPS_LEG);
					generateNecromancyDrop(ThuwumcraftItems.NECROMANCY_HEART, supplier, AlchemyEntityTags.DROPS_HEART);
					generateNecromancyDrop(ThuwumcraftItems.NECROMANCY_RIBCAGE, supplier, AlchemyEntityTags.DROPS_RIBCAGE);
				}
			}
		}));
	}

	private static void generateNecromancyDrop(Item item, FabricLootSupplierBuilder supplier, Tag<EntityType<?>> tag)
	{
		FabricLootPoolBuilder pool = FabricLootPoolBuilder.builder()
				.rolls(ConstantLootNumberProvider.create(1))
				.withEntry(ItemEntry.builder(item)
				.conditionally(RandomChanceWithLootingLootCondition.builder(0.05F, 0.05F))
				.conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().type(tag)))
				.build()
				);
		supplier.withPool(pool.build());
	}

	/**
	 * Aspects
	 */
	private static void registerAspects()
	{
		Aspects.register(Aspects.AIR.getId(), Aspects.AIR);
		Aspects.register(Aspects.EARTH.getId(), Aspects.EARTH);
		Aspects.register(Aspects.WATER.getId(), Aspects.WATER);
		Aspects.register(Aspects.FIRE.getId(), Aspects.FIRE);
		Aspects.register(Aspects.ORDER.getId(), Aspects.ORDER);
		Aspects.register(Aspects.DISORDER.getId(), Aspects.DISORDER);
		Aspects.register(Aspects.METAL.getId(), Aspects.METAL);
	}

	/**
	 * Sounds
	 */
	private static void registerSounds()
	{
		Registry.register(Registry.SOUND_EVENT, getId("block.cauldron.add_ingredient"), AlchemySounds.CAULDRON_ADD_INGREDIENT);
		Registry.register(Registry.SOUND_EVENT, getId("block.crucible.create"), AlchemySounds.USE_DUST_SOUND);
		Registry.register(Registry.SOUND_EVENT, getId("block.cauldron.bubble"), AlchemySounds.BUBBLE_SOUND);
		Registry.register(Registry.SOUND_EVENT, getId("item.research_book.open"), AlchemySounds.BOOK_OPEN_SOUND);
	}

	/**
	 * Abilities
	 */
	private static void registerAbilities()
	{
		AbilityProvider.ENTITY_REGISTRY.register(getId("runed_shield_ability"), RunedShieldAbilityEntity::new);
		AbilityProvider.ENTITY_REGISTRY.registerPacket(getId("runed_shield_ability"), RunedShieldAbilityEntity::new);
		AbilityProvider.ITEM_REGISTRY.register(getId("runed_shield_ability"), RunedShieldAbilityItem::new);
		AbilityProvider.ITEM_REGISTRY.register(getId("aspect_storage_ability"), PhialStorageAbility::new);
		AbilityProvider.ENTITY_REGISTRY.register(getId("player_research_ability"), PlayerResearchAbilityImpl::new);
		AbilityProvider.CHUNK_REGISTRY.register(getId("vis_ability"), VisAbilityImpl::new);
		AbilityProvider.ENTITY_REGISTRY.register(PlayerUnknownAbility.ID, PlayerUnknownAbilityImpl::new);
		AbilityProvider.ITEM_REGISTRY.register(WandAbility.ID, WandAbilityImpl::new);
		AbilityProvider.ITEM_REGISTRY.register(WandFocusAbility.ID, WandFocusAbilityImpl::new);
		AbilityProvider.ITEM_REGISTRY.register(BerserkerWeapon.ID, BerserkerWeaponImpl::new);
		AbilityProvider.ITEM_REGISTRY.register(StatusEffectItem.ID, StatusEffectItemImpl::new);
	}

	/**
	 * Lookup API
	 */
	private static void registerLookup()
	{
		AspectContainer.API.registerForBlocks((world, pos, state, entity, direction) -> {
			return (AspectContainer)entity;
		}, ThuwumcraftBlocks.ASPECT_PIPE_BLOCK);
		AspectContainer.API.registerForBlockEntities((entity, direction) -> {
			if(direction == null || direction == Direction.UP)
			{
				return (AspectContainer)entity;
			}
			return null;
		}, ThuwumcraftBlockEntities.JAR_ENTITY);
		AspectContainer.API.registerForBlockEntities((entity, direction) -> {
			if(direction == null)
			{
				return (AspectContainer)entity;
			}
			World world = entity.getWorld();
			BlockState state = world.getBlockState(entity.getPos().offset(direction));
			if(state.isIn(AlchemyBlockTags.ESSENTIA_REFINERIES) || state.getBlock() instanceof EssentiaSmeltery)
			{
				return (AspectContainer)entity;
			}
			return null;
		}, ThuwumcraftBlockEntities.ESSENTIA_SMELTERY_ENTITY);
		AspectContainer.API.registerForBlockEntities((entity, direction) -> {
			return (AspectContainer)entity;
		}, ThuwumcraftBlockEntities.ESSENTIA_REFINERY);
	}

	/**
	 * Reload Listeners
	 */
	private static void registerReloadListeners()
	{
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ResearchCategoryLoader());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ResearchLoader());
	}

	/**
	 * Biome Modifications
	 */
	private static void registerBiomeModifications()
	{
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
				GenerationStep.Feature.UNDERGROUND_DECORATION,
				BuiltinRegistries.CONFIGURED_FEATURE.getKey(ThuwumcraftFeatures.EARTH_CRYSTAL_GEODE).get());
		BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(),
				GenerationStep.Feature.UNDERGROUND_DECORATION,
				BuiltinRegistries.CONFIGURED_FEATURE.getKey(ThuwumcraftFeatures.NETHER_GEODE).get());
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.BASALT_DELTAS),
				GenerationStep.Feature.UNDERGROUND_DECORATION,
				BuiltinRegistries.CONFIGURED_FEATURE.getKey(ThuwumcraftFeatures.BASALT_DELTA_GEODE).get());
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
				GenerationStep.Feature.UNDERGROUND_DECORATION,
				BuiltinRegistries.CONFIGURED_FEATURE.getKey(ThuwumcraftFeatures.DIMENSIONAL_LAKE).get());
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
				GenerationStep.Feature.VEGETAL_DECORATION,
				BuiltinRegistries.CONFIGURED_FEATURE.getKey(ThuwumcraftFeatures.SILVERWOOD_TREE).get());
	}

	private static void registerMultiBlocks()
	{
		MultiBlockRegistry.TYPES.add(AlchemicalFurnaceType.INSTANCE);
	}

	@Override
	public void onInitialize()
	{
		ThuwumcraftStatusEffects.register();
		ThuwumcraftRecipes.register();
		ThuwumcraftBlockEntities.register();
		ThuwumcraftFluids.register();
		registerEvents();
		registerAspects();
		registerSounds();
		registerMultiBlocks();
		registerAbilities();
		registerNetwork();
		ThuwumcraftFeatures.register();
		AlchemyEntityTags.register();
		ThuwumcraftBiomes.register();
		ThuwumcraftStructurePieceTypes.register();
		ThuwumcraftStructureFeatures.register();
		registerLookup();
		registerReloadListeners();
		registerBiomeModifications();
		VillageAdditions.register();
	}
}
