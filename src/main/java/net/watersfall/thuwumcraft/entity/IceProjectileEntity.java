package net.watersfall.thuwumcraft.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;
import net.watersfall.thuwumcraft.registry.ThuwumcraftEntities;

public class IceProjectileEntity extends ThrownItemEntity
{
	public IceProjectileEntity(World world, LivingEntity owner)
	{
		super(ThuwumcraftEntities.ICE_PROJECTILE, owner, world);
	}

	public IceProjectileEntity(EntityType<IceProjectileEntity> type, World world)
	{
		super(type, world);
	}

	@Override
	protected Item getDefaultItem()
	{
		return ThuwumcraftItems.ICE_PROJECTILE_ITEM;
	}



	@Override
	protected void onEntityHit(EntityHitResult entityHitResult)
	{
		super.onEntityHit(entityHitResult);
		if(entityHitResult.getEntity() != this.getOwner() && !this.world.isClient)
		{
			if(entityHitResult.getEntity() instanceof LivingEntity)
			{
				LivingEntity entity = (LivingEntity)entityHitResult.getEntity();
				entity.damage(DamageSource.FREEZE, 6.0F);
				if(entity.canFreeze())
				{
					entity.setFrozenTicks(entity.getFrozenTicks() + 100);
				}
				this.remove(RemovalReason.DISCARDED);
				world.playSound(null, this.getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
				for(int i = 0; i < 8; i++)
				{
					this.world.addParticle(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult)
	{
		super.onBlockHit(blockHitResult);
		this.remove(RemovalReason.DISCARDED);
		world.playSound(null, this.getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
		for(int i = 0; i < 8; i++)
		{
			this.world.addParticle(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean updateMovementInFluid(Tag<Fluid> tag, double d)
	{
		boolean bool;
		if((bool = super.updateMovementInFluid(tag, d)))
		{
			if(tag == FluidTags.WATER)
			{
				FluidState state = world.getFluidState(this.getBlockPos());
				if(state.isStill())
				{
					world.setBlockState(this.getBlockPos(), Blocks.ICE.getDefaultState());
				}
				this.remove(RemovalReason.DISCARDED);
			}
		}
		return bool;
	}

	@Override
	protected void onCollision(HitResult hitResult)
	{
		super.onCollision(hitResult);
	}

	@Override
	public Packet<?> createSpawnPacket()
	{
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeVarInt(Registry.ENTITY_TYPE.getRawId(this.getType()));
		buf.writeUuid(this.getUuid());
		buf.writeVarInt(this.getId());
		buf.writeDouble(this.getPos().x);
		buf.writeDouble(this.getPos().y);
		buf.writeDouble(this.getPos().z);
		buf.writeFloat(this.getPitch());
		buf.writeFloat(this.getYaw());
		if(this.getOwner() == null)
		{
			buf.writeVarInt(-1);
		}
		else
		{
			buf.writeVarInt(this.getOwner().getId());
		}
		return ServerPlayNetworking.createS2CPacket(Thuwumcraft.getId("spawn_packet"), buf);
	}
}
