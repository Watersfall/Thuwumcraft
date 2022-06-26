package net.watersfall.thuwumcraft.research;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.api.research.Research;
import net.watersfall.thuwumcraft.api.research.ResearchCategory;

public final class ResearchHelper
{
	private ResearchHelper(){}

	public static PacketByteBuf writeRegistryToPacket(PacketByteBuf buf)
	{
		buf.writeInt(ThuwumcraftRegistry.RESEARCH.values().size());
		ThuwumcraftRegistry.RESEARCH.values().forEach((value) -> {
			value.toPacket(buf);
		});
		return buf;
	}

	public static void readRegistryFromPacket(PacketByteBuf buf)
	{
		ThuwumcraftRegistry.RESEARCH.clear();
		int size = buf.readInt();
		for(int i = 0; i < size; i++)
		{
			Research research = new ResearchImpl(buf.readIdentifier(), buf);
			ThuwumcraftRegistry.RESEARCH.register(research.getId(), research);
		}
	}

	public static PacketByteBuf writeCategoriesToPacket(PacketByteBuf buf)
	{
		buf.writeInt(ThuwumcraftRegistry.RESEARCH_CATEGORY.values().size());
		ThuwumcraftRegistry.RESEARCH_CATEGORY.values().forEach((category -> {
			category.toPacket(buf);
		}));
		return buf;
	}

	public static void readCategoriesFromPacket(PacketByteBuf buf)
	{
		ThuwumcraftRegistry.RESEARCH_CATEGORY.clear();
		int size = buf.readInt();
		for(int i = 0; i < size; i++)
		{
			Identifier id = buf.readIdentifier();
			ResearchCategory category = new ResearchCategoryImpl(id, buf);
			ThuwumcraftRegistry.RESEARCH_CATEGORY.register(category.getId(), category);
		}
	}
}
