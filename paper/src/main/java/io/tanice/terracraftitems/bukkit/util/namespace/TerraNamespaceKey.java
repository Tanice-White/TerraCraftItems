package io.tanice.terracraftitems.bukkit.util.namespace;

import io.tanice.terracraftitems.bukkit.TerraCraftItems;

import javax.annotation.Nullable;
import java.util.Objects;

public class TerraNamespaceKey {

    private final String namespace;
    private final String key;

    public static TerraNamespaceKey minecraft(String key) {
        return new TerraNamespaceKey("minecraft", key);
    }

    public TerraNamespaceKey(String key) {
        this.namespace = TerraCraftItems.inst().getName().toLowerCase();
        this.key = key;
    }

    public TerraNamespaceKey(String namespace, String key) {
        this.namespace = namespace;
        this.key = key;
    }

    @Nullable
    public static TerraNamespaceKey from(String namespaceKey) {
        if (namespaceKey == null) return null;
        if (namespaceKey.contains(":")) {
            String[] split = namespaceKey.split(":");
            return new TerraNamespaceKey(split[0], split[1]);
        } else return TerraNamespaceKey.minecraft(namespaceKey);
    }

    public String get() {
        return this.namespace + ":" + this.key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, key);
    }
}
