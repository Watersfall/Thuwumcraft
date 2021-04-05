package net.watersfall.alchemy.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

import static net.watersfall.alchemy.AlchemyMod.getId;

public class AlchemyScreenHandlers
{
	public static final ScreenHandlerType<ApothecaryGuideHandler> APOTHECARY_GUIDE_HANDLER;
	public static final ScreenHandlerType<AlchemicalFurnaceHandler> ALCHEMICAL_FURNACE_HANDLER;
	public static final ScreenHandlerType<ResearchBookHandler> RESEARCH_BOOK_HANDLER;
	public static final ScreenHandlerType<NekomancyTableHandler> NEKOMANCY_TABLE_HANDLER;
	public static final ScreenHandlerType<AspectCraftingHandler> ASPECT_CRAFTING_HANDLER;

	static
	{
		APOTHECARY_GUIDE_HANDLER = ScreenHandlerRegistry.registerSimple(getId("apothecary_guide_handler"), ApothecaryGuideHandler::new);
		ALCHEMICAL_FURNACE_HANDLER = ScreenHandlerRegistry.registerSimple(getId("alchemical_furnace_handler"), AlchemicalFurnaceHandler::new);
		RESEARCH_BOOK_HANDLER = ScreenHandlerRegistry.registerSimple(getId("guide_handler"), ResearchBookHandler::new);
		NEKOMANCY_TABLE_HANDLER = ScreenHandlerRegistry.registerSimple(getId("nekomancy_handler"), NekomancyTableHandler::new);
		ASPECT_CRAFTING_HANDLER = ScreenHandlerRegistry.registerExtended(getId("aspect_crafting_handler"), AspectCraftingHandler::new);
	}
}
