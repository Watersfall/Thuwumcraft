package net.watersfall.thuwumcraft.entity.spell;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.registry.ThuwumcraftEntities;

import java.util.List;
import java.util.UUID;

public class WindEntity extends Entity
{
	protected UUID ownerUUID;
	protected Entity owner;

	public WindEntity(World world)
	{
		this(ThuwumcraftEntities.WIND, world);
	}

	public WindEntity(EntityType<WindEntity> type, World world)
	{
		super(type, world);
	}

	@Override
	public void tick()
	{
		this.noClip = true;
		super.tick();
		this.move(MovementType.SELF, this.getVelocity());
		List<Entity> entities = world.getOtherEntities(this, getBoundingBox().expand(1), entity -> {
			if(owner == null && ownerUUID != null)
			{
				if(entity.getUuid().equals(ownerUUID))
				{
					this.owner = entity;
					return false;
				}
				else
				{
					return !entity.isSpectator() && !(entity instanceof WitherEntity) && !(entity instanceof EnderDragonEntity);
				}
			}
			return entity.getId() != owner.getId() && !entity.isSpectator();
		});
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).setVelocity(getVelocity().multiply(1.5));
		}
		this.checkBlockCollision();
		if(this.age > 40) this.discard();
	}

	@Override
	protected void onBlockCollision(BlockState state)
	{
		if(!state.isAir() && state.getMaterial().isSolid())
		{
			this.discard();
		}
	}

	public void setOwner(Entity entity)
	{
		this.owner = entity;
		this.ownerUUID = entity.getUuid();
	}

	public Entity getOwner()
	{
		return this.owner;
	}

	@Override
	protected void initDataTracker()
	{

	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt)
	{
		if(nbt.contains("owner"))
		{
			this.ownerUUID = nbt.getUuid("owner");
		}
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt)
	{
		if(this.ownerUUID != null)
		{
			nbt.putUuid("owner", ownerUUID);
		}
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
		buf.writeVarInt(this.owner != null ? this.owner.getId() : -1);
		return ServerPlayNetworking.createS2CPacket(Thuwumcraft.getId("spawn_packet"), buf);
	}
}
