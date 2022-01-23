package net.watersfall.thuwumcraft.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.Chunk;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.abilities.chunk.VisAbility;
import net.watersfall.thuwumcraft.block.entity.AspectCraftingEntity;
import net.watersfall.thuwumcraft.gui.AspectCraftingHandler;
import net.watersfall.thuwumcraft.recipe.AspectCraftingShapedRecipe;
import net.watersfall.wet.api.abilities.AbilityProvider;

public class AspectCraftingScreen extends HandledScreen<AspectCraftingHandler>
{
	private static final Identifier TEXTURE = Thuwumcraft.getId("textures/gui/container/aspect_crafting_table.png");
	private final AspectCraftingEntity entity;
	private final AbilityProvider<Chunk> provider;

	public AspectCraftingScreen(AspectCraftingHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		this.entity = handler.entity;
		provider = AbilityProvider.getProvider(this.entity.getWorld().getChunk(this.entity.getPos()));
	}

	@Override
	protected void init()
	{
		this.backgroundWidth = 175;
		this.backgroundHeight = 201;
		this.playerInventoryTitleY = 108;
		super.init();
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		renderBackground(matrices);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		super.render(matrices, mouseX, mouseY, delta);
		VisAbility ability = provider.getAbility(VisAbility.ID, VisAbility.class).get();
		matrices.push();
		matrices.scale(0.5F, 0.5F, 1F);
		this.textRenderer.draw(matrices, new LiteralText(ability.getVis() + " Vis Available"), (this.x + 116) * 2, (y + 36) * 2, -1);
		if(handler.currentRecipe != null && handler.currentRecipe instanceof AspectCraftingShapedRecipe && handler.canTakeOutput())
		{
			AspectCraftingShapedRecipe recipe = (AspectCraftingShapedRecipe)handler.currentRecipe;
			int color = ability.getVis() >= recipe.getVis() ? 0x00FF00 : 0xFF0000;
			this.textRenderer.draw(matrices, new LiteralText(recipe.getVis() + " Vis Required"), (this.x + 116) * 2, (y + 84) * 2, color);
		}
		matrices.pop();
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
}
