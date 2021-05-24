package net.watersfall.thuwumcraft.research;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.watersfall.thuwumcraft.AlchemyMod;
import net.watersfall.thuwumcraft.api.research.Research;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ResearchLoader implements IdentifiableResourceReloadListener
{
	@Override
	public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor)
	{
		CompletableFuture<Collection<Identifier>> resourceFuture = CompletableFuture.supplyAsync(() -> {
			return manager.findResources("research", (string) -> string.endsWith(".json"));
		});
		return resourceFuture.thenCompose(synchronizer::whenPrepared).thenAcceptAsync((collection) -> {
			Research.REGISTRY.clear();
			collection.forEach((id) -> {
				try
				{
					Resource resource = manager.getResource(id);
					JsonElement json = new JsonParser().parse(new InputStreamReader(resource.getInputStream()));
					Research research = new Research(id, json.getAsJsonObject());
					Research.REGISTRY.register(research.getId(), research);
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			});
		}, applyExecutor);
	}

	@Override
	public Identifier getFabricId()
	{
		return AlchemyMod.getId("research");
	}
}
