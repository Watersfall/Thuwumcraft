package net.watersfall.alchemy.recipe;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.StringTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.AspectStack;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.block.AlchemyBlocks;
import net.watersfall.alchemy.block.entity.JarEntity;
import net.watersfall.alchemy.block.entity.PedestalEntity;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PedestalRecipe implements Recipe<PedestalEntity>
{
	private final Identifier id;
	private final List<Ingredient> inputs;
	private final List<AspectStack> aspects;
	private final Ingredient catalyst;
	private final ItemStack output;
	private static final byte HORIZONTAL_RANGE = 5;
	private static final byte VERTICAL_RANGE = 1;

	public static List<PedestalEntity> getNearbyPedestals(BlockPos pos, World world)
	{
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		BlockState loopState;
		List<PedestalEntity> entities = new ArrayList<>();
		for(int x = pos.getX() - HORIZONTAL_RANGE; x < pos.getX() + HORIZONTAL_RANGE; x++)
		{
			for(int y = pos.getY() - VERTICAL_RANGE; y < pos.getY() + VERTICAL_RANGE; y++)
			{
				for(int z = pos.getZ() - HORIZONTAL_RANGE; z < pos.getZ() + HORIZONTAL_RANGE; z++)
				{
					mutablePos.set(x, y, z);
					if(!mutablePos.equals(pos))
					{
						loopState = world.getBlockState(mutablePos);
						if(loopState.getBlock() == AlchemyBlocks.PEDESTAL_BLOCK)
						{
							entities.add((PedestalEntity)world.getBlockEntity(mutablePos));
						}
					}
				}
			}
		}
		return entities;
	}

	public static List<JarEntity> getNearbyJars(BlockPos pos, World world)
	{
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		BlockState loopState;
		List<JarEntity> entities = new ArrayList<>();
		for(int x = pos.getX() - HORIZONTAL_RANGE; x < pos.getX() + HORIZONTAL_RANGE; x++)
		{
			for(int y = pos.getY() - VERTICAL_RANGE; y < pos.getY() + VERTICAL_RANGE; y++)
			{
				for(int z = pos.getZ() - HORIZONTAL_RANGE; z < pos.getZ() + HORIZONTAL_RANGE; z++)
				{
					mutablePos.set(x, y, z);
					if(!mutablePos.equals(pos))
					{
						loopState = world.getBlockState(mutablePos);
						if(loopState.getBlock() == AlchemyBlocks.JAR_BLOCK)
						{
							entities.add((JarEntity) world.getBlockEntity(mutablePos));
						}
					}
				}
			}
		}
		return entities;
	}

	public PedestalRecipe(Identifier id, List<Ingredient> inputs, List<AspectStack> aspects, Ingredient catalyst, ItemStack output)
	{
		this.id = id;
		this.inputs = inputs;
		this.catalyst = catalyst;
		this.output = output;
		this.aspects = aspects;
	}

	@Override
	public boolean matches(PedestalEntity inv, World world)
	{
		if(catalyst.test(inv.getStack()))
		{
			BlockPos pos = inv.getPos();
			List<PedestalEntity> entities = getNearbyPedestals(pos, world);
			entities.removeIf((entity) -> entity.isCrafting() || entity.isMain());
			if(entities.size() >= inputs.size())
			{
				for(int i = 0; i < inputs.size(); i++)
				{
					boolean found = false;
					for(int o = 0; o < entities.size(); o++)
					{
						if(inputs.get(i).test(entities.get(o).getStack()))
						{
							found = true;
							break;
						}
					}
					if(!found)
					{
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack craft(PedestalEntity inv)
	{
		inv.setStack(this.output.copy());
		inv.sync();
		return output.copy();
	}

	@Override
	public boolean fits(int width, int height)
	{
		return true;
	}

	@Override
	public boolean isIgnoredInRecipeBook()
	{
		return true;
	}

	@Override
	public ItemStack getOutput()
	{
		return this.output.copy();
	}

	@Override
	public Identifier getId()
	{
		return this.id;
	}

	public List<Ingredient> getInputs()
	{
		return inputs;
	}

	public Ingredient getCatalyst()
	{
		return catalyst;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return AlchemyRecipes.PEDESTAL_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return AlchemyRecipes.PEDESTAL_RECIPE;
	}

	public static class Serializer implements RecipeSerializer<PedestalRecipe>
	{
		private final static String INPUTS = "inputs";
		private final static String CATALYST = "catalyst";
		private final static String OUTPUT = "output";
		private final static String ASPECTS = "aspects";

		private final PedestalRecipe.Serializer.RecipeFactory<PedestalRecipe> recipeFactory;

		public Serializer(PedestalRecipe.Serializer.RecipeFactory<PedestalRecipe> recipeFactory)
		{
			this.recipeFactory = recipeFactory;
		}

		@Override
		public PedestalRecipe read(Identifier id, JsonObject json)
		{
			JsonArray jsonList = json.getAsJsonArray(INPUTS);
			List<Ingredient> list = new ArrayList<>(jsonList.size());
			for(int i = 0; i < jsonList.size(); i++)
			{
				list.add(Ingredient.fromJson(jsonList.get(i)));
			}
			JsonArray aspectsJson = json.getAsJsonArray(ASPECTS);
			List<AspectStack> aspects = new ArrayList<>();
			if(aspectsJson != null)
			{
				for(int i = 0; i < aspectsJson.size(); i++)
				{
					JsonObject object = aspectsJson.get(i).getAsJsonObject();
					AspectStack stack = new AspectStack(Aspects.getAspectById(Identifier.tryParse(object.get("aspect").getAsString())), object.get("count").getAsInt());
					aspects.add(stack);
				}
			}
			Ingredient catalyst = Ingredient.fromJson(json.getAsJsonObject(CATALYST));
			ItemStack output = new ItemStack(Registry.ITEM.get(Identifier.tryParse(json.get(OUTPUT).getAsString())));
			return new PedestalRecipe(id, ImmutableList.copyOf(list), ImmutableList.copyOf(aspects), catalyst, output);
		}

		@Override
		public PedestalRecipe read(Identifier id, PacketByteBuf buf)
		{
			int count = buf.readInt();
			List<Ingredient> list = new ArrayList<>(count);
			for(int i = 0; i < count; i++)
			{
				list.add(Ingredient.fromPacket(buf));
			}
			List<AspectStack> aspects = new ArrayList<>(count);
			count = buf.readInt();
			for(int i = 0; i < count; i++)
			{
				AspectStack stack = new AspectStack(Aspects.getAspectById(Identifier.tryParse(buf.readString())), buf.readInt());
				aspects.add(stack);
			}
			Ingredient catalyst = Ingredient.fromPacket(buf);
			ItemStack output = buf.readItemStack();
			return new PedestalRecipe(id, ImmutableList.copyOf(list), ImmutableList.copyOf(aspects), catalyst, output);
		}

		@Override
		public void write(PacketByteBuf buf, PedestalRecipe recipe)
		{
			buf.writeInt(recipe.getInputs().size());
			for(int i = 0; i < recipe.getInputs().size(); i++)
			{
				recipe.getInputs().get(i).write(buf);
			}
			buf.writeInt(recipe.aspects.size());
			for(int i = 0; i < recipe.aspects.size(); i++)
			{
				buf.writeString(recipe.aspects.get(i).getAspect().toString());
				buf.writeInt(recipe.aspects.get(i).getCount());
			}
			recipe.getCatalyst().write(buf);
			buf.writeItemStack(recipe.getOutput());
		}

		public interface RecipeFactory<T extends Recipe<?>>
		{
			T create(Identifier id, List<Ingredient> inputs, List<AspectStack> aspects, Ingredient catalyst, ItemStack output);
		}
	}

	public static class StageTracker
	{
		private World world;
		private BlockPos pos;
		private PedestalRecipe recipe;
		private PedestalEntity primaryEntity;
		private Stage stage;
		private List<AspectStack> neededAspects;
		private List<Ingredient> neededItems;
		private PedestalEntity currentItemEntity;
		private int currentItem;
		private AspectStack currentAspect;
		private JarEntity currentJarEntity;
		private int itemTick = 0;
		private int aspectTick = 0;
		private int stageDelayTicks = 0;
		private CompoundTag tag;

		public StageTracker(PedestalEntity primaryEntity, World world, BlockPos pos, PedestalRecipe recipe)
		{
			this.primaryEntity = primaryEntity;
			this.world = world;
			this.pos = pos;
			this.stage = Stage.START;
			this.recipe = recipe;
			this.neededItems = new ArrayList<>(recipe.getInputs());
			this.neededAspects = new ArrayList<>(recipe.aspects);
		}

		public StageTracker(PedestalEntity primaryEntity, World world, BlockPos pos, CompoundTag tag)
		{
			this.primaryEntity = primaryEntity;
			this.world = world;
			this.pos = pos;
			this.tag = tag;
		}

		public void setWorld(World world)
		{
			this.world = world;
		}

		public CompoundTag toTag(CompoundTag tag)
		{
			tag.putString("stage", this.stage.name());
			tag.putString("recipe", this.recipe.getId().toString());
			ListTag items = new ListTag();
			ListTag aspects = new ListTag();
			for(int i = 0; i < this.neededItems.size(); i++)
			{
				items.add(StringTag.of(this.neededItems.get(i).toJson().toString()));
			}
			for(int i = 0; i < this.neededAspects.size(); i++)
			{
				CompoundTag aspectTag = new CompoundTag();
				aspectTag.putString("aspect", this.neededAspects.get(i).getAspect().getId().toString());
				aspectTag.putInt("count", this.neededAspects.get(i).getCount());
				aspects.add(aspectTag);
			}
			tag.put("items", items);
			tag.put("aspects", aspects);
			return tag;
		}

		public void fromTag()
		{
			this.stage = Stage.valueOf(tag.getString("stage"));
			this.recipe = (PedestalRecipe) this.world.getRecipeManager().get(Identifier.tryParse(tag.getString("recipe"))).get();
			this.neededItems = new ArrayList<>();
			this.neededAspects = new ArrayList<>();
			ListTag items = tag.getList("items", NbtType.STRING);
			ListTag aspects = tag.getList("aspects", NbtType.COMPOUND);
			for(int i = 0; i < items.size(); i++)
			{
				this.neededItems.add(Ingredient.fromJson(new JsonParser().parse(items.getString(i))));
			}
			for(int i = 0; i < aspects.size(); i++)
			{
				CompoundTag aspectTag = aspects.getCompound(i);
				AspectStack stack = new AspectStack(Aspects.getAspectById(Identifier.tryParse(aspectTag.getString("aspect"))), aspectTag.getInt("count"));
				this.neededAspects.add(stack);
			}
			this.tag = null;
		}

		private boolean checkAndConsumeAspect()
		{
			if(this.neededAspects.isEmpty())
			{
				return false;
			}
			else
			{
				if(this.aspectTick >= 10)
				{
					aspectTick = 0;
					if(this.currentJarEntity == null)
					{
						List<JarEntity> jars = getNearbyJars(pos, world);
						boolean found = false;
						for(int i = 0; i < jars.size(); i++)
						{
							for(int o = 0; o < neededAspects.size(); o++)
							{
								if(!jars.get(i).isEmpty())
								{
									AspectStack stack = jars.get(i).getAspects().values().stream().findFirst().get();
									if(stack.getAspect() == neededAspects.get(i).getAspect())
									{
										this.currentJarEntity = jars.get(i);
										this.currentAspect = neededAspects.get(i).copy();
										neededAspects.set(i, this.currentAspect);
										found = true;
										break;
									}
								}
							}
							if(found) break;
						}
					}
					else if(!this.currentJarEntity.isRemoved())
					{
						if(!this.currentJarEntity.isEmpty())
						{
							AspectStack jarStack = this.currentJarEntity.getAspect(currentAspect.getAspect());
							if(!jarStack.isEmpty())
							{
								jarStack.decrement(1);
								currentAspect.decrement(1);
								if(currentAspect.isEmpty())
								{
									this.neededAspects.removeIf((aspectStack -> aspectStack.getAspect() == currentAspect.getAspect()));
								}
								this.currentJarEntity.markDirty();
								this.primaryEntity.markDirty();
								this.currentJarEntity.sync();
							}
						}
					}
				}
				if(this.currentJarEntity != null)
				{
					if(this.currentJarEntity.isRemoved())
					{
						this.currentJarEntity = null;
					}
				}
				aspectTick++;
			}
			return true;
		}

		private boolean checkAndConsumeItem()
		{
			if(this.neededItems.isEmpty())
			{
				return false;
			}
			else
			{
				if(this.itemTick >= 25)
				{
					itemTick = 0;
					if(this.currentItemEntity == null)
					{
						List<PedestalEntity> pedestals = getNearbyPedestals(pos, world);
						boolean found = false;
						for(int i = 0; i < pedestals.size(); i++)
						{
							for(int o = 0; o < neededItems.size(); o++)
							{
								if(neededItems.get(o).test(pedestals.get(i).getStack()))
								{
									this.currentItemEntity = pedestals.get(i);
									this.currentItemEntity.setCrafting(true);
									this.currentItemEntity.sync();
									currentItem = o;
									found = true;
									break;
								}
							}
							if(found) break;
						}
					}
					else if(!this.currentItemEntity.isRemoved())
					{
						this.neededItems.remove(currentItem);
						this.currentItemEntity.setCrafting(false);
						this.currentItemEntity.setStack(ItemStack.EMPTY);
						this.currentItemEntity.markDirty();
						this.currentItemEntity.sync();
						this.primaryEntity.markDirty();
						this.currentItemEntity = null;
					}
				}
				if(this.currentItemEntity != null)
				{
					if(this.currentItemEntity.isRemoved())
					{
						this.currentItemEntity = null;
					}
				}
				itemTick++;
			}
			return true;
		}

		public void tick()
		{
			if(this.stage == Stage.START)
			{
				this.primaryEntity.setMain(true);
				this.primaryEntity.sync();
				this.stage = Stage.ASPECT;
			}
			else if(this.stage == Stage.ASPECT && stageDelayTicks > 25)
			{
				if(!checkAndConsumeAspect())
				{
					this.stage = Stage.ITEM;
					stageDelayTicks = 0;
				}
			}
			else if(this.stage == Stage.ITEM && stageDelayTicks > 25)
			{
				if(!checkAndConsumeItem())
				{
					this.stage = Stage.FINISH;
					stageDelayTicks = 0;
				}
			}
			else if(this.stage == Stage.FINISH && stageDelayTicks > 25)
			{
				this.primaryEntity.setMain(false);
				this.recipe.craft(primaryEntity);
				this.primaryEntity.setCraftingFinished(true);
				world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.AMBIENT, 10000.0F, 1.5F);
				this.stage = Stage.END;
			}
			stageDelayTicks++;
		}

		public Stage getStage()
		{
			return this.stage;
		}

		public enum Stage
		{
			START,
			ASPECT,
			ITEM,
			FINISH,
			END
		}
	}
}
