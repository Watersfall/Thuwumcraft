package net.watersfall.thuwumcraft.api.research;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;

import java.util.ArrayList;
import java.util.List;

public interface Research
{
	Identifier getId();
	Text getName();
	Text getDescription();
	Text getCompletedDescription();
	Ingredient getDisplayStack();
	ResearchCategory getCategory();
	List<RecipeGroup> getRecipeTabs();
	Icon getIcon();
	int getX();
	int getY();
	boolean isVisible(PlayerResearchAbility player);
	boolean isReadable(PlayerResearchAbility player);
	boolean isClickable(PlayerResearchAbility player);
	boolean isResearchable(PlayerEntity player, PlayerResearchAbility research);
	boolean hasRequiredItems(PlayerEntity player);
	List<Identifier> getVisibilityRequirements();
	List<Identifier> getReadabilityRequirements();
	List<Identifier> getClickableRequirements();
	List<Identifier> getResearchRequirements();
	List<Identifier> getPrerequisiteResearch();
	List<Ingredient> getRequiredItems();
	List<Ingredient> getConsumedItems();
	void consumeItems(PlayerEntity player);
	void toPacket(PacketByteBuf buf);
	void fromPacket(Identifier id, PacketByteBuf buf);

	record Icon(Identifier texture, int u, int v)
	{
		public void toPacket(PacketByteBuf buf)
		{
			buf.writeIdentifier(texture);
			buf.writeInt(u);
			buf.writeInt(v);
		}

		public static Icon fromPacket(PacketByteBuf buf)
		{
			Identifier id = buf.readIdentifier();
			int u = buf.readInt();
			int v = buf.readInt();
			return new Icon(id, u ,v);
		}

		public static Icon fromJson(JsonObject json)
		{
			Identifier id = Identifier.tryParse(json.get("texture").getAsString());
			int u = JsonHelper.getInt(json, "u", 0);
			int v = JsonHelper.getInt(json, "v", 0);
			return new Icon(id, u ,v);
		}
	}

	record RecipeGroup(List<Identifier> recipes, boolean isResearchRequired)
	{
		public void toPacket(PacketByteBuf buf)
		{
			buf.writeInt(recipes.size());
			for(int i = 0; i < recipes.size(); i++)
			{
				buf.writeIdentifier(recipes.get(i));
			}
			buf.writeBoolean(this.isResearchRequired);
		}

		public static RecipeGroup fromPacket(PacketByteBuf buf)
		{
			int size = buf.readInt();
			List<Identifier> tempList = new ArrayList<>(size);
			for(int i = 0; i < size; i++)
			{
				tempList.add(buf.readIdentifier());
			}
			boolean researchRequired = buf.readBoolean();
			return new RecipeGroup(ImmutableList.copyOf(tempList), researchRequired);
		}

		public static RecipeGroup fromJson(JsonObject json)
		{
			JsonArray array = json.getAsJsonArray("recipes");
			List<Identifier> tempList = new ArrayList<>(array.size());
			for(int i = 0; i < array.size(); i++)
			{
				tempList.add(Identifier.tryParse(array.get(i).getAsString()));
			}
			boolean requiresComplete = json.get("requires_complete").getAsBoolean();
			return new RecipeGroup(ImmutableList.copyOf(tempList), requiresComplete);
		}
	}
}
