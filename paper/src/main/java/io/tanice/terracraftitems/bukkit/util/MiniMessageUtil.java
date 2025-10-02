package io.tanice.terracraftitems.bukkit.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import javax.annotation.Nullable;

public final class MiniMessageUtil {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    /**
     * 将 String 打包成为 Component
     */
    public static Component serialize(@Nullable String message) {
        if (message == null || message.isEmpty()) return Component.empty();
        return miniMessage.deserialize(message);
    }

    /**
     * 解 Component 成 String
     */
    public static String deserialize (@Nullable Component component) {
        if (component == null) return "";
        return miniMessage.serialize(component);
    }

    /**
     * 将 component 化成 nbt json
     */
    public static String toNBTJson(Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }

    public static String stripAllTags(@Nullable String message) {
        if (message == null) return "";
        return miniMessage.stripTags(message);
    }
}
