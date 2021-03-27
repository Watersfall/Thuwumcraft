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

	static
	{
		BREWING_CAULDRON_BLOCK = new BrewingCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON).ticksRandomly());
		PEDESTAL_BLOCK = new PedestalBlock(FabricBlockSettings.copyOf(Blocks.STONE).luminance(7).nonOpaque());
		ALCHEMICAL_FURNACE_BLOCK = new AlchemicalFurnaceBlock(FabricBlockSettings.copyOf(Blocks.STONE));
		CHILD_BLOCK = new ChildBlock(FabricBlockSettings.copyOf(Blocks.GLASS));
		CRUCIBLE_BLOCK = new CrucibleBlock(FabricBlockSettings.copyOf(BREWING_CAULDRON_BLOCK));
		JAR_BLOCK = new JarBlock(FabricBlockSettings.of(Material.GLASS).nonOpaque().breakByHand(true));
		PHIAL_SHELF_BLOCK = new PhialShelfBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS).nonOpaque());
		ASPECT_PIPE_BLOCK = new AspectPipeBlock(FabricBlockSettings.of(Material.METAL).nonOpaque());
		SPAWNER_FRAME = new Block(FabricBlockSettings.copyOf(Blocks.SPAWNER));
		CUSTOM_SPAWNER = new CustomSpawnerBlock(FabricBlockSettings.copyOf(Blocks.SPAWNER));
		NEKOMANCY_TABLE = new NekomancyBlock(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE));
	}

	public static void register()
	{
		Registry.register(Registry.BLOCK, AlchemyMod.getId("brewing_cauldron"), AlchemyBlocks.BREWING_CAULDRON_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("pedestal"), AlchemyBlocks.PEDESTAL_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("alchemical_furnace"), AlchemyBlocks.ALCHEMICAL_FURNACE_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("child_block"), AlchemyBlocks.CHILD_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("brewing_crucible"), CRUCIBLE_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("jar"), JAR_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("phial_shelf"), PHIAL_SHELF_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("aspect_pipe"), ASPECT_PIPE_BLOCK);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("spawner_frame"), SPAWNER_FRAME);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("custom_spawner"), CUSTOM_SPAWNER);
		Registry.register(Registry.BLOCK, AlchemyMod.getId("nekomancy_table"), NEKOMANCY_TABLE);
	}

	private static <T extends Block> T register(T block, Identifier id)
	{
		return Registry.register(Registry.BLOCK, id, block);
	}
}
