package net.watersfall.thuwumcraft.hooks;

import net.minecraft.advancement.Advancement;
import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.aspect.Aspects;
import net.watersfall.thuwumcraft.block.AbstractCauldronBlock;
import net.watersfall.thuwumcraft.recipe.ResearchRequiredCraftingRecipe;
import net.watersfall.thuwumcraft.registry.ThuwumcraftAttributes;
import net.watersfall.thuwumcraft.registry.ThuwumcraftStatusEffects;
import net.watersfall.wet.api.abilities.AbilityProvider;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Hooks
{
	private static BlockState[] fireCrystals = null;
	private static BlockState[] waterCrystals = null;

	public static void modifyAnvilOutput(PlayerEntity player, ItemStack stack, CallbackInfo info)
	{
		stack.onCraft(player.getEntityWorld(), player, stack.getCount());
	}

	public static void cancelCampfireSmokeParticles(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke, CallbackInfo info)
	{
		Block block = world.getBlockState(pos.up()).getBlock();
		if(block instanceof AbstractCauldronBlock)
		{
			info.cancel();
		}
	}

	public static void checkCraftingResearch(ScreenHandler handler, World world, ServerPlayerEntity player, Optional<CraftingRecipe> recipeOptional, ItemStack stack, CraftingInventory inventory, CraftingResultInventory resultInventory, CallbackInfo info)
	{
		if(recipeOptional.isPresent() && recipeOptional.get() instanceof ResearchRequiredCraftingRecipe recipe)
		{
			AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
			Optional<PlayerResearchAbility> optional = provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class);
			if(optional.isPresent())
			{
				if (resultInventory.shouldCraftRecipe(world, player, recipeOptional.get()) && recipe.matches(inventory, world, optional.get()))
				{
					stack = recipeOptional.get().craft(inventory);
					resultInventory.setStack(0, stack);
					handler.setPreviousTrackedSlot(0, stack);
					player.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, stack));
				}
			}
			info.cancel();
		}
	}

	public static void addLavaCrystals(FeatureContext<SingleStateFeatureConfig> context, BlockPos pos, int x, int y, int z, Random random)
	{
		StructureWorldAccess world = context.getWorld();
		if((context.getWorld().getLunarTime() & 0x100) == 0)
		{
			for(Direction direction : Direction.values())
			{
				BlockPos currentPos = pos.add(x, y, z).offset(direction);
				if(world.isAir(currentPos) && !bordersFluid(world, currentPos, Fluids.LAVA) && random.nextInt(10) == 0)
				{
					world.setBlockState(currentPos, getFireCrystal(random, direction), 2);
				}
			}
		}
	}

	public static void addWaterCrystals(StructureWorldAccess world, BlockPos pos, int x, int z, Random random)
	{
		if((world.getLunarTime() & 0x10) == 0)
		{
			for(int y = 0; y < 4; y++)
			{
				BlockPos currentPos = pos.add(x, y, z);
				FluidState fluid = world.getFluidState(currentPos);
				if(fluid.getFluid() == Fluids.WATER)
				{
					for(Direction direction : Direction.values())
					{
						BlockState state = world.getBlockState(currentPos.offset(direction));
						if(random.nextInt(10) == 0 && state.getMaterial().isSolid() && state.getMaterial() != Material.AMETHYST)
						{
							world.setBlockState(currentPos, getWaterCrystal(random, direction.getOpposite()), 2);
						}
					}
				}
			}
		}
	}

	public static void livingEntityTick(LivingEntity entity)
	{
		World world = entity.getEntityWorld();
		if(!world.isClient)
		{
			if(entity.hasStatusEffect(ThuwumcraftStatusEffects.PROJECTILE_SHIELD))
			{
				List<ArrowEntity> entities = world.getEntitiesByType(EntityType.ARROW, entity.getBoundingBox().expand(3), arrow -> test(arrow, entity));
				for(int i = 0; i < entities.size(); i++)
				{
					Vec3d velocity = entities.get(i).getVelocity();
					entities.get(i).setOwner(entity);
					entities.get(i).setVelocity(velocity.x * -0.1, velocity.y * -0.1, velocity.z * -0.1);
				}
			}
			else if(entity.hasStatusEffect(ThuwumcraftStatusEffects.PROJECTILE_ATTRACTION))
			{
				List<ArrowEntity> entities = world.getEntitiesByType(EntityType.ARROW, entity.getBoundingBox().expand(3), arrow -> test(arrow, entity));
				for(int i = 0; i < entities.size(); i++)
				{
					ArrowEntity arrow = entities.get(i);
					Vec3d vec = arrow.getPos().subtract(entity.getPos()).normalize();
					Vec3d vel = arrow.getVelocity();
					float speed = (float) Math.sqrt(vel.x * vel.x + vel.y * vel.y + vel.z * vel.z);
					entities.get(i).setVelocity(-vec.x, -vec.y, -vec.z, speed, 0);
				}
			}
		}
	}

	public static void livingEntityAddStatusEffect(LivingEntity entity, StatusEffectInstance effect)
	{
		if(entity.getEntityWorld() instanceof ServerWorld serverWorld)
		{
			ServerChunkManager manager = serverWorld.getChunkManager();
			manager.sendToNearbyPlayers(entity, new EntityStatusEffectS2CPacket(entity.getId(), effect));
		}
	}

	public static void livingEntityRemoveStatusEffect(LivingEntity entity, StatusEffectInstance effect)
	{
		if(entity.getEntityWorld() instanceof ServerWorld serverWorld)
		{
			ServerChunkManager manager = serverWorld.getChunkManager();
			manager.sendToNearbyPlayers(entity, new RemoveEntityStatusEffectS2CPacket(entity.getId(), effect.getEffectType()));
		}
	}

	public static void livingEntityAddAttributeModifiers(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info)
	{
		info.getReturnValue().add(ThuwumcraftAttributes.MAGIC_RESISTANCE, 0);
	}

	public static float livingEntityApplyDamageModifiers(LivingEntity entity, DamageSource source, float amount)
	{
		if(source.isProjectile())
		{
			if(entity.hasStatusEffect(ThuwumcraftStatusEffects.PROJECTILE_RESISTANCE))
			{
				amount = amount * 0.5F;
			}
			if(entity.hasStatusEffect(ThuwumcraftStatusEffects.PROJECTILE_WEAKNESS))
			{
				amount = amount * 2.0F;
			}
		}
		if(source.isMagic())
		{
			double resistance = entity.getAttributeValue(ThuwumcraftAttributes.MAGIC_RESISTANCE) / 100;
			amount = amount * (float)(1 - resistance);
		}
		return amount;
	}

	public static void serverPlayerOnAdvancementComplete(ServerPlayerEntity player, Advancement advancement)
	{
		AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
		provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).ifPresent((ability -> {
			ability.addAdvancement(advancement.getId());
		}));
	}

	private static boolean test(ArrowEntity entity, LivingEntity living)
	{
		return entity != null && entity.getOwner() != null && living.getId() != entity.getOwner().getId();
	}

	private static boolean bordersFluid(StructureWorldAccess world, BlockPos pos, Fluid fluid)
	{
		return world.getFluidState(pos).getFluid() == fluid ||
				world.getFluidState(pos.up()).getFluid() == fluid ||
				world.getFluidState(pos.down()).getFluid() == fluid ||
				world.getFluidState(pos.north()).getFluid() == fluid ||
				world.getFluidState(pos.south()).getFluid() == fluid ||
				world.getFluidState(pos.east()).getFluid() == fluid ||
				world.getFluidState(pos.west()).getFluid() == fluid;
	}

	private static BlockState getFireCrystal(Random random, Direction direction)
	{
		if(fireCrystals == null)
		{
			fireCrystals = new BlockState[]{
					Aspects.ASPECT_TO_CLUSTER.get(Aspects.FIRE).getDefaultState(),
					Aspects.ASPECT_TO_LARGE_CLUSTER.get(Aspects.FIRE).getDefaultState(),
					Aspects.ASPECT_TO_MEDIUM_CLUSTER.get(Aspects.FIRE).getDefaultState(),
					Aspects.ASPECT_TO_SMALL_CLUSTER.get(Aspects.FIRE).getDefaultState()
			};
		}
		return fireCrystals[random.nextInt(fireCrystals.length)].with(AmethystClusterBlock.FACING, direction);
	}

	private static BlockState getWaterCrystal(Random random, Direction direction)
	{
		if(waterCrystals == null)
		{
			waterCrystals = new BlockState[]{
					Aspects.ASPECT_TO_CLUSTER.get(Aspects.WATER).getDefaultState(),
					Aspects.ASPECT_TO_LARGE_CLUSTER.get(Aspects.WATER).getDefaultState(),
					Aspects.ASPECT_TO_MEDIUM_CLUSTER.get(Aspects.WATER).getDefaultState(),
					Aspects.ASPECT_TO_SMALL_CLUSTER.get(Aspects.WATER).getDefaultState()
			};
		}
		return waterCrystals[random.nextInt(waterCrystals.length)].with(AmethystClusterBlock.FACING, direction).with(Properties.WATERLOGGED, true);
	}
}
