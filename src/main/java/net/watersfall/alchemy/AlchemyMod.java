package net.watersfall.alchemy;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
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
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.watersfall.alchemy.abilities.chunk.VisAbilityImpl;
import net.watersfall.alchemy.abilities.entity.PlayerResearchAbilityImpl;
import net.watersfall.alchemy.abilities.entity.RunedShieldAbilityEntity;
import net.watersfall.alchemy.abilities.item.PhialStorageAbility;
import net.watersfall.alchemy.abilities.item.RunedShieldAbilityItem;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.block.AspectContainer;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.api.multiblock.MultiBlockRegistry;
import net.watersfall.alchemy.api.research.Research;
import net.watersfall.alchemy.api.research.ResearchCategory;
import net.watersfall.alchemy.api.sound.AlchemySounds;
import net.watersfall.alchemy.api.tag.AlchemyEntityTags;
import net.watersfall.alchemy.block.entity.AlchemyBlockEntities;
import net.watersfall.alchemy.block.entity.PedestalEntity;
import net.watersfall.alchemy.effect.AlchemyStatusEffects;
import net.watersfall.alchemy.item.AlchemyItems;
import net.watersfall.alchemy.item.SpecialPickaxeItem;
import net.watersfall.alchemy.multiblock.type.AlchemicalFurnaceType;
import net.watersfall.alchemy.recipe.AlchemyRecipes;
import net.watersfall.alchemy.recipe.PedestalRecipe;
import net.watersfall.alchemy.research.ResearchCategoryLoader;
import net.watersfall.alchemy.research.ResearchLoader;
import net.watersfall.alchemy.util.StatusEffectHelper;
import net.watersfall.alchemy.world.biome.AlchemyBiomes;
import net.watersfall.alchemy.world.feature.AlchemyFeatures;
import net.watersfall.alchemy.world.village.VillageAdditions;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AlchemyMod implements ModInitializer
{
	public static final String MOD_ID = "waters_alchemy_mod";
	private static Tag<Item> INGREDIENT_TAG;
	public static final BlockApiLookup<AspectContainer, Direction> API = BlockApiLookup.get(AlchemyMod.getId("aspect_container"), AspectContainer.class, Direction.class);

	private static Set<Item> getAllIngredients(MinecraftServer server)
	{
		Set<Item> set = new HashSet<>();
		server.getRecipeManager().listAllOfType(AlchemyRecipes.CAULDRON_INGREDIENTS).forEach((item) -> set.add(item.getInput().getItem()));
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
	}

	private static void registerEvents()
	{
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if(!world.isClient)
			{
				if(!player.getStackInHand(hand).isEmpty())
				{
					if(player.getStackInHand(hand).getTag() != null)
					{
						NbtCompound tag = player.getStackInHand(hand).getTag();
						if(tag.contains(StatusEffectHelper.EFFECTS_LIST))
						{
							if(StatusEffectHelper.hasUses(tag))
							{
								List<StatusEffectInstance> effects = StatusEffectHelper.getEffectsFromTag(tag);
								if(effects.size() > 0)
								{
									effects.forEach(((LivingEntity)entity)::addStatusEffect);
								}
							}
							StatusEffectHelper.decrementUses(tag);
						}
					}
				}
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
		DispenserBlock.registerBehavior(AlchemyItems.WITCHY_SPOON_ITEM, ((pointer, stack) -> {
			Direction direction = pointer.getWorld().getBlockState(pointer.getBlockPos()).get(Properties.FACING);
			if(pointer.getWorld().getBlockEntity(pointer.getBlockPos().offset(direction)) instanceof PedestalEntity)
			{
				World world = pointer.getWorld();
				PedestalEntity entity = (PedestalEntity) world.getBlockEntity(pointer.getBlockPos().offset(direction));
				Optional<PedestalRecipe> recipeOptional = pointer.getWorld().getRecipeManager().getFirstMatch(AlchemyRecipes.PEDESTAL_RECIPE, entity, world);
				recipeOptional.ifPresent(entity::beginCraft);
			}
			return stack;
		}));
		PlayerBlockBreakEvents.BEFORE.register(((world, player, pos, state, blockEntity) -> {
			Block block = state.getBlock();
			ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
			Item item = stack.getItem();
			if(item == AlchemyItems.SPECIAL_PICKAXE_ITEM && (block instanceof OreBlock)
					|| (item == AlchemyItems.SPECIAL_AXE_ITEM && state.isIn(BlockTags.LOGS)))
			{
				BlockPos breakPos = SpecialPickaxeItem.getFurthestOre(world, state.getBlock(), pos);
				if(breakPos.equals(pos))
				{
					return true;
				}
				else
				{
					world.breakBlock(breakPos, false, player);
					Block.dropStacks(state, world, pos, blockEntity, player, stack);
					return false;
				}
			}
			return true;
		}));
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if(entity instanceof AbilityProvider)
			{
				AbilityProvider<Entity> provider = (AbilityProvider<Entity>)entity;
				PacketByteBuf buf = PacketByteBufs.create();
				provider.toPacket(buf);
				for(ServerPlayerEntity player : PlayerLookup.tracking(entity))
				{
					ServerPlayNetworking.send(player, getId("abilities_packet"), buf);
				}
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
			if(id.getPath().startsWith("entities/"))
			{
				String[] path = id.getPath().split("/");
				Optional<EntityType<?>> optional = EntityType.get(path[path.length - 1]);
				if(optional.isPresent())
				{
					generateNecromancyDrop(AlchemyItems.NECROMANCY_SKULL, supplier, AlchemyEntityTags.DROPS_HEAD);
					generateNecromancyDrop(AlchemyItems.NECROMANCY_ARM, supplier, AlchemyEntityTags.DROPS_ARM);
					generateNecromancyDrop(AlchemyItems.NECROMANCY_LEG, supplier, AlchemyEntityTags.DROPS_LEG);
					generateNecromancyDrop(AlchemyItems.NECROMANCY_HEART, supplier, AlchemyEntityTags.DROPS_HEART);
					generateNecromancyDrop(AlchemyItems.NECROMANCY_RIBCAGE, supplier, AlchemyEntityTags.DROPS_RIBCAGE);
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

	private static void registerSounds()
	{
		Registry.register(Registry.SOUND_EVENT, getId("block.cauldron.add_ingredient"), AlchemySounds.CAULDRON_ADD_INGREDIENT);
		Registry.register(Registry.SOUND_EVENT, getId("block.crucible.create"), AlchemySounds.USE_DUST_SOUND);
		Registry.register(Registry.SOUND_EVENT, getId("block.cauldron.bubble"), AlchemySounds.BUBBLE_SOUND);
		Registry.register(Registry.SOUND_EVENT, getId("item.research_book.open"), AlchemySounds.BOOK_OPEN_SOUND);
	}

	private static void registerAbilities()
	{
		AbilityProvider.ENTITY_REGISTRY.register(getId("runed_shield_ability"), RunedShieldAbilityEntity::new);
		AbilityProvider.ENTITY_REGISTRY.registerPacket(getId("runed_shield_ability"), RunedShieldAbilityEntity::new);
		AbilityProvider.ITEM_REGISTRY.register(getId("runed_shield_ability"), RunedShieldAbilityItem::new);
		AbilityProvider.ITEM_REGISTRY.register(getId("aspect_storage_ability"), PhialStorageAbility::new);
		AbilityProvider.ENTITY_REGISTRY.register(getId("player_research_ability"), PlayerResearchAbilityImpl::new);
		AbilityProvider.ENTITY_REGISTRY.registerPacket(getId("player_research_ability"), PlayerResearchAbilityImpl::new);
		AbilityProvider.CHUNK_REGISTRY.register(getId("vis_ability"), VisAbilityImpl::new);
	}

	private static void registerMultiBlocks()
	{
		MultiBlockRegistry.TYPES.add(AlchemicalFurnaceType.INSTANCE);
	}

	@Override
	public void onInitialize()
	{
		AlchemyStatusEffects.register();
		AlchemyRecipes.register();
		AlchemyBlockEntities.register();
		registerEvents();
		registerAspects();
		registerSounds();
		registerMultiBlocks();
		registerAbilities();
		registerNetwork();
		AlchemyFeatures.register();
		AlchemyEntityTags.register();
		AlchemyBiomes.register();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ResearchCategoryLoader());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ResearchLoader());
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
				GenerationStep.Feature.UNDERGROUND_DECORATION,
				BuiltinRegistries.CONFIGURED_FEATURE.getKey(AlchemyFeatures.EARTH_CRYSTAL_GEODE).get());
		BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(),
				GenerationStep.Feature.UNDERGROUND_DECORATION,
				BuiltinRegistries.CONFIGURED_FEATURE.getKey(AlchemyFeatures.NETHER_GEODE).get());
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.BASALT_DELTAS),
				GenerationStep.Feature.UNDERGROUND_DECORATION,
				BuiltinRegistries.CONFIGURED_FEATURE.getKey(AlchemyFeatures.BASALT_DELTA_GEODE).get());
		VillageAdditions.register();
	}
}
