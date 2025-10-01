package io.tanice.terracraftitems.core.config;

import io.tanice.terracraftitems.bukkit.TerraCraftItems;
import io.tanice.terracraftitems.core.util.logger.TerraCraftLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class ConfigManager {

    private static final String RESOURCE_FOLDER = "/config/";

    private static double version;
    private static boolean debug;
    private static boolean generateExamples;
    private static Map<String, Boolean> oriUpdateConfigMap;

    public static void load() {
        File configFile = new File(TerraCraftItems.inst().getDataFolder(), "config.yml");
        if (!configFile.exists()) generateExampleConfig();
        else if (generateExamples) generateExampleConfig();

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);
        ConfigurationSection sub;

        version = cfg.getDouble("VERSION", -1);
        debug = cfg.getBoolean("DEBUG", false);
        generateExamples = cfg.getBoolean("generate_examples", true);

        oriUpdateConfigMap = new HashMap<>();
        sub = cfg.getConfigurationSection("update");
        if (sub == null) TerraCraftLogger.error("Global configuration file error, there is no update config section");
        else for (String key : sub.getKeys(false)) oriUpdateConfigMap.put(key, sub.getBoolean(key, false));
    }

    public static void reload() {
        load();
    }

    public static void unload() {
        oriUpdateConfigMap.clear();
    }

    public static double getVersion() {
        return version;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static boolean shouldGenerateExamples() {
        return generateExamples;
    }

    public static Map<String, Boolean> getOriUpdateConfigMap() {
        return oriUpdateConfigMap;
    }

    private static void generateExampleConfig() {
        File targetFolder = TerraCraftItems.inst().getDataFolder();
        URL sourceUrl = TerraCraftItems.inst().getClass().getResource("");
        if (sourceUrl == null) {
            TerraCraftLogger.error("The plugin package is incomplete, please re_download it!");
            return;
        }

        try (FileSystem fs = FileSystems.newFileSystem(sourceUrl.toURI(), Collections.emptyMap())) {
            Path cp = fs.getPath(RESOURCE_FOLDER);
            try (Stream<Path> sourcePaths = Files.walk(cp)) {
                for (Path source : sourcePaths.toArray(Path[]::new)) {
                    Path targetPath = targetFolder.toPath().resolve(cp.relativize(source).toString());
                    if (Files.exists(targetPath)) continue;
                    if (Files.isDirectory(source)) Files.createDirectory(targetPath);
                    else Files.copy(source, targetPath);
                }
                TerraCraftLogger.success("Example config files generated successfully!");
            }
        } catch (IOException | URISyntaxException e) {
            TerraCraftLogger.error("Failed to load default example config file: " + e.getMessage());
        }
    }
}
