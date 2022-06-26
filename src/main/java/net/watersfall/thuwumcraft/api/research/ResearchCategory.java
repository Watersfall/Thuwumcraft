package net.watersfall.thuwumcraft.api.research;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;

import java.util.List;

public interface ResearchCategory
{
	Identifier getId();
	Text getName();
	ItemStack getIconStack();
	int getIndex();
	List<Identifier> visibilityRequirements();
	void toPacket(PacketByteBuf buf);
	void fromPacket(Identifier id, PacketByteBuf buf);
	boolean isVisible(PlayerResearchAbility ability);
}
