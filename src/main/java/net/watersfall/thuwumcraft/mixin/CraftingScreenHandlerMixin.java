package net.watersfall.thuwumcraft.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.recipe.ResearchRequiredCraftingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin
{
	@Inject(method = "updateResult",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/recipe/RecipeManager;getFirstMatch(Lnet/minecraft/recipe/RecipeType;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/world/World;)Ljava/util/Optional;"),
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true)
	private static void checkResearchCrafting(ScreenHandler handler,
											  World world,
											  PlayerEntity player,
											  CraftingInventory inventory,
											  CraftingResultInventory resultInventory,
											  CallbackInfo info,
											  ServerPlayerEntity serverPlayer,
											  ItemStack stack,
											  Optional<CraftingRecipe> optional)
	{
		if(optional.isPresent() && optional.get() instanceof ResearchRequiredCraftingRecipe)
		{
			if (resultInventory.shouldCraftRecipe(world, serverPlayer, optional.get()))
			{
				stack = optional.get().craft(inventory);
				resultInventory.setStack(0, stack);
				handler.setPreviousTrackedSlot(0, stack);
				serverPlayer.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, 0, stack));
				info.cancel();
			}
		}
	}
}
