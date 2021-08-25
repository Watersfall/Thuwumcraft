package net.watersfall.thuwumcraft.item.tool;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.api.abilities.item.BerserkerWeapon;
import net.watersfall.thuwumcraft.api.item.BeforeActions;
import net.watersfall.thuwumcraft.registry.ThuwumcraftStatusEffects;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SpecialBattleaxeItem extends OpenAxeItem implements BeforeActions
{
	public SpecialBattleaxeItem()
	{
		super(AlchemyToolMaterials.MAGIC, 9, -3.2F, new FabricItemSettings().maxCount(1).group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP));
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		if(attacker instanceof PlayerEntity player)
		{
			if(target.isDead())
			{
				if(target.getType() == EntityType.WITHER_SKELETON && !player.getAbilities().creativeMode)
				{
					target.dropItem(Items.WITHER_SKELETON_SKULL);
				}
				AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
				provider.getAbility(BerserkerWeapon.ID, BerserkerWeapon.class).ifPresent(ability -> ability.addExperience((int)target.getMaxHealth()));
			}
		}
		return super.postHit(stack, target, attacker);
	}

	@Override
	public void onCraft(ItemStack stack, World world, PlayerEntity player)
	{
		if(stack.getNbt() != null && stack.getNbt().contains("RepairCost"))
		{
			stack.getNbt().putInt("RepairCost", 0);
		}
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		if(!user.hasStatusEffect(ThuwumcraftStatusEffects.BERSERK))
		{
			AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(user.getStackInHand(hand));
			Optional<BerserkerWeapon> optional = provider.getAbility(BerserkerWeapon.ID, BerserkerWeapon.class);
			if(optional.isPresent())
			{
				BerserkerWeapon weapon = optional.get();
				if(weapon.getCurrentLevel() >= 0)
				{
					user.setCurrentHand(hand);
					return TypedActionResult.success(user.getStackInHand(hand));
				}
			}
		}
		return TypedActionResult.pass(user.getStackInHand(hand));
	}

	@Override
	public int getMaxUseTime(ItemStack stack)
	{
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		Optional<BerserkerWeapon> optional = provider.getAbility(BerserkerWeapon.ID, BerserkerWeapon.class);
		if(optional.isPresent())
		{
			return (1 + optional.get().getCurrentLevel()) * 20;
		}
		return 0;
	}

	@Override
	public UseAction getUseAction(ItemStack stack)
	{
		return UseAction.BOW;
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user)
	{
		if(!world.isClient)
		{
			AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
			provider.getAbility(BerserkerWeapon.ID, BerserkerWeapon.class).ifPresent(ability -> {
				int level = ability.getCurrentLevel();
				user.addStatusEffect(new StatusEffectInstance(ThuwumcraftStatusEffects.BERSERK, 120 * 20, level));
				ability.setExperience(ability.getExperience() - ability.getLevelCost(level));
			});
		}
		return stack;
	}

	public float getLevel(ItemStack stack)
	{
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		Optional<BerserkerWeapon> optional = provider.getAbility(BerserkerWeapon.ID, BerserkerWeapon.class);
		if(optional.isPresent())
		{
			return (float)(optional.get().getCurrentLevel() + 1) / ((float)optional.get().getMaxLevel() + 1);
		}
		return 0;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
	{
		super.appendTooltip(stack, world, tooltip, context);
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		provider.getAbility(BerserkerWeapon.ID, BerserkerWeapon.class).ifPresent(ability -> {
			tooltip.add(new LiteralText("Souls: " + ability.getExperience() + "/" + ability.getLevelCost(ability.getCurrentLevel() + 1)).formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
			if(ability.getCurrentLevel() >= 0)
			{
				tooltip.add(new LiteralText("Level " + (ability.getCurrentLevel() + 1)).formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
			}
		});
	}
}
