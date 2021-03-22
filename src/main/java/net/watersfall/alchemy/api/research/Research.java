package net.watersfall.alchemy.api.research;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.alchemy.client.gui.element.RecipeElement;

import java.util.*;
import java.util.function.Predicate;

public class Research
{
	public static Registry REGISTRY = new Registry();

	private Identifier id;
	private Text name;
	private Text description;
	private Text completedDescription;
	private ItemStack stack;
	private ResearchCategory category;
	private RecipeGroup[] tabs;
	private int x;
	private int y;
	private List<Identifier> visibilityAdvancements;
	private List<Identifier> readableAdvancements;
	private List<Identifier> researchAdvancements;
	private List<Identifier> visibilityResearch;
	private List<Identifier> readableResearch;
	private List<Identifier> researchResearch;
	private Predicate<PlayerResearchAbility> isVisible;
	private Predicate<PlayerResearchAbility> isReadable;
	private Predicate<PlayerResearchAbility> isAvailable;

	private Predicate<PlayerResearchAbility> generateFunction(List<Identifier> advancements, List<Identifier> research)
	{
		return (ability -> {
			for(int i = 0; i < advancements.size(); i++)
			{
				if(!ability.hasAdvancement(advancements.get(i)))
				{
					return false;
				}
			}
			for(int i = 0; i < research.size(); i++)
			{
				if(!ability.hasResearch(research.get(i)))
				{
					return false;
				}
			}
			return true;
		});
	}

	private void fillLists(JsonObject json, List<Identifier> advancements, List<Identifier> research)
	{
		if(json != null)
		{
			if(json.has("advancements"))
			{
				JsonArray array = json.getAsJsonArray("advancements");
				array.forEach((advancement) -> {
					advancements.add(Identifier.tryParse(advancement.getAsString()));
				});
			}
			if(json.has("research"))
			{
				JsonArray array = json.getAsJsonArray("research");
				array.forEach((element) -> {
					research.add(Identifier.tryParse(element.getAsString()));
				});
			}
		}
	}

	public Research(Identifier id, JsonObject json)
	{
		this.id = new Identifier(id.getNamespace(), id.getPath().replace("research/", "").replace(".json", "").replace("/", "."));
		this.name = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath() + ".name");
		this.description = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath() + ".desc");
		this.completedDescription = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath() + ".desc.completed");
		this.stack = net.minecraft.util.registry.Registry.ITEM.get(Identifier.tryParse(json.get("icon").getAsString())).getDefaultStack();
		this.category = ResearchCategory.REGISTRY.get(Identifier.tryParse(json.get("category").getAsString()));
		JsonArray tabsArray = json.getAsJsonArray("tabs");
		this.tabs = new RecipeGroup[tabsArray.size()];
		for(int i = 0; i < tabs.length; i++)
		{
			tabs[i] = new RecipeGroup(tabsArray.get(i).getAsJsonObject());
		}
		this.x = json.get("x").getAsInt();
		this.y = json.get("y").getAsInt();
		JsonObject visibleJson = json.getAsJsonObject("visibility_requirements");
		JsonObject readableJson = json.getAsJsonObject("readable_requirements");
		JsonObject researchRequirements = json.getAsJsonObject("research_requirements");
		this.visibilityAdvancements = new ArrayList<>();
		this.readableAdvancements = new ArrayList<>();
		this.researchAdvancements = new ArrayList<>();
		this.visibilityResearch = new ArrayList<>();
		this.readableResearch = new ArrayList<>();
		this.researchResearch = new ArrayList<>();
		fillLists(visibleJson, visibilityAdvancements, visibilityResearch);
		fillLists(readableJson, readableAdvancements, readableResearch);
		fillLists(researchRequirements, researchAdvancements, researchResearch);
		this.isVisible = generateFunction(visibilityAdvancements, visibilityResearch);
		this.isReadable = generateFunction(readableAdvancements, readableResearch);
		this.isAvailable = generateFunction(researchAdvancements, researchResearch);
	}

	public Research(PacketByteBuf buf)
	{
		this.fromPacket(buf);
	}

	public Identifier getId()
	{
		return this.id;
	}

	public Text getName()
	{
		return this.name;
	}

	public Text getDescription()
	{
		return this.description;
	}

	public Text getCompletedDescription()
	{
		return this.completedDescription;
	}

	public ResearchCategory getCategory()
	{
		return this.category;
	}

	public int getX()
	{
		return this.x * 16;
	}

	public int getY()
	{
		return this.y * 16;
	}

	public boolean isVisible(PlayerResearchAbility ability)
	{
		return this.isVisible.test(ability);
	}

	public boolean isReadable(PlayerResearchAbility ability)
	{
		return this.isReadable.test(ability);
	}

	public boolean isAvailable(PlayerResearchAbility ability)
	{
		return this.isAvailable.test(ability);
	}

	public RecipeGroup[] getTabs()
	{
		return this.tabs;
	}

	public ItemStack getStack()
	{
		return this.stack;
	}

	public List<Research> getRequirements()
	{
		List<Research> list = new ArrayList<>();
		for(int i = 0; i < researchResearch.size(); i++)
		{
			list.add(Research.REGISTRY.get(researchResearch.get(i)));
		}
		return list;
	}

	private void writeList(PacketByteBuf buf, List<Identifier> list)
	{
		buf.writeInt(list.size());
		list.forEach(buf::writeIdentifier);
	}

	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeIdentifier(this.id);
		buf.writeItemStack(this.stack);
		buf.writeIdentifier(this.category.getId());
		buf.writeInt(tabs.length);
		for(int i = 0; i < tabs.length; i++)
		{
			tabs[i].toPacket(buf);
		}
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		writeList(buf, visibilityAdvancements);
		writeList(buf, visibilityResearch);
		writeList(buf, readableAdvancements);
		writeList(buf, readableResearch);
		writeList(buf, researchAdvancements);
		writeList(buf, researchResearch);
		return buf;
	}

	private void readList(PacketByteBuf buf, List<Identifier> list)
	{
		int size = buf.readInt();
		for(int i = 0; i < size; i++)
		{
			list.add(buf.readIdentifier());
		}
	}

	public void fromPacket(PacketByteBuf buf)
	{
		this.id = buf.readIdentifier();
		this.name = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath() + ".name");
		this.description = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath() + ".desc");
		this.completedDescription = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath() + ".desc.completed");
		this.stack = buf.readItemStack();
		this.category = ResearchCategory.REGISTRY.get(buf.readIdentifier());
		this.tabs = new RecipeGroup[buf.readInt()];
		for(int i = 0; i < tabs.length; i++)
		{
			tabs[i] = new RecipeGroup(buf);
		}
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.visibilityAdvancements = new ArrayList<>();
		this.readableAdvancements = new ArrayList<>();
		this.researchAdvancements = new ArrayList<>();
		this.visibilityResearch = new ArrayList<>();
		this.readableResearch = new ArrayList<>();
		this.researchResearch = new ArrayList<>();
		readList(buf, visibilityAdvancements);
		readList(buf, visibilityResearch);
		readList(buf, readableAdvancements);
		readList(buf, readableResearch);
		readList(buf, researchAdvancements);
		readList(buf, researchResearch);
		this.isVisible = generateFunction(visibilityAdvancements, visibilityResearch);
		this.isReadable = generateFunction(readableAdvancements, readableResearch);
		this.isAvailable = generateFunction(researchAdvancements, researchResearch);
	}

	public static class Registry
	{
		private final HashMap<Identifier, Research> research;

		private Registry()
		{
			research = new HashMap<>();
		}

		public Research get(Identifier id)
		{
			return this.research.get(id);
		}

		public Research register(Identifier id, Research research)
		{
			this.research.put(id, research);
			return research;
		}

		public Collection<Research> getAll()
		{
			return research.values();
		}

		public PacketByteBuf toPacket(PacketByteBuf buf)
		{
			buf.writeInt(research.size());
			this.research.values().forEach((value) -> {
				value.toPacket(buf);
			});
			return buf;
		}

		public void fromPacket(PacketByteBuf buf)
		{
			research.clear();
			int size = buf.readInt();
			for(int i = 0; i < size; i++)
			{
				Research research = new Research(buf);
				this.research.put(research.getId(), research);
			}
		}
	}

	public static class RecipeGroup
	{
		private final Identifier[] recipes;
		private final boolean requiresComplete;

		public RecipeGroup(Identifier[] recipes, boolean requiresComplete)
		{
			this.recipes = recipes;
			this.requiresComplete = requiresComplete;
		}

		public RecipeGroup(PacketByteBuf buf)
		{
			this.recipes = new Identifier[buf.readInt()];
			for(int i = 0; i < recipes.length; i++)
			{
				this.recipes[i] = buf.readIdentifier();
			}
			this.requiresComplete = buf.readBoolean();
		}

		public RecipeGroup(JsonObject json)
		{
			JsonArray array = json.getAsJsonArray("recipes");
			this.recipes = new Identifier[array.size()];
			for(int i = 0; i < recipes.length; i++)
			{
				this.recipes[i] = Identifier.tryParse(array.get(i).getAsString());
			}
			this.requiresComplete = json.get("requires_complete").getAsBoolean();
		}

		public Identifier[] getRecipes()
		{
			return this.recipes;
		}

		public boolean requiresComplete()
		{
			return this.requiresComplete;
		}

		public PacketByteBuf toPacket(PacketByteBuf buf)
		{
			buf.writeInt(this.recipes.length);
			for(int i = 0; i < this.recipes.length; i++)
			{
				buf.writeIdentifier(this.recipes[i]);
			}
			buf.writeBoolean(this.requiresComplete);
			return buf;
		}
	}
}
