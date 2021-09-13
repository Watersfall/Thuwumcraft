package net.watersfall.thuwumcraft.hooks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.ReadOnlyChunk;
import net.minecraft.world.chunk.WorldChunk;
import net.watersfall.thuwumcraft.abilities.chunk.VisAbilityImpl;
import net.watersfall.thuwumcraft.abilities.entity.PlayerResearchAbilityImpl;
import net.watersfall.thuwumcraft.abilities.entity.PlayerUnknownAbilityImpl;
import net.watersfall.thuwumcraft.abilities.item.BerserkerWeaponImpl;
import net.watersfall.thuwumcraft.abilities.item.RunedShieldAbilityItem;
import net.watersfall.thuwumcraft.abilities.item.WandAbilityImpl;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.item.tool.SpecialBattleaxeItem;
import net.watersfall.thuwumcraft.item.wand.WandItem;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class AbilityHooks
{
	public static void readChunkAbilities(CallbackInfoReturnable<ProtoChunk> info, NbtCompound tag)
	{
		AbilityProvider.getProvider(info.getReturnValue()).fromNbt(tag);
	}

	public static void writeChunkAbilities(CallbackInfoReturnable<NbtCompound> info, Chunk chunk)
	{
		if(!(chunk instanceof ProtoChunk) || (chunk instanceof ReadOnlyChunk))
		{
			info.getReturnValue().put("thuwumcraft:abilities", AbilityProvider.getProvider(chunk).toNbt(new NbtCompound()));
		}
	}

	public static void tickChuckAbilities(WorldChunk chunk)
	{
		AbilityProvider<Chunk> provider = AbilityProvider.getProvider(chunk);
		provider.tick(chunk);
	}

	public static void addChunkAbilities(AbilityProvider<Chunk> provider)
	{
		provider.addAbility(new VisAbilityImpl());
	}

	public static void readItemStackAbilities(NbtCompound tag, AbilityProvider<ItemStack> provider)
	{
		provider.fromNbt(tag);
	}

	public static void writeItemStackAbilities(NbtCompound tag, AbilityProvider<ItemStack> provider)
	{
		NbtCompound abilitiesTag = new NbtCompound();
		abilitiesTag = provider.toNbt(abilitiesTag);
		if(!abilitiesTag.isEmpty())
		{
			tag.put("thuwumcraft:abilities", abilitiesTag);
		}
	}

	public static void copyItemStackAbilities(ItemStack to, AbilityProvider<ItemStack> provider)
	{
		provider.copy(to, true);
	}
	
	public static void addItemStackAbilities(ItemConvertible item, AbilityProvider<ItemStack> provider)
	{
		if(item != null && item.asItem() == Items.NETHERITE_CHESTPLATE)
		{
			provider.addAbility(new RunedShieldAbilityItem(10, 10 ,10));
		}
		else if(item != null && item.asItem() instanceof WandItem)
		{
			provider.addAbility(new WandAbilityImpl());
		}
		else if(item != null && item.asItem() instanceof SpecialBattleaxeItem)
		{
			provider.addAbility(new BerserkerWeaponImpl());
		}
	}

	public static void addEntityAbilities(EntityType<? extends Entity> type, World world, AbilityProvider<Entity> provider)
	{
		if(type == EntityType.PLAYER)
		{
			if(!world.isClient)
			{
				provider.addAbility(new PlayerResearchAbilityImpl());
			}
			provider.addAbility(new PlayerUnknownAbilityImpl());
		}
	}

	public static void readEntityAbilities(NbtCompound tag, AbilityProvider<Entity> provider)
	{
		provider.fromNbt(tag);
	}

	public static NbtCompound writeEntityAbilities(NbtCompound tag, AbilityProvider<Entity> provider)
	{
		tag.put("thuwumcraft:abilities", provider.toNbt(new NbtCompound()));
		return tag;
	}

	public static void serverPlayerCopyAbilities(ServerPlayerEntity from, ServerPlayerEntity to, boolean alive)
	{
		AbilityProvider<Entity> provider = AbilityProvider.getProvider(from);
		provider.copy(to, alive);
	}
}
