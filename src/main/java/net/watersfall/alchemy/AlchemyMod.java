package net.watersfall.alchemy;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.abilities.entity.PlayerResearchAbilityImpl;
import net.watersfall.alchemy.abilities.entity.RunedShieldAbilityEntity;
import net.watersfall.alchemy.abilities.item.PhialStorageAbility;
import net.watersfall.alchemy.abilities.item.RunedShieldAbilityItem;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.api.multiblock.MultiBlockRegistry;
import net.watersfall.alchemy.api.research.Research;
import net.watersfall.alchemy.api.sound.AlchemySounds;
import net.watersfall.alchemy.block.AlchemyBlocks;
import net.watersfall.alchemy.block.entity.AlchemyBlockEntities;
import net.watersfall.alchemy.block.entity.PedestalEntity;
import net.watersfall.alchemy.effect.AlchemyStatusEffects;
import net.watersfall.alchemy.item.AlchemyItems;
import net.watersfall.alchemy.item.SpecialPickaxeItem;
import net.watersfall.alchemy.multiblock.type.AlchemicalFurnaceType;
import net.watersfall.alchemy.screen.AlchemicalFurnaceHandler;
import net.watersfall.alchemy.screen.ApothecaryGuideHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.watersfall.alchemy.recipe.AlchemyRecipes;
import net.watersfall.alchemy.recipe.PedestalRecipe;
import net.watersfall.alchemy.screen.ResearchBookHandler;
import net.watersfall.alchemy.util.StatusEffectHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AlchemyMod implements ModInitializer
{
	public static final String MOD_ID = "waters_alchemy_mod";
	public static final ScreenHandlerType<ApothecaryGuideHandler> APOTHECARY_GUIDE_HANDLER;
	public static final ScreenHandlerType<AlchemicalFurnaceHandler> ALCHEMICAL_FURNACE_HANDLER;
	public static final ScreenHandlerType<ResearchBookHandler> RESEARCH_BOOK_HANDLER;
	private static Tag<Item> INGREDIENT_TAG;

	static
	{
		APOTHECARY_GUIDE_HANDLER = ScreenHandlerRegistry.registerSimple(getId("apothecary_guide_handler"), ApothecaryGuideHandler::new);
		ALCHEMICAL_FURNACE_HANDLER = ScreenHandlerRegistry.registerSimple(getId("alchemical_furnace_handler"), AlchemicalFurnaceHandler::new);
		RESEARCH_BOOK_HANDLER = ScreenHandlerRegistry.registerSimple(getId("guide_handler"), ResearchBookHandler::new);
		ServerPlayNetworking.registerGlobalReceiver(getId("research_click"), ((server, player, handler, buf, responseSender) -> {
			AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
			Optional<PlayerResearchAbility> optional = provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class);
			optional.ifPresent((ability) -> {
				Research research = Research.REGISTRY.get(buf.readIdentifier());
				if(!ability.getResearch().contains(research))
				{
					ability.addResearch(research.getId());
					responseSender.sendPacket(getId("research_click"), ability.toPacket(PacketByteBufs.create()));
				}
			});
		}));
	}

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

	private static void registerEvents()
	{
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if(!world.isClient)
			{
				if(!player.getStackInHand(hand).isEmpty())
				{
					if(player.getStackInHand(hand).getTag() != null)
					{
						CompoundTag tag = player.getStackInHand(hand).getTag();
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
			if((item == AlchemyItems.SPECIAL_PICKAXE_ITEM && (block instanceof OreBlock || block == Blocks.COPPER_ORE)) ||
					(item == AlchemyItems.SPECIAL_AXE_ITEM && state.isIn(BlockTags.LOGS)))
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
				if(entity.getType() == EntityType.PLAYER)
				{
					ServerPlayNetworking.send((ServerPlayerEntity)entity, AlchemyMod.getId("abilities_packet_player"), buf);
				}
				for(ServerPlayerEntity player : PlayerLookup.tracking(entity))
				{
					ServerPlayNetworking.send(player, getId("abilities_packet"), buf);
				}
			}
		});
	}

	private static void registerAspects()
	{
		Aspects.register(Aspects.AIR.getId(), Aspects.AIR);
		Aspects.register(Aspects.EARTH.getId(), Aspects.EARTH);
		Aspects.register(Aspects.WATER.getId(), Aspects.WATER);
		Aspects.register(Aspects.FIRE.getId(), Aspects.FIRE);
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
	}

	private static void registerMultiBlocks()
	{
		MultiBlockRegistry.TYPES.add(AlchemicalFurnaceType.INSTANCE);
	}

	@Override
	public void onInitialize()
	{
		AlchemyItems.register();
		AlchemyBlocks.register();
		AlchemyStatusEffects.register();
		AlchemyRecipes.register();
		AlchemyBlockEntities.register();
		registerEvents();
		registerAspects();
		registerSounds();
		registerMultiBlocks();
		registerAbilities();
		Research.REGISTRY.register(Research.TEST_RESEARCH.getId(), Research.TEST_RESEARCH);
		Research.REGISTRY.register(Research.TEST_RESEARCH_2.getId(), Research.TEST_RESEARCH_2);
		Research.REGISTRY.register(Research.TEST_RESEARCH_3.getId(), Research.TEST_RESEARCH_3);
		Research.TEST_RESEARCH_2.setRequirements(Research.TEST_RESEARCH);
	}
}
