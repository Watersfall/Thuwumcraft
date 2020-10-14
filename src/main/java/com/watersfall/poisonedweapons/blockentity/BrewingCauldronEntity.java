package com.watersfall.poisonedweapons.blockentity;

import com.watersfall.poisonedweapons.inventory.BrewingCauldronInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

public class BrewingCauldronEntity extends BlockEntity implements BrewingCauldronInventory
{
    private final DefaultedList<ItemStack> contents = DefaultedList.ofSize(3, ItemStack.EMPTY);

    public BrewingCauldronEntity()
    {
        super(AlchemyModBlockEntities.BREWING_CAULDRON_ENTITY);
    }

    @Override
    public DefaultedList<ItemStack> getContents()
    {
        return contents;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, this.contents);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        super.toTag(tag);
        Inventories.toTag(tag, this.contents);
        return tag;
    }
}
