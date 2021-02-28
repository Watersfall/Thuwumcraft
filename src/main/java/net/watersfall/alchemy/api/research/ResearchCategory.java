package net.watersfall.alchemy.api.research;

import com.google.common.collect.ImmutableList;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;

import java.util.List;
import java.util.function.Function;

public class ResearchCategory
{
	public static final ResearchCategory TEST_CATEGORY = new ResearchCategory(new LiteralText("TEST"), (ability -> true));

	private final Text name;
	private List<Research> research;
	private final Function<PlayerResearchAbility, Boolean> isVisible;

	public ResearchCategory(Text name, Function<PlayerResearchAbility, Boolean> isVisible)
	{
		this.name = name;
		this.isVisible = isVisible;
	}

	public Text getName()
	{
		return name;
	}

	public List<Research> getResearch()
	{
		return research;
	}

	public boolean getIsVisible(PlayerResearchAbility ability)
	{
		return isVisible.apply(ability);
	}
}
