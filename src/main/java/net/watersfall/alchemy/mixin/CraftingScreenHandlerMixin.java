package net.watersfall.alchemy.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.alchemy.recipe.ResearchRequiredCraftingRecipe;
import net.watersfall.alchemy.recipe.ResearchUnlockedShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin
{
	@Inject(method = "updateResult",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"),
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true)
	private static void checkResearchCrafting(ScreenHandler handler,
											  World world,
											  PlayerEntity player,
											  CraftingInventory inventory,
											  CraftingResultInventory resultInventory,
											  CallbackInfo info,
											  ServerPlayerEntity serverPlayer,
											  ItemStack stack)
	{
		if(stack == ItemStack.EMPTY)
		{
			List<CraftingRecipe> recipes = world.getServer().getRecipeManager().listAllOfType(RecipeType.CRAFTING);
			for(int i = 0; i < recipes.size(); i++)
			{
				if(recipes.get(i) instanceof ResearchRequiredCraftingRecipe)
				{
					ResearchRequiredCraftingRecipe recipe = (ResearchRequiredCraftingRecipe) recipes.get(i);
					if(recipe.matches(inventory, world, AbilityProvider.getProvider(serverPlayer).getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).get()))
					{
						stack = recipes.get(i).getOutput();
						break;
					}
				}
			}
			resultInventory.setStack(0, stack);
			handler.setPreviousTrackedSlot(0, stack);
			serverPlayer.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, 0, stack));
			info.cancel();
		}
	}
}
