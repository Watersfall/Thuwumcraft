package net.watersfall.alchemy.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.AspectInventory;
import net.watersfall.alchemy.api.aspect.AspectStack;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.api.lookup.AspectContainer;
import net.watersfall.alchemy.inventory.BasicInventory;
import net.watersfall.alchemy.inventory.BetterAspectInventory;
import net.watersfall.alchemy.recipe.AlchemyRecipes;
import net.watersfall.alchemy.recipe.AspectIngredient;
import net.watersfall.alchemy.screen.EssentiaSmelteryHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;

public class EssentiaSmelteryEntity extends BlockEntity implements BetterAspectInventory, BasicInventory, NamedScreenHandlerFactory, AspectContainer
{
	private final DefaultedList<ItemStack> items;
	private final HashMap<Aspect, AspectStack> aspects;
	private int aspectCount;
	private int fuelAmount;
	private final PropertyDelegate properties = new PropertyDelegate()
	{
		@Override
		public int get(int index)
		{
			switch(index)
			{
				case 0:
					return aspectCount;
				default:
					return fuelAmount;
			}
		}

		@Override
		public void set(int index, int value)
		{
			switch(index)
			{
				case 0:
					aspectCount = value;
					break;
				default:
					fuelAmount = value;
			}
		}

		@Override
		public int size()
		{
			return 2;
		}
	};

	public EssentiaSmelteryEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyBlockEntities.ESSENTIA_SMELTERY_ENTITY, pos, state);
		this.items = DefaultedList.ofSize(2, ItemStack.EMPTY);
		this.aspects = new HashMap<>();
	}

	public static void tick(World world, BlockPos pos, BlockState state, EssentiaSmelteryEntity entity)
	{
		ItemStack stack = entity.getStack(0);
		if(!stack.isEmpty())
		{
			Optional<AspectIngredient> optional = world.getRecipeManager().getFirstMatch(AlchemyRecipes.ASPECT_INGREDIENTS, new AspectInventory.Impl(stack), world);
			if(optional.isPresent())
			{
				AspectIngredient ingredient = optional.get();
				int amount = ingredient.getAspects().stream().mapToInt(AspectStack::getCount).sum();
				if(amount + entity.properties.get(0) <= entity.getTotalAllowedAspects())
				{
					ingredient.getAspects().forEach(entity::addAspect);
					stack.decrement(1);
				}
			}
		}
	}

	public void addAspect(AspectStack stack)
	{
		this.properties.set(0, this.properties.get(0) + stack.getCount());
		AspectStack newStack = this.getAspect(stack.getAspect());
		if(newStack.isEmpty())
		{
			this.setAspect(stack.copy());
		}
		else
		{
			newStack.increment(stack.getCount());
		}
	}

	@Override
	public HashMap<Aspect, AspectStack> getAspects()
	{
		return this.aspects;
	}

	@Override
	public int getMaxSize()
	{
		return Aspects.ASPECTS.size();
	}

	@Override
	public DefaultedList<ItemStack> getContents()
	{
		return this.items;
	}

	public int getTotalAllowedAspects()
	{
		return 256;
	}

	@Override
	public Text getDisplayName()
	{
		return new LiteralText("text");
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
	{
		return new EssentiaSmelteryHandler(syncId, inv, this, this, properties);
	}

	@Override
	public AspectStack insert(AspectStack stack)
	{
		return AspectStack.EMPTY;
	}

	@Override
	public AspectStack extract(AspectStack stack)
	{
		if(stack.isEmpty())
		{
			return stack;
		}
		else if(this.aspects.containsKey(stack.getAspect()))
		{
			AspectStack currentStack = this.aspects.get(stack.getAspect());
			int extract = Math.min(currentStack.getCount(), stack.getCount());
			if(extract > 0)
			{
				currentStack.decrement(extract);
				this.aspectCount -= extract;
				return new AspectStack(stack.getAspect(), extract);
			}
		}
		return AspectStack.EMPTY;
	}

	@Override
	public int getSuction()
	{
		return 1;
	}
}
