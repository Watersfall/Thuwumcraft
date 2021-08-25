package net.watersfall.thuwumcraft.gui;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

import static net.watersfall.thuwumcraft.Thuwumcraft.getId;

public class ThuwumcraftScreenHandlers
{
	public static final ScreenHandlerType<ApothecaryGuideHandler> APOTHECARY_GUIDE_HANDLER;
	public static final ScreenHandlerType<AlchemicalFurnaceHandler> ALCHEMICAL_FURNACE_HANDLER;
	public static final ScreenHandlerType<ResearchBookHandler> RESEARCH_BOOK_HANDLER;
	public static final ScreenHandlerType<NekomancyTableHandler> NEKOMANCY_TABLE_HANDLER;
	public static final ScreenHandlerType<AspectCraftingHandler> ASPECT_CRAFTING_HANDLER;
	public static final ScreenHandlerType<PotionSprayerHandler> POTION_SPRAYER_HANDLER;
	public static final ScreenHandlerType<EssentiaSmelteryHandler> ESSENTIA_SMELTERY_HANDLER;
	public static final ScreenHandlerType<WandWorkbenchHandler> WAND_WORKBENCH;
	public static final ScreenHandlerType<ThaumatoriumHandler> THAUMATORIUM;

	static
	{
		APOTHECARY_GUIDE_HANDLER = ScreenHandlerRegistry.registerSimple(getId("apothecary_guide_handler"), ApothecaryGuideHandler::new);
		ALCHEMICAL_FURNACE_HANDLER = ScreenHandlerRegistry.registerSimple(getId("alchemical_furnace_handler"), AlchemicalFurnaceHandler::new);
		RESEARCH_BOOK_HANDLER = ScreenHandlerRegistry.registerExtended(getId("guide_handler"), ResearchBookHandler::new);
		NEKOMANCY_TABLE_HANDLER = ScreenHandlerRegistry.registerSimple(getId("nekomancy_handler"), NekomancyTableHandler::new);
		ASPECT_CRAFTING_HANDLER = ScreenHandlerRegistry.registerExtended(getId("aspect_crafting_handler"), AspectCraftingHandler::new);
		POTION_SPRAYER_HANDLER = ScreenHandlerRegistry.registerSimple(getId("potion_sprayer_handler"), PotionSprayerHandler::new);
		ESSENTIA_SMELTERY_HANDLER = ScreenHandlerRegistry.registerSimple(getId("essentia_smeltery_handler"), EssentiaSmelteryHandler::new);
		WAND_WORKBENCH = ScreenHandlerRegistry.registerSimple(getId("wand_workbench"), WandWorkbenchHandler::new);
		THAUMATORIUM = ScreenHandlerRegistry.registerExtended(getId("thaumatorium"), ThaumatoriumHandler::new);
	}
}
