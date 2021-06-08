package net.watersfall.thuwumcraft.block.sapling;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.watersfall.thuwumcraft.world.feature.ThuwumcraftFeatures;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class SilverwoodSaplingGenerator extends SaplingGenerator
{
	@Nullable
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bees)
	{
		return (ConfiguredFeature<TreeFeatureConfig, ?>)ThuwumcraftFeatures.SILVERWOOD_TREE_SAPLING;
	}
}
