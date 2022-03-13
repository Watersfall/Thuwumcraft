package net.watersfall.thuwumcraft.block.sapling;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.watersfall.thuwumcraft.world.feature.ThuwumcraftConfiguredFeatures;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class SilverwoodSaplingGenerator extends SaplingGenerator
{
	@Nullable
	@Override
	protected RegistryEntry<? extends ConfiguredFeature<TreeFeatureConfig, ?>> getTreeFeature(Random random, boolean bees)
	{
		return ThuwumcraftConfiguredFeatures.SILVERWOOD_TREE;
	}
}
