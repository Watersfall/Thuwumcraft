package net.watersfall.thuwumcraft.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.api.abilities.item.WandFocusAbility;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.api.spell.SpellType;
import net.watersfall.thuwumcraft.block.entity.BlockEntityClientSerializable;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;
import net.watersfall.thuwumcraft.registry.ThuwumcraftScreenHandlers;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Potential Spell Systems
 *
 * 		Spells are created by piecing together pieces of magic in a
 * special workbench called a Focal Manipulator. This apparatus allows
 * a thaumaturge to fit together bits of magic inside of a crystal ball
 * that, when magic is forced through, can manifest as various magical
 * effects in the world. Beginner thaumaturges beware, magic is not
 * meant to be contained in such ways, and it fights back.
 *
 * Basically quark matrix enchanting or blokus, but with spells.
 * Potential Ideas:
 * 		- An AI will play against you, which limits what you can place.
 * 		  However, the more pieces the AI places down, the cheaper the
 * 		  spell is to cast. Maximizing how many pieces you place down,
 * 		  as well as how many the AI does is key to maximizing efficiency
 * 		- An AI will play against you, and the pieces it placed down will
 * 		  weaken your spell. Whether by cost or reducing stats. You can
 * 		  place more pieces at the risk of the AI placing more negative
 * 		  pieces.
 * 		- More advanced spell creation tools reduce the chance of negative
 * 		  pieces being present
 * 		- The AI will only be present in lower tier focus creation. More
 * 		  advanced focus creation will not have this present, and will
 * 		  just be about fitting pieces onto the board by yourself. However,
 * 		  the board will be smaller so not all pieces can be used
*/
public class FocalManipulatorHandler extends ScreenHandler
{
	private static final List<SpellType<?>> readSpells(PacketByteBuf buf)
	{
		int size = buf.readInt();
		List<SpellType<?>> spells = new ArrayList<>(size);
		for(int i = 0; i < size; i++)
		{
			Identifier id = buf.readIdentifier();
			SpellType<?> spell = ThuwumcraftRegistry.SPELL.get(id);
			spells.add(spell);
		}
		return spells;
	}

	private final List<SpellType<?>> unlockedSpells;

	public FocalManipulatorHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf)
	{
		this(syncId, playerInventory, new SimpleInventory(1), readSpells(buf));
	}

	public FocalManipulatorHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, List<SpellType<?>> unlockedSpells)
	{
		super(ThuwumcraftScreenHandlers.FOCAL_MANIPULATOR, syncId);

		this.unlockedSpells = unlockedSpells;

		this.addSlot(new Slot(inventory, 0, 8, 14){
			@Override
			public boolean canInsert(ItemStack stack)
			{
				if(stack.isOf(ThuwumcraftItems.WAND_FOCUS))
				{
					return !AbilityProvider.getAbility(stack, WandFocusAbility.ID, WandFocusAbility.class).isPresent();
				}
				return false;
			}

			@Override
			public void markDirty()
			{
				super.markDirty();
				if(this.inventory instanceof BlockEntityClientSerializable entity && !playerInventory.player.world.isClient)
				{
					entity.sync();
				}
			}
		});

		//Player Inventory
		for (int y = 0; y < 3; ++y)
		{
			for (int x = 0; x < 9; ++x)
			{
				this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 142 + y * 18));
			}
		}
		for (int y = 0; y < 9; ++y)
		{
			this.addSlot(new Slot(playerInventory, y, 8 + y * 18, 200));
		}
	}

	public List<SpellType<?>> getUnlockedSpells()
	{
		return unlockedSpells;
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return true;
	}
}
