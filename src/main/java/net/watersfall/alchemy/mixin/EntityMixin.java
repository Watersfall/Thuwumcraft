package net.watersfall.alchemy.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.api.abilities.AbilityClientSerializable;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Optional;

@Mixin(Entity.class)
public class EntityMixin implements AbilityProvider<Entity>
{
	private final HashMap<Identifier, Ability<Entity>> waters_abilities = new HashMap<>();

	@Override
	public void addAbility(Ability<Entity> ability)
	{
		this.waters_abilities.put(ability.getId(), ability);
	}

	@Override
	public void removeAbility(Identifier id)
	{
		this.waters_abilities.remove(id);
	}

	@Override
	public Optional<Ability<Entity>> getAbility(Identifier id)
	{
		if(this.waters_abilities.containsKey(id))
		{
			return Optional.of(this.waters_abilities.get(id));
		}
		return Optional.empty();
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo info)
	{
		this.waters_abilities.values().forEach((ability) -> {
			ability.tick((Entity)(Object)this);
		});
	}

	@Override
	public CompoundTag toNbt(CompoundTag tag)
	{
		this.waters_abilities.values().forEach(value -> tag.put(value.getId().toString(), value.toNbt(new CompoundTag())));
		return tag;
	}

	@Override
	public void fromNbt(CompoundTag tag)
	{
		AbilityProvider.ENTITY_REGISTRY.getIds().forEach((id) -> {
			this.addAbility(AbilityProvider.ENTITY_REGISTRY.create(id, tag));
		});
	}

	@Override
	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeInt(this.waters_abilities.size());
		waters_abilities.values().forEach((value) -> {
			if(value instanceof AbilityClientSerializable)
			{
				((AbilityClientSerializable) value).toPacket(buf);
			}
		});
		return buf;
	}

	@Override
	public void fromPacket(PacketByteBuf buf)
	{
		waters_abilities.clear();
		int size = buf.readInt();
		for(int i = 0; i < size; i++)
		{
			Identifier id = Identifier.tryParse(buf.readString());
			this.waters_abilities.put(id, AbilityProvider.ENTITY_REGISTRY.create(id, buf));
		}
	}
}
