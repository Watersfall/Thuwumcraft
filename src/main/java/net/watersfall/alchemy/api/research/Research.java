package net.watersfall.alchemy.api.research;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Research
{
	public static Registry REGISTRY = new Registry();

	private Identifier id;
	private Text name;
	private Text description;
	private ItemStack stack;
	private ResearchCategory category;
	private int x;
	private int y;
	private Function<PlayerResearchAbility, Boolean> isVisible;
	private Function<PlayerResearchAbility, Boolean> isReadable;
	private Function<PlayerResearchAbility, Boolean> isAvailable;
	private List<Research> requirements;

	public Research(Identifier id,
					Text name,
					Text description,
					ItemStack stack,
					ResearchCategory category,
					int x,
					int y,
					Function<PlayerResearchAbility, Boolean> isVisible,
					Function<PlayerResearchAbility, Boolean> isReadable,
					Function<PlayerResearchAbility, Boolean> isAvailable
	)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.stack = stack;
		this.category = category;
		this.x = x;
		this.y = y;
		this.isVisible = isVisible;
		this.isReadable = (ability -> ability.hasCriterion("minecraft:zombie"));
		this.isAvailable = isAvailable;
		this.requirements = new ArrayList<>();
	}

	public Research(Identifier id, JsonObject json)
	{
		this.id = new Identifier(id.getNamespace(), id.getPath().replace("research/", "").replace(".json", "").replace("/", "."));
		this.name = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath() + ".name");
		this.description = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath() + ".desc");
		this.stack = net.minecraft.util.registry.Registry.ITEM.get(Identifier.tryParse(json.get("icon").getAsString())).getDefaultStack();
		this.category = ResearchCategory.TEST_CATEGORY;
		this.x = json.get("x").getAsInt();
		this.y = json.get("y").getAsInt();
		this.isVisible = (ability -> true);
		this.isReadable = (ability -> ability.hasCriterion("minecraft:zombie"));
		this.isAvailable = (ability -> true);
		this.requirements = new ArrayList<>();
	}

	public Research(PacketByteBuf buf)
	{
		this.fromPacket(buf);
	}

	public void setRequirements(Research... requirements)
	{
		this.requirements.addAll(Arrays.stream(requirements).collect(Collectors.toList()));
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

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}

	public boolean isVisible(PlayerResearchAbility ability)
	{
		return this.isVisible.apply(ability);
	}

	public boolean isReadable(PlayerResearchAbility ability)
	{
		return this.isReadable.apply(ability);
	}

	public boolean isAvailable(PlayerResearchAbility ability)
	{
		return this.isAvailable.apply(ability);
	}

	public ItemStack getStack()
	{
		return this.stack;
	}

	public List<Research> getRequirements()
	{
		return this.requirements;
	}

	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeIdentifier(this.id);
		buf.writeItemStack(this.stack);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		return buf;
	}

	public void fromPacket(PacketByteBuf buf)
	{
		this.id = buf.readIdentifier();
		this.name = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath() + ".name");
		this.description = new TranslatableText("research." + this.id.getNamespace() + "." + this.id.getPath() + ".desc");
		this.stack = buf.readItemStack();
		this.category = ResearchCategory.TEST_CATEGORY;
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.isVisible = (ability -> true);
		this.isReadable = (ability -> ability.hasCriterion("minecraft:zombie"));
		this.isAvailable = (ability -> true);
		this.requirements = new ArrayList<>();
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
}
