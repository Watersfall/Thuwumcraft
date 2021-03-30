package net.watersfall.alchemy.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;

public class AlchemyBlocks
{
	public static final BrewingCauldronBlock BREWING_CAULDRON_BLOCK;
	public static final PedestalBlock PEDESTAL_BLOCK;
	public static final AlchemicalFurnaceBlock ALCHEMICAL_FURNACE_BLOCK;
	public static final ChildBlock CHILD_BLOCK;
	public static final CrucibleBlock CRUCIBLE_BLOCK;
	public static final JarBlock JAR_BLOCK;
	public static final PhialShelfBlock PHIAL_SHELF_BLOCK;
	public static final AspectPipeBlock ASPECT_PIPE_BLOCK;
	public static final Block SPAWNER_FRAME;
	public static final CustomSpawnerBlock CUSTOM_SPAWNER;
	public static final NekomancyBlock NEKOMANCY_TABLE;
	public static final CraftingHopper CRAFTING_HOPPER;

	static
	{
		BREWING_CAULDRON_BLOCK = register(AlchemyMod.getId("brewing_cauldron"), new BrewingCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON).ticksRandomly()));
		PEDESTAL_BLOCK = register(AlchemyMod.getId("pedestal"), new PedestalBlock(FabricBlockSettings.copyOf(Blocks.STONE).luminance(7).nonOpaque()));
		ALCHEMICAL_FURNACE_BLOCK = register(AlchemyMod.getId("alchemical_furnace"), new AlchemicalFurnaceBlock(FabricBlockSettings.copyOf(Blocks.STONE)));
		CHILD_BLOCK = register(AlchemyMod.getId("child_block"), new ChildBlock(FabricBlockSettings.copyOf(Blocks.GLASS)));
		CRUCIBLE_BLOCK = register(AlchemyMod.getId("brewing_crucible"), new CrucibleBlock(FabricBlockSettings.copyOf(BREWING_CAULDRON_BLOCK)));
		JAR_BLOCK = register(AlchemyMod.getId("jar"), new JarBlock(FabricBlockSettings.of(Material.GLASS).nonOpaque().breakByHand(true)));
		PHIAL_SHELF_BLOCK = register(AlchemyMod.getId("phial_shelf"), new PhialShelfBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS).nonOpaque()));
		ASPECT_PIPE_BLOCK = register(AlchemyMod.getId("aspect_pipe"), new AspectPipeBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()));
		SPAWNER_FRAME = register(AlchemyMod.getId("spawner_frame"), new Block(FabricBlockSettings.copyOf(Blocks.SPAWNER)));
		CUSTOM_SPAWNER = register(AlchemyMod.getId("custom_spawner"), new CustomSpawnerBlock(FabricBlockSettings.copyOf(Blocks.SPAWNER)));
		NEKOMANCY_TABLE = register(AlchemyMod.getId("nekomancy_table"), new NekomancyBlock(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE)));
		CRAFTING_HOPPER = register(AlchemyMod.getId("crafting_hopper"), new CraftingHopper(FabricBlockSettings.copyOf(Blocks.HOPPER)));
	}

	private static <T extends Block> T register(Identifier id, T block)
	{
		return Registry.register(Registry.BLOCK, id, block);
	}
}
