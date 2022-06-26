package net.watersfall.thuwumcraft.research;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.research.ResearchCategory;

import java.util.ArrayList;
import java.util.List;

public class ResearchCategoryImpl implements ResearchCategory
{
	private Identifier id;
	private Text name;
	private ItemStack icon;
	private int index;
	private List<Identifier> visiblityRequirements;

	public ResearchCategoryImpl(Identifier id, JsonObject json)
	{
		this.id = new Identifier(id.getNamespace(), id.getPath().replace("research_category/", "").replace(".json", "").replace("/", "."));
		this.name = new TranslatableText("research_category." + this.id.getNamespace() + "." + this.id.getPath());
		this.icon = new ItemStack(JsonHelper.getItem(json, "icon", Items.PORKCHOP), 1);
		this.index = JsonHelper.getInt(json, "index", Integer.MAX_VALUE);
		JsonArray array = JsonHelper.getArray(json, "required_research", new JsonArray());
		visiblityRequirements = new ArrayList<>();
		for(int i = 0; i < array.size(); i++)
		{
			visiblityRequirements.add(Identifier.tryParse(array.get(i).getAsString()));
		}
	}

	public ResearchCategoryImpl(Identifier id, PacketByteBuf buf)
	{
		this.fromPacket(id, buf);
	}

	@Override
	public boolean isVisible(PlayerResearchAbility ability)
	{
		for(Identifier id : this.visiblityRequirements)
		{
			if(!ability.hasResearch(id))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public Identifier getId()
	{
		return id;
	}

	@Override
	public Text getName()
	{
		return name;
	}

	@Override
	public ItemStack getIconStack()
	{
		return icon;
	}

	@Override
	public int getIndex()
	{
		return index;
	}

	@Override
	public List<Identifier> visibilityRequirements()
	{
		return visiblityRequirements;
	}

	@Override
	public void toPacket(PacketByteBuf buf)
	{
		buf.writeIdentifier(id);
		buf.writeText(name);
		buf.writeItemStack(icon);
		buf.writeInt(index);
		buf.writeInt(visiblityRequirements.size());
		for(int i = 0; i < visiblityRequirements.size(); i++)
		{
			buf.writeIdentifier(visiblityRequirements.get(i));
		}
	}

	@Override
	public void fromPacket(Identifier id, PacketByteBuf buf)
	{
		this.id = id;
		this.name = buf.readText();
		this.icon = buf.readItemStack();
		this.index = buf.readInt();
		int size = buf.readInt();
		this.visiblityRequirements = new ArrayList<>();
		for(int i = 0; i < size; i++)
		{
			visiblityRequirements.add(buf.readIdentifier());
		}
	}
}
