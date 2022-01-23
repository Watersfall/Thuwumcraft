package net.watersfall.thuwumcraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.abilities.item.WandFocusAbilityImpl;
import net.watersfall.thuwumcraft.api.abilities.item.WandFocusAbility;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.client.gui.button.ItemStackButton;
import net.watersfall.thuwumcraft.gui.FocalManipulatorHandler;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;
import net.watersfall.thuwumcraft.spell.Spell;
import net.watersfall.thuwumcraft.spell.SpellType;
import net.watersfall.thuwumcraft.spell.modifier.SpellModifier;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FocalManipulatorScreen extends HandledScreen<FocalManipulatorHandler>
{
	private static final Identifier TEXTURE = Thuwumcraft.getId("textures/gui/container/focal_manipulator.png");

	private final List<ClickableWidget> currentModifierPage = new ArrayList<>();
	private final List<SpellModifier> currentSpellModifiers = new ArrayList<>();
	private int currentSpellModifiersIndex = 0;
	private boolean addedItem;
	private final Set<ItemStackButton> spellList;
	private ItemStack currentSpell;

	private final ButtonWidget.PressAction CREATE = button -> {
		AbilityProvider.getAbility(currentSpell, WandFocusAbility.ID, WandFocusAbility.class).ifPresent(ability -> {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeNbt(ability.toNbt(new NbtCompound(), currentSpell));
			ClientPlayNetworking.send(Thuwumcraft.getId("spell_create"), buf);
		});
	};

	private ButtonWidget createButton;
	private ButtonWidget nextModifier;
	private ButtonWidget previousModifier;

	private final ButtonWidget.PressAction SPELL_CLICK = check -> {
		if(check instanceof ItemStackButton button)
		{
			AbilityProvider.getAbility(button.getStack(), WandFocusAbility.ID, WandFocusAbility.class).ifPresent(ability -> {
				if(ability.getSpell() != null)
				{
					for(ClickableWidget element : currentModifierPage)
					{
						this.remove(element);
					}
					currentModifierPage.clear();
					currentSpellModifiers.clear();
					currentSpellModifiers.addAll(ability.getSpell().getModifiers().getModifiers());
					if(!currentSpellModifiers.isEmpty())
					{
						currentModifierPage.addAll(currentSpellModifiers.get(0).getGuiElements(this));
						for(ClickableWidget element : currentModifierPage)
						{
							this.addDrawableChild(element);
						}
					}
					setCurrentSpell(button.getStack());
					addDrawableChild(createButton);
					addDrawableChild(nextModifier);
					addDrawableChild(previousModifier);
				}
			});
		}
	};

	private final ButtonWidget.PressAction NEXT_MODIFIER = check -> {
		setCurrentSpellModifiersIndex(currentSpellModifiersIndex + 1);
		if(currentSpellModifiersIndex >= currentSpellModifiers.size())
		{
			setCurrentSpellModifiersIndex(0);
		}
		for(ClickableWidget element : currentModifierPage)
		{
			this.remove(element);
		}
		currentModifierPage.clear();
		if(!currentSpellModifiers.isEmpty())
		{
			currentModifierPage.addAll(currentSpellModifiers.get(currentSpellModifiersIndex).getGuiElements(this));
			for(ClickableWidget element : currentModifierPage)
			{
				this.addDrawableChild(element);
			}
		}
	};

	private final ButtonWidget.PressAction PREVIOUS_MODIFIER = check -> {
		setCurrentSpellModifiersIndex(currentSpellModifiersIndex - 1);
		if(currentSpellModifiersIndex <= 0)
		{
			setCurrentSpellModifiersIndex(currentSpellModifiers.size() - 1);
		}
		for(ClickableWidget element : currentModifierPage)
		{
			this.remove(element);
		}
		currentModifierPage.clear();
		if(!currentSpellModifiers.isEmpty())
		{
			currentModifierPage.addAll(currentSpellModifiers.get(currentSpellModifiersIndex).getGuiElements(this));
			for(ClickableWidget element : currentModifierPage)
			{
				this.addDrawableChild(element);
			}
		}
	};

	public FocalManipulatorScreen(FocalManipulatorHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		addedItem = false;
		spellList = new HashSet<>();
	}

	@Override
	protected void init()
	{
		this.backgroundWidth = 176;
		this.backgroundHeight = 224;
		this.playerInventoryTitleY = 130;
		super.init();
		TranslatableText text = new TranslatableText("create");
		createButton = new ButtonWidget(x - 12 + (backgroundWidth / 2), y + playerInventoryTitleY - 24, textRenderer.getWidth(text) + 12, 20, text, CREATE);
		nextModifier = new ButtonWidget(x + backgroundWidth - 24, y + playerInventoryTitleY - 24, 20, 20, new LiteralText(">"), NEXT_MODIFIER);
		previousModifier = new ButtonWidget(x + 36, y + playerInventoryTitleY - 24, 20, 20, new LiteralText("<"), PREVIOUS_MODIFIER);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		this.drawBackground(matrices, delta, mouseX, mouseY);
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		if(!this.currentSpellModifiers.isEmpty())
		{
			TranslatableText text = new TranslatableText(this.currentSpellModifiers.get(currentSpellModifiersIndex).getName());
			drawCenteredText(matrices, textRenderer, text, x + (backgroundWidth / 2) + 12, y + 12, 0xFFFFFF);
		}
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	protected void handledScreenTick()
	{
		super.handledScreenTick();
		ItemStack stack = handler.getSlot(0).getStack();
		if(stack.isEmpty() && addedItem)
		{
			addedItem = false;
			for(ItemStackButton button : spellList)
			{
				remove(button);
				for(ClickableWidget element : currentModifierPage)
				{
					this.remove(element);
				}
				currentModifierPage.clear();
				currentSpellModifiers.clear();
				currentSpellModifiersIndex = 0;
				this.remove(createButton);
				this.remove(nextModifier);
				this.remove(previousModifier);
			}
		}
		if(!addedItem && !stack.isEmpty())
		{
			addedItem = true;
			int y = 32 + this.y;
			int x = 8 + this.x;
			for(SpellType<?> type : ThuwumcraftRegistry.SPELL.values())
			{
				Spell<?> spell = type.create();
				ItemStack focus = new ItemStack(ThuwumcraftItems.WAND_FOCUS);
				WandFocusAbility ability = new WandFocusAbilityImpl(spell, focus);
				AbilityProvider.getProvider(focus).addAbility(ability);
				ItemStackButton button = new ItemStackButton(x, y, 16, 16, focus, SPELL_CLICK, ((button1, matrices, mouseX, mouseY) -> {
					ItemStack tooltip = ((ItemStackButton)button1).getStack();
					renderTooltip(matrices, tooltip, mouseX, mouseY);
				}));
				this.addDrawableChild(button);
				spellList.add(button);
				y += 16;
			}
		}
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getWidth()
	{
		return backgroundWidth;
	}

	public void setCurrentSpell(ItemStack currentSpell)
	{
		this.currentSpell = currentSpell;
	}

	public void setCurrentSpellModifiersIndex(int index)
	{
		currentSpellModifiersIndex = index;
	}
}
