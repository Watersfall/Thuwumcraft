package net.watersfall.alchemy.api.research;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ResearchCategory
{
	public static final Registry REGISTRY = new Registry();

	private Identifier id;
	private Text name;
	private ItemStack icon;

	public ResearchCategory(Identifier id, JsonObject json)
	{
		this.id = new Identifier(id.getNamespace(), id.getPath().replace("research_category/", "").replace(".json", "").replace("/", "."));
		this.name = new TranslatableText("research_category." + this.id.getNamespace() + "." + this.id.getPath() + ".name");
		this.icon = new ItemStack(JsonHelper.getItem(json, "icon"), 1);
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

	public static class Registry
	{
		private final Map<Identifier, ResearchCategory> categories;

		private Registry()
		{
			this.categories = new HashMap<>();
		}

		public void register(Identifier id, ResearchCategory category)
		{
			this.categories.put(id, category);
		}

		public ResearchCategory get(Identifier id)
		{
			return this.categories.get(id);
		}

		public ResearchCategory getFirst()
		{
			return this.categories.values().stream().findFirst().get();
		}

		public Collection<ResearchCategory> getAll()
		{
			return this.categories.values();
		}
	}
}
