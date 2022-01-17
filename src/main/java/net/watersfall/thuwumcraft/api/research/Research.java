package net.watersfall.thuwumcraft.api.research;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;

import java.util.ArrayList;
import java.util.List;

public class Research
{
	private Identifier id;
	private Text name;
	private Text description;
	private Text completedDescription;
	private Ingredient stack;
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
	private List<Ingredient> requiredItems;
	private List<Ingredient> consumedItems;

	private boolean checkList(PlayerResearchAbility ability, List<Identifier> advancements, List<Identifier> research)
	{
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

	private void fillItemLists(JsonArray json, List<Ingredient> items)
	{
		for(int i = 0; i < json.size(); i++)
		{
			if(json.get(i).isJsonObject() || json.get(i).isJsonArray())
			{
				items.add(Ingredient.fromJson(json.get(i)));
			}
			else
			{
				items.add(Ingredient.ofItems(net.minecraft.util.registry.Registry.ITEM.get(Identifier.tryParse(json.get(i).getAsString()))));
			}
		}
	}

	public Research(Identifier id, JsonObject json)
	{
		this.id = new Identifier(id.getNamespace(), id.getPath().replace("research/", "").replace(".json", ""));
		this.name = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath().replace("/", ".") + ".name");
		this.description = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath().replace("/", ".") + ".desc");
		this.completedDescription = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath().replace("/", ".") + ".desc.completed");
		if(json.get("icon").isJsonObject() || json.get("icon").isJsonArray())
		{
			this.stack = Ingredient.fromJson(json.get("icon"));
		}
		else
		{
			this.stack = Ingredient.ofItems(net.minecraft.util.registry.Registry.ITEM.get(Identifier.tryParse(json.get("icon").getAsString())));
		}
		this.category = ThuwumcraftRegistry.RESEARCH_CATEGORY.get(Identifier.tryParse(json.get("category").getAsString()));
		JsonArray tabsArray = json.getAsJsonArray("tabs");
		if(tabsArray != null)
		{
			this.tabs = new RecipeGroup[tabsArray.size()];
			for(int i = 0; i < tabs.length; i++)
			{
				tabs[i] = new RecipeGroup(tabsArray.get(i).getAsJsonObject());
			}
		}
		else
		{
			this.tabs = new RecipeGroup[0];
		}
		this.x = json.get("x").getAsInt();
		this.y = json.get("y").getAsInt();
		JsonObject visibleJson = json.getAsJsonObject("visibility_requirements");
		JsonObject readableJson = json.getAsJsonObject("readable_requirements");
		JsonObject researchRequirements = json.getAsJsonObject("research_requirements");
		this.requiredItems = new ArrayList<>();
		this.consumedItems = new ArrayList<>();
		if(json.has("complete_requirements"))
		{
			JsonArray requiredItems = json.getAsJsonObject("complete_requirements").getAsJsonArray("inventory_required");
			JsonArray consumedItems = json.getAsJsonObject("complete_requirements").getAsJsonArray("inventory_consumed");
			if(requiredItems != null)
				fillItemLists(requiredItems, this.requiredItems);
			if(consumedItems != null)
				fillItemLists(consumedItems, this.consumedItems);
		}
		this.visibilityAdvancements = new ArrayList<>();
		this.readableAdvancements = new ArrayList<>();
		this.researchAdvancements = new ArrayList<>();
		this.visibilityResearch = new ArrayList<>();
		this.readableResearch = new ArrayList<>();
		this.researchResearch = new ArrayList<>();
		fillLists(visibleJson, visibilityAdvancements, visibilityResearch);
		fillLists(readableJson, readableAdvancements, readableResearch);
		fillLists(researchRequirements, researchAdvancements, researchResearch);
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
		return this.checkList(ability, this.visibilityAdvancements, this.visibilityResearch);
	}

	public boolean isReadable(PlayerResearchAbility ability)
	{
		return this.checkList(ability, this.readableAdvancements, this.readableResearch);
	}

	public boolean isAvailable(PlayerResearchAbility ability)
	{
		return this.checkList(ability, this.researchAdvancements, this.researchResearch);
	}

	public RecipeGroup[] getTabs()
	{
		return this.tabs;
	}

	public Ingredient getStack()
	{
		return this.stack;
	}

	public List<Ingredient> getRequiredItems()
	{
		return this.requiredItems;
	}

	public List<Ingredient> getConsumedItems()
	{
		return this.consumedItems;
	}

	public boolean hasItems(PlayerEntity player)
	{
		for(int i = 0; i < this.requiredItems.size(); i++)
		{
			boolean found = false;
			for(int o = 0; o < player.getInventory().size(); o++)
			{
				if(this.requiredItems.get(i).test(player.getInventory().getStack(o)))
				{
					found = true;
					break;
				}
			}
			if(!found)
			{
				return false;
			}
		}
		for(int i = 0; i < this.consumedItems.size(); i++)
		{
			boolean found = false;
			for(int o = 0; o < player.getInventory().size(); o++)
			{
				if(this.consumedItems.get(i).test(player.getInventory().getStack(o)))
				{
					found = true;
					break;
				}
			}
			if(!found)
			{
				return false;
			}
		}
		return true;
	}

	public void consumeItems(PlayerEntity player)
	{
		for(int i = 0; i < this.consumedItems.size(); i++)
		{
			for(int o = 0; o < player.getInventory().size(); o++)
			{
				if(consumedItems.get(i).test(player.getInventory().getStack(o)))
				{
					player.getInventory().getStack(o).decrement(1);
					break;
				}
			}
		}
	}

	public List<Research> getRequirements()
	{
		List<Research> list = new ArrayList<>();
		for(int i = 0; i < researchResearch.size(); i++)
		{
			list.add(ThuwumcraftRegistry.RESEARCH.get(researchResearch.get(i)));
		}
		return list;
	}

	private void writeList(PacketByteBuf buf, List<Identifier> list)
	{
		buf.writeInt(list.size());
		list.forEach(buf::writeIdentifier);
	}

	private void writeItemList(PacketByteBuf buf, List<Ingredient> list)
	{
		buf.writeInt(list.size());
		list.forEach(ingredient -> ingredient.write(buf));
	}

	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeIdentifier(this.id);
		this.stack.write(buf);
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
		writeItemList(buf, requiredItems);
		writeItemList(buf, consumedItems);
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

	private void readItemList(PacketByteBuf buf, List<Ingredient> list)
	{
		int size = buf.readInt();
		for(int i = 0; i < size; i++)
		{
			list.add(Ingredient.fromPacket(buf));
		}
	}

	public void fromPacket(PacketByteBuf buf)
	{
		this.id = buf.readIdentifier();
		this.name = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath().replace("/", ".") + ".name");
		this.description = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath().replace("/", ".") + ".desc");
		this.completedDescription = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath().replace("/", ".") + ".desc.completed");
		this.stack = Ingredient.fromPacket(buf);
		this.category = ThuwumcraftRegistry.RESEARCH_CATEGORY.get(buf.readIdentifier());
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
		this.requiredItems = new ArrayList<>();
		this.consumedItems = new ArrayList<>();
		readList(buf, visibilityAdvancements);
		readList(buf, visibilityResearch);
		readList(buf, readableAdvancements);
		readList(buf, readableResearch);
		readList(buf, researchAdvancements);
		readList(buf, researchResearch);
		readItemList(buf, requiredItems);
		readItemList(buf, consumedItems);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(super.equals(obj))
		{
			return true;
		}
		else if(obj instanceof Research research)
		{
			return research.getId().equals(this.id);
		}
		return false;
	}

	public static PacketByteBuf toFullPacket(PacketByteBuf buf)
	{
		buf.writeInt(ThuwumcraftRegistry.RESEARCH.values().size());
		ThuwumcraftRegistry.RESEARCH.values().forEach((value) -> {
			value.toPacket(buf);
		});
		return buf;
	}

	public static void fromFullPacket(PacketByteBuf buf)
	{
		ThuwumcraftRegistry.RESEARCH.clear();
		int size = buf.readInt();
		for(int i = 0; i < size; i++)
		{
			Research research = new Research(buf);
			ThuwumcraftRegistry.RESEARCH.register(research.getId(), research);
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
