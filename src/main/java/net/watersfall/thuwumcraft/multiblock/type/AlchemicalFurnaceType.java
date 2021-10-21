package net.watersfall.thuwumcraft.multiblock.type;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.multiblock.MultiBlockRegistry;
import net.watersfall.thuwumcraft.api.multiblock.MultiBlockType;
import net.watersfall.thuwumcraft.block.AlchemicalFurnaceBlock;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlocks;
import net.watersfall.thuwumcraft.multiblock.component.AlchemicalFurnaceComponent;
import net.watersfall.thuwumcraft.multiblock.component.AlchemicalFurnaceFuelComponent;
import net.watersfall.thuwumcraft.multiblock.component.AlchemicalFurnaceInputComponent;
import net.watersfall.thuwumcraft.multiblock.component.AlchemicalFurnaceOutputComponent;
import net.watersfall.thuwumcraft.multiblock.multiblock.AlchemicalFurnaceMultiBlock;

import java.util.List;

public class AlchemicalFurnaceType implements MultiBlockType<AlchemicalFurnaceMultiBlock>
{
	public static final AlchemicalFurnaceType INSTANCE = new AlchemicalFurnaceType();
	private static final List<BlockState> VALID_STARTING_POINTS = Blocks.FURNACE.getStateManager().getStates();

	private AlchemicalFurnaceType(){}

	private boolean checkStates(BlockPos[] states, World world)
	{
		for(int i = 0; i < states.length; i++)
		{
			if(world.getBlockState(states[i]).getBlock() != Blocks.FURNACE)
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public List<BlockState> getStartingPoints()
	{
		return VALID_STARTING_POINTS;
	}

	@Override
	public BlockPos[] matches(PlayerEntity player, World world, BlockPos pos)
	{
		//AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
		//PlayerResearchAbility ability = provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).get();
		//Research research = Research.REGISTRY.get(AlchemyMod.getId("test_research_6"));
		//if(!ability.hasResearch(research))
		{
			//return MultiBlockType.MISSING;
		}
		BlockState furnace = world.getBlockState(pos);
		BlockPos[] poses = new BlockPos[4];
		poses[AlchemicalFurnaceMultiBlock.BOTTOM_LEFT] = pos;
		poses[AlchemicalFurnaceMultiBlock.INPUT] = pos.up();
		switch(furnace.get(AbstractFurnaceBlock.FACING))
		{
			case EAST:
				poses[AlchemicalFurnaceMultiBlock.OUTPUT] = pos.north();
				poses[AlchemicalFurnaceMultiBlock.TOP_RIGHT] = pos.north().up();
				break;
			case WEST:
				poses[AlchemicalFurnaceMultiBlock.OUTPUT] = pos.south();
				poses[AlchemicalFurnaceMultiBlock.TOP_RIGHT] = pos.south().up();
				break;
			case NORTH:
				poses[AlchemicalFurnaceMultiBlock.OUTPUT] = pos.west();
				poses[AlchemicalFurnaceMultiBlock.TOP_RIGHT] = pos.west().up();
				break;
			case SOUTH:
				poses[AlchemicalFurnaceMultiBlock.OUTPUT] = pos.east();
				poses[AlchemicalFurnaceMultiBlock.TOP_RIGHT] = pos.east().up();
				break;
		}
		if(checkStates(poses, world))
		{
			return poses;
		}
		return MultiBlockType.MISSING;
	}

	@Override
	public AlchemicalFurnaceMultiBlock create(PlayerEntity player, World world, BlockPos pos, BlockPos[] states)
	{
		Direction direction = world.getBlockState(pos).get(AbstractFurnaceBlock.FACING);
		AlchemicalFurnaceComponent[] components = new AlchemicalFurnaceComponent[4];
		AlchemicalFurnaceMultiBlock multiBlock = new AlchemicalFurnaceMultiBlock(world, pos, components);
		components[AlchemicalFurnaceMultiBlock.BOTTOM_LEFT] = new AlchemicalFurnaceFuelComponent(world, multiBlock, pos);
		components[AlchemicalFurnaceMultiBlock.TOP_RIGHT] = new AlchemicalFurnaceComponent(world, multiBlock, states[AlchemicalFurnaceMultiBlock.TOP_RIGHT]);
		components[AlchemicalFurnaceMultiBlock.INPUT] = new AlchemicalFurnaceInputComponent(world, multiBlock, states[AlchemicalFurnaceMultiBlock.INPUT]);
		components[AlchemicalFurnaceMultiBlock.OUTPUT] = new AlchemicalFurnaceOutputComponent(world, multiBlock, states[AlchemicalFurnaceMultiBlock.OUTPUT]);
		world.setBlockState(states[AlchemicalFurnaceMultiBlock.BOTTOM_LEFT], ThuwumcraftBlocks.ALCHEMICAL_FURNACE.getDefaultState().with(AlchemicalFurnaceBlock.DIRECTION, direction));
		world.setBlockState(states[AlchemicalFurnaceMultiBlock.OUTPUT], ThuwumcraftBlocks.CHILD_BLOCK.getDefaultState());
		world.setBlockState(states[AlchemicalFurnaceMultiBlock.INPUT], ThuwumcraftBlocks.CHILD_BLOCK.getDefaultState());
		world.setBlockState(states[AlchemicalFurnaceMultiBlock.TOP_RIGHT], ThuwumcraftBlocks.CHILD_BLOCK.getDefaultState());
		MultiBlockRegistry.SERVER_TICKER.add(multiBlock);
		return multiBlock;
	}
}
