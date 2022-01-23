package net.watersfall.thuwumcraft.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.abilities.item.WandAbility;
import net.watersfall.thuwumcraft.gui.WandWorkbenchHandler;
import net.watersfall.wet.api.abilities.AbilityProvider;

public class WandWorkbenchScreen extends HandledScreen<WandWorkbenchHandler>
{
	private static final Identifier BACKGROUND_TEXTURE = Thuwumcraft.getId("textures/gui/container/wand_workbench.png");
	private static final TranslatableText WAND_CORE = new TranslatableText("item.thuwumcraft.wand.core");
	private static final TranslatableText WAND_CAP = new TranslatableText("item.thuwumcraft.wand.cap");
	private static final TranslatableText WAND_RECHARGE_TYPE = new TranslatableText("item.thuwumcraft.wand_cap.recharge_type");
	private static final TranslatableText WAND_VIS_CAPACITY = new TranslatableText("item.thuwumcraft.wand_core.vis_capacity");
	private static final TranslatableText WAND_SPELL = new TranslatableText("item.thuwumcraft.wand_focus");
	private static final TranslatableText WAND_SPELL_NONE = new TranslatableText("item.thuwumcraft.wand.spell.none");
	private final TextRenderer text;

	public WandWorkbenchScreen(WandWorkbenchHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		text = MinecraftClient.getInstance().textRenderer;
	}

	@Override
	protected void init()
	{
		this.backgroundWidth = 176;
		this.backgroundHeight = 180;
		this.playerInventoryTitleY = 87;
		super.init();
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		renderBackground(matrices);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		drawTexture(matrices, x, y, 0, 0, 256, backgroundHeight, 256, 256);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
		ItemStack stack = handler.getSlot(0).getStack();
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		provider.getAbility(WandAbility.ID, WandAbility.class).ifPresent(wand -> {
			if(wand.getWandCore() == null)
			{
				text.draw(matrices, WAND_CORE.copy().append(": ").append(new TranslatableText("item.thuwumcraft.wand.core.none")), this.x + 90, this.y + 20, 0);
			}
			else
			{
				text.draw(matrices, WAND_CORE.copy().append(": ").append(wand.getWandCore().getId().toString()), this.x + 90, this.y + 20, 0);
				text.draw(matrices, WAND_VIS_CAPACITY.copy().append(": " + wand.getWandCore().getMaxVis()), this.x + 90, this.y + 38, 0);
			}
			if(wand.getWandCap() == null)
			{
				text.draw(matrices, WAND_CAP.copy().append(": ").append(new TranslatableText("item.thuwumcraft.wand.cap.none")), this.x + 90, this.y + 29, 0);
			}
			else
			{
				text.draw(matrices, WAND_CAP.copy().append(": ").append(wand.getWandCap().getId().toString()), this.x + 90, this.y + 29, 0);
				text.draw(matrices, WAND_RECHARGE_TYPE.copy().append(": " + wand.getWandCap().getRechargeType().name()), this.x + 90, this.y + 47, 0);
			}
			if(wand.getSpell() == null || wand.getSpell() == null)
			{
				text.draw(matrices, WAND_SPELL.copy().append(": ").append(WAND_SPELL_NONE), this.x + 90, this.y + 56, 0);
			}
			else
			{
				text.draw(matrices, WAND_SPELL.copy().append(": ").append(wand.getSpell().getName()), this.x + 90, this.y + 56, 0);
			}
		});
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
}
