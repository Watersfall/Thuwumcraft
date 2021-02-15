package net.watersfall.alchemy;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.watersfall.alchemy.block.AlchemyBlocks;
import net.watersfall.alchemy.block.entity.AlchemyBlockEntities;
import net.watersfall.alchemy.block.entity.PedestalEntity;
import net.watersfall.alchemy.effect.AlchemyStatusEffects;
import net.watersfall.alchemy.item.AlchemyItems;
import net.watersfall.alchemy.item.SpecialPickaxeItem;
import net.watersfall.alchemy.screen.AlchemicalFurnaceHandler;
import net.watersfall.alchemy.screen.ApothecaryGuideHandler;
import net.watersfall.alchemy.multiblock.MultiBlockRegistry;
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
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.watersfall.alchemy.recipe.AlchemyRecipes;
import net.watersfall.alchemy.recipe.PedestalRecipe;
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
	private static Tag<Item> INGREDIENT_TAG;

	static
	{
		APOTHECARY_GUIDE_HANDLER = ScreenHandlerRegistry.registerSimple(getId("apothecary_guide_handler"), ApothecaryGuideHandler::new);
		ALCHEMICAL_FURNACE_HANDLER = ScreenHandlerRegistry.registerSimple(getId("alchemical_furnace_handler"), AlchemicalFurnaceHandler::new);
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

	@Override
	public void onInitialize()
	{
		Registry.register(Registry.ITEM, getId("witchy_spoon"), AlchemyItems.WITCHY_SPOON_ITEM);
		Registry.register(Registry.ITEM, getId("throw_bottle"), AlchemyItems.THROW_BOTTLE);
		Registry.register(Registry.ITEM, getId("ladle"), AlchemyItems.LADLE_ITEM);
		Registry.register(Registry.ITEM, getId("apothecary_guide_book"), AlchemyItems.APOTHECARY_GUIDE);
		Registry.register(Registry.ITEM, getId("pedestal"), AlchemyItems.PEDESTAL_ITEM);
		Registry.register(Registry.ITEM, getId("magical_coal_0"), AlchemyItems.MAGICAL_COAL_TIER_0);
		Registry.register(Registry.ITEM, getId("magical_coal_1"), AlchemyItems.MAGICAL_COAL_TIER_1);
		Registry.register(Registry.ITEM, getId("magical_coal_2"), AlchemyItems.MAGICAL_COAL_TIER_2);
		Registry.register(Registry.ITEM, getId("magic_dust"), AlchemyItems.MAGIC_DUST);
		Registry.register(Registry.ITEM, getId("magic_pickaxe"), AlchemyItems.SPECIAL_PICKAXE_ITEM);
		Registry.register(Registry.ITEM, getId("magic_axe"), AlchemyItems.SPECIAL_AXE_ITEM);
		Registry.register(Registry.BLOCK, getId("brewing_cauldron"), AlchemyBlocks.BREWING_CAULDRON_BLOCK);
		Registry.register(Registry.BLOCK, getId("pedestal"), AlchemyBlocks.PEDESTAL_BLOCK);
		Registry.register(Registry.BLOCK, getId("alchemical_furnace"), AlchemyBlocks.ALCHEMICAL_FURNACE_BLOCK);
		Registry.register(Registry.BLOCK, getId("child_block"), AlchemyBlocks.CHILD_BLOCK);
		Registry.register(Registry.STATUS_EFFECT, getId("projectile_shield"), AlchemyStatusEffects.PROJECTILE_SHIELD);
		Registry.register(Registry.STATUS_EFFECT, getId("projectile_attraction"), AlchemyStatusEffects.PROJECTILE_ATTRACTION);
		Registry.register(Registry.STATUS_EFFECT, getId("projectile_weakness"), AlchemyStatusEffects.PROJECTILE_WEAKNESS);
		Registry.register(Registry.STATUS_EFFECT, getId("projectile_resistance"), AlchemyStatusEffects.PROJECTILE_RESISTANCE);
		Registry.register(Registry.RECIPE_TYPE, getId("cauldron_ingredient"), AlchemyRecipes.CAULDRON_INGREDIENTS);
		Registry.register(Registry.RECIPE_TYPE, getId("cauldron_recipe"), AlchemyRecipes.CAULDRON_INGREDIENT_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, getId("cauldron_item"), AlchemyRecipes.CAULDRON_ITEM_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, getId("pedestal_crafting"), AlchemyRecipes.PEDESTAL_RECIPE);
		Registry.register(Registry.RECIPE_TYPE, getId("grinding"), AlchemyRecipes.GRINDING_RECIPE);
		Registry.register(Registry.RECIPE_SERIALIZER, getId("cauldron_ingredient"), AlchemyRecipes.CAULDRON_INGREDIENTS_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, getId("cauldron_recipe"), AlchemyRecipes.CAULDRON_INGREDIENT_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, getId("cauldron_item"), AlchemyRecipes.CAULDRON_ITEM_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, getId("pedestal_crafting"), AlchemyRecipes.PEDESTAL_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, getId("grinding"), AlchemyRecipes.GRINDING_RECIPE_SERIALIZER);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, getId("brewing_cauldron_entity"), AlchemyBlockEntities.BREWING_CAULDRON_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, getId("pedestal_entity"), AlchemyBlockEntities.PEDESTAL_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, getId("alchemical_furnace_entity"), AlchemyBlockEntities.ALCHEMICAL_FURNACE_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, getId("child_block_entity"), AlchemyBlockEntities.CHILD_BLOCK_ENTITY);
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
		ServerTickEvents.END_SERVER_TICK.register(server -> MultiBlockRegistry.SERVER.tick());
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
	}
}
