package net.watersfall.thuwumcraft.world.feature;

import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.FeatureSize;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.trunk.TrunkPlacer;

import java.util.List;

public class OpenTreeFeatureConfig extends TreeFeatureConfig
{
	protected OpenTreeFeatureConfig(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, BlockStateProvider dirtProvider, FeatureSize minimumSize, List<TreeDecorator> decorators, boolean ignoreVines, boolean forceDirt)
	{
		super(trunkProvider, trunkPlacer, foliageProvider, foliagePlacer, dirtProvider, minimumSize, decorators, ignoreVines, forceDirt);
	}
}
