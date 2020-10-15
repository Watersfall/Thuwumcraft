package com.watersfall.poisonedweapons.blockentity;

import com.watersfall.poisonedweapons.inventory.BrewingCauldronInventory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.loom.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class BrewingCauldronEntity extends BlockEntity implements BrewingCauldronInventory, BlockEntityClientSerializable
{
    private final DefaultedList<ItemStack> contents = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private short waterLevel;
    private byte ingredientCount;
    public float lastWaterLevel = 0;

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
        this.waterLevel = tag.getShort("water_level");
        this.ingredientCount = tag.getByte("ingredient_count");
        this.lastWaterLevel = waterLevel + ((float)this.ingredientCount * (1F / 32F));
        Inventories.fromTag(tag, this.contents);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        super.toTag(tag);
        tag.putShort("water_level", waterLevel);
        tag.putByte("ingredient_count", ingredientCount);
        Inventories.toTag(tag, this.contents);
        return tag;
    }

    public short getWaterLevel()
    {
        return waterLevel;
    }

    public void setWaterLevel(short waterLevel)
    {
        if(waterLevel < 5)
        {
            waterLevel = 0;
        }
        this.waterLevel = waterLevel;
        markDirty();
    }

    public byte getIngredientCount()
    {
        return ingredientCount;
    }

    public void setIngredientCount(byte ingredientCount)
    {
        this.ingredientCount = ingredientCount;
        markDirty();
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag)
    {
        this.waterLevel = compoundTag.getShort("water_level");
        this.ingredientCount = compoundTag.getByte("ingredient_count");
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag)
    {
        compoundTag.putShort("water_level", waterLevel);
        compoundTag.putByte("ingredient_count", ingredientCount);
        return compoundTag;
    }

    @Environment(EnvType.CLIENT)
    public float getAnimationProgress(float tickDelta)
    {
        return MathHelper.lerp(tickDelta, this.lastWaterLevel, ((float)this.waterLevel + ((float)this.ingredientCount * (1000F / 16F))));
    }
}
