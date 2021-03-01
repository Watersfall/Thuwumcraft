package net.watersfall.alchemy.api.research;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Research
{
	public static Registry REGISTRY = new Registry();

	public static final Research TEST_RESEARCH = new Research(AlchemyMod.getId("test_research"),
			new LiteralText("Test Research"),
			new LiteralText("Test Description"),
			Items.DIAMOND.getDefaultStack(),
			ResearchCategory.TEST_CATEGORY,
			0,
			0,
			(ability -> true),
			(ability -> true),
			(ability -> true)
	);

	public static final Research TEST_RESEARCH_2 = new Research(AlchemyMod.getId("test_research_2"),
			new LiteralText("Test Research 2"),
			new LiteralText("Test Description 2"),
			Items.DISPENSER.getDefaultStack(),
			ResearchCategory.TEST_CATEGORY,
			64,
			64,
			(ability -> true),
			(ability -> true),
			(ability -> true)
	);

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
		this.isReadable = isReadable;
		this.isAvailable = isAvailable;
		this.requirements = new ArrayList<>();
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
	}
}