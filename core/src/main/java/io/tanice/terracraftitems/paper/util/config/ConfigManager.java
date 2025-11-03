package io.tanice.terracraftitems.paper.util.config;

import io.tanice.terracraftitems.paper.TerraCraftItems;
import io.tanice.terracraftitems.paper.util.logger.TerraLogger;
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
import java.util.stream.Stream;

public final class ConfigManager {

    private static final String RESOURCE_FOLDER = "/config/";

    private static double version;
    private static boolean debug;
    private static String language;
    private static boolean generateExamples;

    public static void load() {
        File configFile = new File(TerraCraftItems.inst().getDataFolder(), "config.yml");
        if (!configFile.exists()) generateExampleConfig();
        else if (generateExamples) generateExampleConfig();

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);

        version = cfg.getDouble("VERSION", -1);
        debug = cfg.getBoolean("DEBUG", false);
        language = cfg.getString("LANGUAGE", "EN");
        generateExamples = cfg.getBoolean("generate_examples", true);
    }

    public static void reload() {
        load();
    }

    public static void unload() {

    }

    public static double getVersion() {
        return version;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static String getLanguage() {
        return language;
    }

    public static boolean shouldGenerateExamples() {
        return generateExamples;
    }

    private static void generateExampleConfig() {
        File targetFolder = TerraCraftItems.inst().getDataFolder();
        URL sourceUrl = TerraCraftItems.inst().getClass().getResource("");
        if (sourceUrl == null) {
            TerraLogger.error("The plugin package is incomplete, please re_download it");
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
                TerraLogger.success("Example config files generated successfully");
            }
        } catch (IOException | URISyntaxException e) {
            TerraLogger.error("Failed to load default example config file", e);
        }
    }
}
