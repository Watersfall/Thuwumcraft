package net.watersfall.thuwumcraft.api.research;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;

import java.util.*;

public class ResearchCategory
{
	private Identifier id;
	private Text name;
	private ItemStack icon;
	private int index;
	private List<Identifier> requiredResearch;

	public ResearchCategory(Identifier id, JsonObject json)
	{
		this.id = new Identifier(id.getNamespace(), id.getPath().replace("research_category/", "").replace(".json", "").replace("/", "."));
		this.name = new TranslatableText("research_category." + this.id.getNamespace() + "." + this.id.getPath() + ".name");
		this.icon = new ItemStack(JsonHelper.getItem(json, "icon"), 1);
		this.index = JsonHelper.getInt(json, "index", Integer.MAX_VALUE);
		JsonArray array = JsonHelper.getArray(json, "required_research");
		requiredResearch = new ArrayList<>();
		for(int i = 0; i < array.size(); i++)
		{
			requiredResearch.add(Identifier.tryParse(array.get(i).getAsString()));
		}
	}

	public ResearchCategory(PacketByteBuf buf)
	{
		this.fromPacket(buf);
	}

	public List<Research> getResearch()
	{
		List<Research> list = new ArrayList<>();
		ThuwumcraftRegistry.RESEARCH.values().forEach((research -> {
			if(research.getCategory() == this)
			{
				list.add(research);
			}
		}));
		return list;
	}

	public Identifier getId()
	{
		return this.id;
	}

	public Text getName()
	{
		return name;
	}

	public ItemStack getIcon()
	{
		return this.icon;
	}

	public int getIndex()
	{
		return this.index;
	}

	public boolean isVisible(PlayerResearchAbility ability)
	{
		for(Identifier id : this.requiredResearch)
		{
			if(!ability.hasResearch(id))
			{
				return false;
			}
		}
		return true;
	}

	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeIdentifier(this.id);
		buf.writeItemStack(this.icon);
		buf.writeInt(this.index);
		buf.writeInt(requiredResearch.size());
		for(int i = 0; i < requiredResearch.size(); i++)
		{
			buf.writeIdentifier(requiredResearch.get(i));
		}
		return buf;
	}

	public void fromPacket(PacketByteBuf buf)
	{
		this.id = buf.readIdentifier();
		this.name = new TranslatableText("research_category." + this.id.getNamespace() + "." + this.id.getPath() + ".name");
		this.icon = buf.readItemStack();
		this.index = buf.readInt();
		int size = buf.readInt();
		this.requiredResearch = new ArrayList<>();
		for(int i = 0; i < size; i++)
		{
			requiredResearch.add(buf.readIdentifier());
		}
	}

	public static PacketByteBuf toFullPacket(PacketByteBuf buf)
	{
		buf.writeInt(ThuwumcraftRegistry.RESEARCH_CATEGORY.values().size());
		ThuwumcraftRegistry.RESEARCH_CATEGORY.values().forEach((category -> {
			category.toPacket(buf);
		}));
		return buf;
	}

	public static void fromFullPacket(PacketByteBuf buf)
	{
		ThuwumcraftRegistry.RESEARCH_CATEGORY.clear();
		int size = buf.readInt();
		for(int i = 0; i < size; i++)
		{
			ResearchCategory category = new ResearchCategory(buf);
			ThuwumcraftRegistry.RESEARCH_CATEGORY.register(category.getId(), category);
		}
	}
}
