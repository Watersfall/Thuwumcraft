package net.watersfall.thuwumcraft.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.abilities.item.WandFocusAbility;
import net.watersfall.thuwumcraft.client.ThuwumcraftClient;
import net.watersfall.thuwumcraft.client.gui.element.RemoveWandFocusElement;
import net.watersfall.thuwumcraft.client.gui.element.TooltipElement;
import net.watersfall.thuwumcraft.client.gui.element.WandFocusElement;
import net.watersfall.thuwumcraft.item.wand.WandFocusItem;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FocusChangeScreen extends Screen
{
	public static final Identifier TEXTURE = Thuwumcraft.getId("textures/gui/focus_change.png");
	private static final int size = 128;

	private int x;
	private int y;
	private final ItemStack stack;
	private final PlayerEntity player;
	private final Map<Integer, ItemStack> stacks;

	public FocusChangeScreen(ItemStack stack)
	{
		super(new LiteralText(""));
		this.stack = stack;
		this.player = MinecraftClient.getInstance().player;
		stacks = new HashMap<>();
		for(int i = 0; i < player.getInventory().size(); i++)
		{
			ItemStack current = player.getInventory().getStack(i);
			if(current != stack && current.getItem() instanceof WandFocusItem)
			{
				if(AbilityProvider.getProvider(current).getAbility(WandFocusAbility.ID, WandFocusAbility.class).isPresent())
				{
					stacks.put(i, current);
				}
			}
		}
	}

	@Override
	protected void init()
	{
		this.x = (width - size) / 2;
		this.y = (height - size) / 2;
		Point origin = new Point(x + 56, y + 56);
		int total = stacks.size();
		int o = 0;
		for(Integer integer : stacks.keySet())
		{
			o++;
			double angle = Math.PI * 2  / (total) * o;
			int circleX = origin.x + (int)(52 * Math.cos(angle - Math.PI / 2));
			int circleY = origin.y + (int)(52 * Math.sin(angle - Math.PI / 2));
			this.addDrawableChild(new WandFocusElement(stacks.get(integer), circleX, circleY, integer));
		}
		this.addDrawableChild(new RemoveWandFocusElement(origin.x, origin.y));
	}

	@Override
	public void renderBackground(MatrixStack matrices)
	{
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		drawTexture(matrices, x, y, 0, 0, size, size, size, size);
		RenderSystem.disableBlend();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.children().forEach(element -> {
			if(element instanceof Drawable drawable)
			{
				drawable.render(matrices, mouseX, mouseY, delta);
			}
			if(element instanceof TooltipElement tooltip)
			{
				if(element.isMouseOver(mouseX, mouseY))
				{
					this.renderTooltip(matrices, tooltip.getTooltip(mouseX, mouseY), mouseX, mouseY);
				}
			}
		});
	}

	@Override
	public boolean shouldPause()
	{
		return false;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		this.children().forEach(element -> {
			if(element.isMouseOver(mouseX, mouseY))
			{
				element.mouseClicked(mouseX, mouseY, button);
			}
		});
		this.close();
		return true;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if(!super.keyPressed(keyCode, scanCode, modifiers))
		{
			if(ThuwumcraftClient.WAND_FOCUS_KEY.matchesKey(keyCode, scanCode))
			{
				this.close();
				ThuwumcraftClient.wandFocusKeyPressed = true;
			}
		}
		return true;
	}
}
