package io.tanice.terracraftitems.core.item;

import io.tanice.terracraftitems.api.item.TerraComponentFactory;
import io.tanice.terracraftitems.api.item.TerraItem;
import io.tanice.terracraftitems.api.item.TerraItemManager;
import io.tanice.terracraftitems.paper.TerraCraftItems;
import io.tanice.terracraftitems.paper.item.ComponentFactory;
import io.tanice.terracraftitems.paper.util.logger.TerraLogger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ItemManager implements TerraItemManager {


    private final ItemProvider provider;
    private final ComponentFactory componentFactory;
    private final ConcurrentMap<String, TerraItem> items;

    public ItemManager() {
        this.provider = new ItemProvider();
        this.componentFactory = ComponentFactory.inst();
        this.items = new ConcurrentHashMap<>();
        Bukkit.getScheduler().runTaskLater(TerraCraftItems.inst(), this::loadResource, 1L);
    }

    public void reload() {
        provider.reload();
        items.clear();
        loadResource();
    }

    public void unload() {
        this.items.clear();
    }

    @Override
    public Collection<String> getItemNames() {
        return Collections.unmodifiableCollection(items.keySet());
    }

    @Override
    public Optional<TerraItem> getItem(String name) {
        if (name == null) return Optional.empty();
        return Optional.ofNullable(items.get(name));
    }

    @Override
    public boolean isTerraItem(String name) {
        return items.containsKey(name);
    }

    @Override
    public Collection<String> filterItems(String name) {
        if (name == null) return getItemNames();
        return items.values().stream()
                .filter(item -> item.getName() != null && item.getName().startsWith(name))
                .map(TerraItem::getName)
                .collect(Collectors.toList());
    }

    @Override
    public TerraComponentFactory getComponentFactory() {
        return componentFactory;
    }

    private void loadResource() {
        Path itemDir = TerraCraftItems.inst().getDataFolder().toPath().resolve("items");
        if (!Files.exists(itemDir) || !Files.isDirectory(itemDir)) {
            TerraLogger.error("Items directory validation failed: " + itemDir + " is not a valid directory");
            return;
        }
        try (Stream<Path> files = Files.list(itemDir)) {
            files.forEach(file -> {
                String fileName = file.getFileName().toString();
                if (fileName.endsWith(".yml")) {
                    ConfigurationSection section = YamlConfiguration.loadConfiguration(file.toFile());
                    for (String k : section.getKeys(false)) {
                        if (items.containsKey(k)) {
                            TerraLogger.error("Existing item: " + k);
                            continue;
                        }
                        provider.createItem(k, section.getConfigurationSection(k)).ifPresent(b -> items.put(k, b));
                    }
                }
            });
            TerraLogger.success("Loaded " + provider.getTotal() + " items");
        } catch (IOException e) {
            TerraLogger.error("Failed to load buffs from " + itemDir.toAbsolutePath() + " " + e.getMessage());
        }
    }
}
