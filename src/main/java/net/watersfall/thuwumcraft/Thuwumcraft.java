package net.watersfall.thuwumcraft;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
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
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.TagKey;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.watersfall.thuwumcraft.abilities.chunk.GolemMarkersAbilityImpl;
import net.watersfall.thuwumcraft.abilities.chunk.VisAbilityImpl;
import net.watersfall.thuwumcraft.abilities.entity.PlayerResearchAbilityImpl;
import net.watersfall.thuwumcraft.abilities.entity.PlayerUnknownAbilityImpl;
import net.watersfall.thuwumcraft.abilities.entity.PlayerWarpAbilityImpl;
import net.watersfall.thuwumcraft.abilities.entity.RunedShieldAbilityEntity;
import net.watersfall.thuwumcraft.abilities.item.*;
import net.watersfall.thuwumcraft.api.abilities.chunk.GolemMarkersAbility;
import net.watersfall.thuwumcraft.api.abilities.common.StatusEffectItem;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerUnknownAbility;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerWarpAbility;
import net.watersfall.thuwumcraft.api.abilities.item.BerserkerWeapon;
import net.watersfall.thuwumcraft.api.abilities.item.WandAbility;
import net.watersfall.thuwumcraft.api.abilities.item.WandFocusAbility;
import net.watersfall.thuwumcraft.api.aspect.Aspects;
import net.watersfall.thuwumcraft.api.lookup.AspectContainer;
import net.watersfall.thuwumcraft.api.multiblock.MultiBlockRegistry;
import net.watersfall.thuwumcraft.api.player.PlayerWarpEvents;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.api.research.Research;
import net.watersfall.thuwumcraft.api.research.ResearchCategory;
import net.watersfall.thuwumcraft.block.EssentiaSmeltery;
import net.watersfall.thuwumcraft.block.ThaumatoriumBlock;
import net.watersfall.thuwumcraft.block.entity.PedestalEntity;
import net.watersfall.thuwumcraft.entity.mind.MindSpider;
import net.watersfall.thuwumcraft.gui.FocalManipulatorHandler;
import net.watersfall.thuwumcraft.gui.ThaumatoriumHandler;
import net.watersfall.thuwumcraft.item.golem.GolemMarkerItem;
import net.watersfall.thuwumcraft.item.tool.SpecialBattleaxeItem;
import net.watersfall.thuwumcraft.item.wand.WandItem;
import net.watersfall.thuwumcraft.multiblock.type.AlchemicalFurnaceType;
import net.watersfall.thuwumcraft.recipe.PedestalRecipe;
import net.watersfall.thuwumcraft.registry.*;
import net.watersfall.thuwumcraft.registry.tag.ThuwumcraftBlockTags;
import net.watersfall.thuwumcraft.registry.tag.ThuwumcraftEntityTags;
import net.watersfall.thuwumcraft.research.ResearchCategoryLoader;
import net.watersfall.thuwumcraft.research.ResearchLoader;
import net.watersfall.thuwumcraft.world.biome.ThuwumcraftBiomes;
import net.watersfall.thuwumcraft.world.feature.ThuwumcraftConfiguredFeatures;
import net.watersfall.thuwumcraft.world.feature.ThuwumcraftFeatures;
import net.watersfall.thuwumcraft.world.feature.ThuwumcraftPlacedFeatures;
import net.watersfall.thuwumcraft.world.tree.trunk.ThuwumcraftTrunkTypes;
import net.watersfall.thuwumcraft.world.village.VillageAdditions;
import net.watersfall.wet.api.abilities.AbilityProvider;
import net.watersfall.wet.api.event.AbilityCreateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Thuwumcraft implements ModInitializer
{
	public static final String MOD_ID = "thuwumcraft";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	private static TagKey<Item> INGREDIENT_TAG;

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

	public static TagKey<Item> getIngredientTag()
	{
		return INGREDIENT_TAG;
	}

	private static void setIngredientTag(TagKey<Item> ingredientTag)
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
				Research research = ThuwumcraftRegistry.RESEARCH.get(buf.readIdentifier());
				if(!ability.hasResearch(research)/* && research.isAvailable(ability) && research.hasItems(player)*/)
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
			AbilityProvider<Chunk> provider = AbilityProvider.getProvider(player.getWorld().getChunk(pos.getStartPos()));
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
					if(wand.getSpell() != null)
					{
						ItemStack newFocus = new ItemStack(ThuwumcraftItems.WAND_FOCUS);
						AbilityProvider<ItemStack> newFocusProvider = AbilityProvider.getProvider(newFocus);
						newFocusProvider.addAbility(new WandFocusAbilityImpl(wand.getSpell(), newFocus));
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
				if(wand.getSpell() != null)
				{
					ItemStack stack = new ItemStack(ThuwumcraftItems.WAND_FOCUS);
					AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
					provider.addAbility(new WandFocusAbilityImpl(wand.getSpell(), stack));
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
		ServerPlayNetworking.registerGlobalReceiver(getId("thaumatorium_click"), ((server, player, handler, buf, responseSender) -> {
			Identifier recipeId = buf.readIdentifier();
			server.execute(() -> {
				if(player.currentScreenHandler instanceof ThaumatoriumHandler screen)
				{
					screen.getEntity().setCurrentRecipe(recipeId);
				}
			});
		}));

		//TODO: Make this... better
		ServerPlayNetworking.registerGlobalReceiver(getId("spell_create"), ((server, player, handler, buf, responseSender) -> {
			WandFocusAbility ability = new WandFocusAbilityImpl(buf.readNbt(), new ItemStack(ThuwumcraftItems.WAND_FOCUS));
			if(player.currentScreenHandler instanceof FocalManipulatorHandler gui)
			{
				ItemStack stack = gui.getSlot(0).getStack();
				if(stack.isOf(ThuwumcraftItems.WAND_FOCUS) && stack.getAbility(WandFocusAbility.ID, WandFocusAbility.class).isEmpty())
				{
					AbilityProvider.getProvider(stack).addAbility(ability);
					gui.getSlot(0).markDirty();
				}
			}
		}));
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
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeBoolean(success);
			server.getPlayerManager().getPlayerList().forEach(player -> {
				ServerPlayNetworking.send(player, Thuwumcraft.getId("server_reload_packet"), buf);
			});
		});
		ServerTickEvents.END_SERVER_TICK.register(server -> MultiBlockRegistry.SERVER_TICKER.tick());
		DispenserBlock.registerBehavior(ThuwumcraftItems.WITCHY_SPOON, ((pointer, stack) -> {
			Direction direction = pointer.getWorld().getBlockState(pointer.getPos()).get(Properties.FACING);
			BlockEntity test = pointer.getWorld().getBlockEntity(pointer.getPos().offset(direction));
			if(test instanceof PedestalEntity entity)
			{
				World world = pointer.getWorld();
				Optional<PedestalRecipe> recipeOptional = pointer.getWorld().getRecipeManager().getFirstMatch(ThuwumcraftRecipes.PEDESTAL, entity, world);
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
				ResearchCategory.toFullPacket(research);
				Research.toFullPacket(research);
				ServerPlayNetworking.send((ServerPlayerEntity)entity, getId("research_packet"), research);
			}
		});
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> {
			if(success)
			{
				for(ServerPlayerEntity player : PlayerLookup.all(server))
				{
					PacketByteBuf research = PacketByteBufs.create();
					ResearchCategory.toFullPacket(research);
					Research.toFullPacket(research);
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
					generateNecromancyDrop(ThuwumcraftItems.NECROMANCY_SKULL, supplier, ThuwumcraftEntityTags.DROPS_HEAD);
					generateNecromancyDrop(ThuwumcraftItems.NECROMANCY_ARM, supplier, ThuwumcraftEntityTags.DROPS_ARM);
					generateNecromancyDrop(ThuwumcraftItems.NECROMANCY_LEG, supplier, ThuwumcraftEntityTags.DROPS_LEG);
					generateNecromancyDrop(ThuwumcraftItems.NECROMANCY_HEART, supplier, ThuwumcraftEntityTags.DROPS_HEART);
					generateNecromancyDrop(ThuwumcraftItems.NECROMANCY_RIBCAGE, supplier, ThuwumcraftEntityTags.DROPS_RIBCAGE);
				}
			}
		}));
		UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
			if(player.getStackInHand(hand).getItem() == ThuwumcraftItems.WAND)
			{
				if(world.getBlockState(hitResult.getBlockPos()).isOf(Blocks.BOOKSHELF))
				{
					if(!world.isClient)
					{
						world.setBlockState(hitResult.getBlockPos(), Blocks.AIR.getDefaultState());
						ItemScatterer.spawn(world, hitResult.getBlockPos(), DefaultedList.ofSize(1, ThuwumcraftItems.RESEARCH_BOOK.getDefaultStack()));
					}
					world.playSound(player, hitResult.getBlockPos(), ThuwumcraftSounds.USE_DUST_SOUND, SoundCategory.BLOCKS, 1, 1);
					return ActionResult.success(world.isClient);
				}
			}
			return ActionResult.PASS;
		}));
		ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
			AbilityProvider<Entity> provider = AbilityProvider.getProvider(handler.getPlayer());
			PacketByteBuf buf = PacketByteBufs.create();
			provider.toPacket(buf);
			ServerPlayNetworking.send(handler.getPlayer(), getId("abilities_packet_player"), buf);
		}));
		FabricDefaultAttributeRegistry.register(ThuwumcraftEntities.GOLEM,
				DefaultAttributeContainer.builder()
						.add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
						.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
						.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3)
						.add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.5)
						.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16)
						.add(EntityAttributes.GENERIC_ARMOR, 0)
						.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0)
						.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0)
						.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.5)
		);
		FabricDefaultAttributeRegistry.register(ThuwumcraftEntities.MIND_SPIDER,
				DefaultAttributeContainer.builder()
						.add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
						.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
						.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3)
						.add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.5)
						.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16)
						.add(EntityAttributes.GENERIC_ARMOR, 0)
						.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0)
						.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0)
						.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.5)
		);
		PlayerBlockBreakEvents.AFTER.register(((world, player, pos, state, blockEntity) -> {
			Chunk chunk = world.getChunk(pos);
			AbilityProvider<Chunk> provider = AbilityProvider.getProvider(chunk);
			Optional<GolemMarkersAbility> optional = provider.getAbility(GolemMarkersAbility.ID, GolemMarkersAbility.class);
			if(optional.isPresent())
			{
				optional.get().removeMarkers(pos);
				optional.get().sync(chunk);
			}
		}));
		UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
			if(player.getStackInHand(hand).getItem() instanceof GolemMarkerItem)
			{
				return player.getStackInHand(hand).useOnBlock(new ItemUsageContext(player, hand, hitResult));
			}
			return ActionResult.PASS;
		}));
	}

	private static void generateNecromancyDrop(Item item, FabricLootSupplierBuilder supplier, TagKey<EntityType<?>> tag)
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
		Registry.register(Registry.SOUND_EVENT, getId("block.cauldron.add_ingredient"), ThuwumcraftSounds.CAULDRON_ADD_INGREDIENT);
		Registry.register(Registry.SOUND_EVENT, getId("block.crucible.create"), ThuwumcraftSounds.USE_DUST_SOUND);
		Registry.register(Registry.SOUND_EVENT, getId("block.cauldron.bubble"), ThuwumcraftSounds.BUBBLE_SOUND);
		Registry.register(Registry.SOUND_EVENT, getId("item.research_book.open"), ThuwumcraftSounds.BOOK_OPEN_SOUND);
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
		AbilityProvider.CHUNK_REGISTRY.register(GolemMarkersAbility.ID, GolemMarkersAbilityImpl::new);
		AbilityProvider.ENTITY_REGISTRY.register(PlayerWarpAbility.ID, PlayerWarpAbilityImpl::new);
		AbilityCreateEvent.CHUNK.register((world, pos, provider) -> {
			provider.addAbility(new VisAbilityImpl(world, pos, provider));
			provider.addAbility(new GolemMarkersAbilityImpl());
		});
		AbilityCreateEvent.ITEM.register((item, provider) -> {
			if(item.asItem() == Items.NETHERITE_CHESTPLATE)
			{
				provider.addAbility(new RunedShieldAbilityItem(10, 10 ,10));
			}
			else if(item instanceof WandItem)
			{
				provider.addAbility(new WandAbilityImpl());
			}
			else if(item instanceof SpecialBattleaxeItem)
			{
				provider.addAbility(new BerserkerWeaponImpl());
			}
		});
		AbilityCreateEvent.ENTITY.register((type, world, provider) -> {
			if(type == EntityType.PLAYER)
			{
				provider.addAbility(new PlayerResearchAbilityImpl());
				provider.addAbility(new PlayerWarpAbilityImpl());
				provider.addAbility(new PlayerUnknownAbilityImpl());
			}
		});
	}

	/**
	 * Lookup API
	 */
	private static void registerLookup()
	{
		AspectContainer.API.registerForBlocks((world, pos, state, entity, direction) -> {
			return (AspectContainer)entity;
		}, ThuwumcraftBlocks.BRASS_PIPE);
		AspectContainer.API.registerForBlockEntities((entity, direction) -> {
			if(direction == null || direction == Direction.UP)
			{
				return (AspectContainer)entity;
			}
			return null;
		}, ThuwumcraftBlockEntities.JAR);
		AspectContainer.API.registerForBlockEntities((entity, direction) -> {
			if(direction == null)
			{
				return (AspectContainer)entity;
			}
			World world = entity.getWorld();
			BlockState state = world.getBlockState(entity.getPos().offset(direction));
			if(state.isIn(ThuwumcraftBlockTags.ESSENTIA_REFINERIES) || state.getBlock() instanceof EssentiaSmeltery)
			{
				return (AspectContainer)entity;
			}
			return null;
		}, ThuwumcraftBlockEntities.ESSENTIA_SMELTERY);
		AspectContainer.API.registerForBlockEntities((entity, direction) -> {
			return (AspectContainer)entity;
		}, ThuwumcraftBlockEntities.ESSENTIA_REFINERY);
		AspectContainer.API.registerForBlocks((world, pos, state, blockEntity, direction) -> {
			if(direction != Direction.UP && direction != Direction.DOWN && direction != state.get(ThaumatoriumBlock.FACING))
			{
				if(state.get(ThaumatoriumBlock.HALF) == DoubleBlockHalf.UPPER)
				{
					if(world.getBlockEntity(pos.down()) instanceof AspectContainer inventory)
					{
						return inventory;
					}
				}
				else if(blockEntity instanceof AspectContainer inventory)
				{
					return inventory;
				}
			}
			return null;
		}, ThuwumcraftBlocks.THAUMATORIUM);
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
				ThuwumcraftPlacedFeatures.EARTH_CRYSTAL_GEODE_PLACED.getKey().get());
		BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(),
				GenerationStep.Feature.UNDERGROUND_DECORATION,
				ThuwumcraftPlacedFeatures.NETHER_GEODE_PLACED.getKey().get());
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.BASALT_DELTAS),
				GenerationStep.Feature.UNDERGROUND_DECORATION,
				ThuwumcraftPlacedFeatures.BASALT_DELTA_GEODE_PLACED.getKey().get());
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
				GenerationStep.Feature.VEGETAL_DECORATION,
				ThuwumcraftPlacedFeatures.SILVERWOOD_TREE_PLACED.getKey().get());
	}
	
	private void registerStrippableBlocks()
	{
		StrippableBlockRegistry.register(ThuwumcraftBlocks.GREATWOOD_LOG, ThuwumcraftBlocks.STRIPPED_GREATWOOD_LOG);
		StrippableBlockRegistry.register(ThuwumcraftBlocks.GREATWOOD_WOOD, ThuwumcraftBlocks.STRIPPED_GREATWOOD_WOOD);
		StrippableBlockRegistry.register(ThuwumcraftBlocks.SILVERWOOD_LOG, ThuwumcraftBlocks.STRIPPED_SILVERWOOD_LOG);
		StrippableBlockRegistry.register(ThuwumcraftBlocks.SILVERWOOD_WOOD, ThuwumcraftBlocks.STRIPPED_SILVERWOOD_WOOD);
	}

	private static void registerMultiBlocks()
	{
		MultiBlockRegistry.TYPES.add(AlchemicalFurnaceType.INSTANCE);
	}

	private void registerWarpEvents()
	{
		PlayerWarpEvents.register(10, (player, warp) -> {
			if(warp.getTotalWarp() > 0)
			{
				player.sendMessage(new TranslatableText("warp_event.thuwumcraft.watching").formatted(Formatting.DARK_PURPLE, Formatting.ITALIC), true);
				return ActionResult.SUCCESS;
			}
			return ActionResult.PASS;
		});
		PlayerWarpEvents.register(10, ((player, warp) -> {
			if(warp.getTotalWarp() > 0 && !player.world.isClient)
			{
				player.sendMessage(new TranslatableText("spiders").formatted(Formatting.DARK_PURPLE, Formatting.ITALIC), true);
				MindSpider spider = new MindSpider(player.world, player.getGameProfile().getId());
				spider.setPosition(player.getX(), player.getY(), player.getZ());
				player.world.spawnEntity(spider);
				return ActionResult.SUCCESS;
			}
			return ActionResult.PASS;
		}));
	}

	@Override
	public void onInitialize()
	{
		Aspects.register();
		registerAspects();
		ThuwumcraftFluids.register();
		ThuwumcraftBlocks.register();
		ThuwumcraftItems.register();
		ThuwumcraftBlockEntities.register();
		ThuwumcraftStatusEffects.register();
		ThuwumcraftEntities.register();
		ThuwumcraftRecipes.register();
		ThuwumcraftParticles.register();
		registerStrippableBlocks();
		registerEvents();
		registerSounds();
		registerMultiBlocks();
		registerAbilities();
		registerNetwork();
		ThuwumcraftTrunkTypes.register();
		ThuwumcraftFeatures.register();
		ThuwumcraftConfiguredFeatures.register();
		ThuwumcraftPlacedFeatures.register();
		ThuwumcraftEntityTags.register();
		ThuwumcraftBiomes.register();
//		ThuwumcraftStructurePieceTypes.register();
//		ThuwumcraftStructureFeatures.register();
		registerLookup();
		registerReloadListeners();
		registerBiomeModifications();
		VillageAdditions.register();
		registerWarpEvents();
		ThuwumcraftSpells.register();
		ThuwumcraftSpellData.register();
	}
}
