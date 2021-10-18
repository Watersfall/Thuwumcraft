package net.watersfall.thuwumcraft.entity.spell;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.registry.ThuwumcraftEntities;

import java.util.UUID;

public class WaterEntity extends Entity
{
	protected Box particleBox;
	protected UUID ownerUUID;
	protected Entity owner;

	public WaterEntity(World world, Entity owner)
	{
		super(ThuwumcraftEntities.WATER_ENTITY, world);
		if(owner != null)
		{
			this.owner = owner;
			this.ownerUUID = owner.getUuid();
		}

	}

	public WaterEntity(EntityType<? extends WaterEntity> type, World world)
	{
		super(type, world);
	}

	@Override
	public void tick()
	{
		super.tick();
		double scale = age / 30F - 1.5;
		this.particleBox = this.getBoundingBox().expand(scale);
		this.noClip = true;
		this.move(MovementType.SELF, this.getVelocity());
		this.addVelocity(0, -0.01, 0);
		if(world.isClient)
		{
			for(int i = 0; i < (this.age / 10) + 1; i++)
			{
				double x = particleBox.minX + Math.random() * particleBox.getXLength();
				double y = particleBox.minY + Math.random() * particleBox.getYLength();
				double z = particleBox.minZ + Math.random() * particleBox.getZLength();
				Particle particle =  MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, this.getVelocity().x / 2, this.getVelocity().y / 2, this.getVelocity().z / 2);
				particle.setMaxAge(10);
				particle.setColor((this.age / 70F), this.age / 70F, 1F);
			}
		}
		if(this.age > 40)
		{
			this.remove(RemovalReason.DISCARDED);
		}
		int x1 = (int)(this.getBoundingBox().minX);
		int y1 = (int)(this.getBoundingBox().minY);
		int z1 = (int)(this.getBoundingBox().minZ);
		int x2 = (int)(this.getBoundingBox().maxX);
		int y2 = (int)(this.getBoundingBox().maxY);
		int z2 = (int)(this.getBoundingBox().maxZ);
		BlockPos.Mutable pos = new BlockPos.Mutable();
		int blocksHit = 0;
		for(int x = x1; x <= x2; x++)
		{
			for(int y = y1; y <= y2; y++)
			{
				for(int z = z1; z <= z2; z++)
				{
					pos.set(x, y, z);
					BlockState state = world.getBlockState(pos);
					if(!state.isAir())
					{
						blocksHit++;
					}
					if(state.isOf(Blocks.FIRE))
					{
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
					}
				}
			}
		}
		if(!world.isClient)
		{
			world.getOtherEntities(this, this.getBoundingBox(), this::checkIfValidEntity).forEach(Entity::extinguish);
		}
	}

	private boolean checkIfValidEntity(Entity entity)
	{
		if(entity.isSpectator())
		{
			return false;
		}
		if(owner != null)
		{
			return entity.getId() != owner.getId();
		}
		if(ownerUUID != null && entity.getUuid().equals(ownerUUID))
		{
			this.owner = entity;
			return false;
		}
		return true;
	}

	@Override
	protected void onBlockCollision(BlockState state)
	{

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
