package net.watersfall.thuwumcraft.research;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.api.research.Research;
import net.watersfall.thuwumcraft.api.research.ResearchCategory;

import java.util.ArrayList;
import java.util.List;

public class ResearchImpl implements Research
{
	private Identifier id;
	private Text name;
	private Text description;
	private Text completedDescription;
	private Ingredient displayStack;
	private ResearchCategory category;
	private Icon icon;
	private int x;
	private int y;
	private List<RecipeGroup> recipeGroups;
	private List<Identifier> visibilityRequirements;
	private List<Identifier> readabilityRequirements;
	private List<Identifier> clickableRequirements;
	private List<Identifier> researchRequirements;
	private List<Identifier> prerequisiteResearch;
	private List<Ingredient> requiredItems;
	private List<Ingredient> consumedItems;

	public ResearchImpl(Identifier id, JsonObject json)
	{
		this.id = id;
		this.name = new TranslatableText("research." + id.getNamespace() + "." + id.getPath().replace("/", "."));
		this.description = new TranslatableText("research." + id.getNamespace() + "." + id.getPath().replace("/", ".") + ".desc");
		this.completedDescription = new TranslatableText("research." + id.getNamespace() + "." + id.getPath().replace("/", ".") + ".complete");
		this.displayStack = Ingredient.fromJson(json.get("icon_stack"));
		this.category = ThuwumcraftRegistry.RESEARCH_CATEGORY.get(Identifier.tryParse(json.get("category").getAsString()));
		this.icon = Icon.fromJson(JsonHelper.getObject(json, "icon", new JsonObject()));
		this.x = JsonHelper.getInt(json, "x", 0);
		this.y = JsonHelper.getInt(json, "y", 0);
		this.recipeGroups = loadGroupList(JsonHelper.getArray(json, "tabs", new JsonArray()));
		this.visibilityRequirements = loadIdList(JsonHelper.getArray(json, "visibility_requirements", new JsonArray()));
		this.readabilityRequirements = loadIdList(JsonHelper.getArray(json, "readability_requirements", new JsonArray()));
		this.clickableRequirements = loadIdList(JsonHelper.getArray(json, "clickable_requirements", new JsonArray()));
		this.researchRequirements = loadIdList(JsonHelper.getArray(json, "research_requirements", new JsonArray()));
		this.prerequisiteResearch = loadIdList(JsonHelper.getArray(json, "prerequisite_research", new JsonArray()));
		this.requiredItems = loadIngredientList(JsonHelper.getArray(json, "required_items", new JsonArray()));
		this.consumedItems = loadIngredientList(JsonHelper.getArray(json, "consumed_items", new JsonArray()));
	}

	public ResearchImpl(Identifier id, PacketByteBuf buf)
	{
		this.fromPacket(id, buf);
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
	public Text getDescription()
	{
		return description;
	}

	@Override
	public Text getCompletedDescription()
	{
		return completedDescription;
	}

	@Override
	public Ingredient getDisplayStack()
	{
		return displayStack;
	}

	@Override
	public ResearchCategory getCategory()
	{
		return category;
	}

	@Override
	public List<RecipeGroup> getRecipeTabs()
	{
		return recipeGroups;
	}

	@Override
	public Icon getIcon()
	{
		return icon;
	}

	@Override
	public int getX()
	{
		return x * 13;
	}

	@Override
	public int getY()
	{
		return y * 13;
	}

	@Override
	public boolean isVisible(PlayerResearchAbility player)
	{
		for(int i = 0; i < visibilityRequirements.size(); i++)
		{
			if(!player.hasAdvancement(id))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isReadable(PlayerResearchAbility player)
	{
		for(int i = 0; i < readabilityRequirements.size(); i++)
		{
			if(!player.hasAdvancement(id))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isClickable(PlayerResearchAbility player)
	{
		for(int i = 0; i < clickableRequirements.size(); i++)
		{
			if(!player.hasAdvancement(id))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isResearchable(PlayerEntity player, PlayerResearchAbility research)
	{
		for(int i = 0; i < researchRequirements.size(); i++)
		{
			if(!research.hasAdvancement(id))
			{
				return false;
			}
		}
		return hasRequiredItems(player);
	}

	@Override
	public boolean hasRequiredItems(PlayerEntity player)
	{
		PlayerInventory inventory = player.getInventory();
		for(int i = 0; i < requiredItems.size(); i++)
		{
			boolean found = false;
			for(int p = 0; p < inventory.size(); p++)
			{
				if(requiredItems.get(i).test(inventory.getStack(p)))
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
		for(int i = 0; i < consumedItems.size(); i++)
		{
			boolean found = false;
			for(int p = 0; p < inventory.size(); p++)
			{
				if(consumedItems.get(i).test(inventory.getStack(p)))
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

	@Override
	public List<Identifier> getVisibilityRequirements()
	{
		return visibilityRequirements;
	}

	@Override
	public List<Identifier> getReadabilityRequirements()
	{
		return readabilityRequirements;
	}

	@Override
	public List<Identifier> getClickableRequirements()
	{
		return clickableRequirements;
	}

	@Override
	public List<Identifier> getResearchRequirements()
	{
		return researchRequirements;
	}

	@Override
	public List<Identifier> getPrerequisiteResearch()
	{
		return prerequisiteResearch;
	}

	@Override
	public List<Ingredient> getRequiredItems()
	{
		return requiredItems;
	}

	@Override
	public List<Ingredient> getConsumedItems()
	{
		return consumedItems;
	}

	@Override
	public void consumeItems(PlayerEntity player)
	{

	}

	@Override
	public void toPacket(PacketByteBuf buf)
	{
		buf.writeIdentifier(id);
		buf.writeText(name);
		buf.writeText(description);
		buf.writeText(completedDescription);
		displayStack.write(buf);
		buf.writeIdentifier(ThuwumcraftRegistry.RESEARCH_CATEGORY.getId(category));
		icon.toPacket(buf);
		buf.writeInt(x);
		buf.writeInt(y);
		writeGroupList(buf);
		writeIdList(visibilityRequirements, buf);
		writeIdList(readabilityRequirements, buf);
		writeIdList(clickableRequirements, buf);
		writeIdList(researchRequirements, buf);
		writeIdList(prerequisiteResearch, buf);
		writeIngredientList(requiredItems, buf);
		writeIngredientList(consumedItems, buf);
	}

	@Override
	public void fromPacket(Identifier id, PacketByteBuf buf)
	{
		this.id = id;
		this.name = buf.readText();
		this.description = buf.readText();
		this.completedDescription = buf.readText();
		this.displayStack = Ingredient.fromPacket(buf);
		this.category = ThuwumcraftRegistry.RESEARCH_CATEGORY.get(buf.readIdentifier());
		this.icon = Icon.fromPacket(buf);
		this.x = buf.readInt();
		this.y = buf.readInt();
		loadGroupList(buf);
		visibilityRequirements = loadIdList(buf);
		readabilityRequirements = loadIdList(buf);
		clickableRequirements = loadIdList(buf);
		researchRequirements = loadIdList(buf);
		prerequisiteResearch = loadIdList(buf);
		requiredItems = loadIngredientList(buf);
		consumedItems = loadIngredientList(buf);
	}

	private void writeGroupList(PacketByteBuf buf)
	{
		buf.writeInt(recipeGroups.size());
		for(int i = 0; i < recipeGroups.size(); i++)
		{
			recipeGroups.get(i).toPacket(buf);
		}
	}

	private void writeIngredientList(List<Ingredient> list, PacketByteBuf buf)
	{
		buf.writeInt(list.size());
		for(int i = 0; i < list.size(); i++)
		{
			list.get(i).write(buf);
		}
	}

	private void writeIdList(List<Identifier> list, PacketByteBuf buf)
	{
		buf.writeInt(list.size());
		for(int i = 0; i < list.size(); i++)
		{
			buf.writeIdentifier(list.get(i));
		}
	}

	private List<RecipeGroup> loadGroupList(PacketByteBuf buf)
	{
		int size = buf.readInt();
		List<RecipeGroup> tempList = new ArrayList<>(size);
		for(int i = 0; i < size; i++)
		{
			tempList.add(RecipeGroup.fromPacket(buf));
		}
		return ImmutableList.copyOf(tempList);
	}

	private List<Ingredient> loadIngredientList(PacketByteBuf buf)
	{
		int size = buf.readInt();
		List<Ingredient> tempList = new ArrayList<>(size);
		for(int i = 0; i < size; i++)
		{
			tempList.add(Ingredient.fromPacket(buf));
		}
		return ImmutableList.copyOf(tempList);
	}

	private List<Identifier> loadIdList(PacketByteBuf buf)
	{
		int size = buf.readInt();
		List<Identifier> tempList = new ArrayList<>(size);
		for(int i = 0; i < size; i++)
		{
			tempList.add(buf.readIdentifier());
		}
		return ImmutableList.copyOf(tempList);
	}

	private List<RecipeGroup> loadGroupList(JsonArray json)
	{
		List<RecipeGroup> tempList = new ArrayList<>(json.size());
		for(int i = 0; i < json.size(); i++)
		{
			tempList.add(RecipeGroup.fromJson(json.get(i).getAsJsonObject()));
		}
		return ImmutableList.copyOf(tempList);
	}

	private List<Ingredient> loadIngredientList(JsonArray json)
	{
		List<Ingredient> tempList = new ArrayList<>(json.size());
		for(int i = 0; i < json.size(); i++)
		{
			JsonElement element = json.get(i);
			if(element.isJsonPrimitive())
			{
				tempList.add(Ingredient.ofItems(JsonHelper.asItem(element, "item")));
			}
			else
			{
				tempList.add(Ingredient.fromJson(json.get(i)));
			}
		}
		return ImmutableList.copyOf(tempList);
	}

	private List<Identifier> loadIdList(JsonArray json)
	{
		List<Identifier> tempList = new ArrayList<>(json.size());
		for(int i = 0; i < json.size(); i++)
		{
			tempList.add(Identifier.tryParse(json.get(i).getAsString()));
		}
		return ImmutableList.copyOf(tempList);
	}
}
