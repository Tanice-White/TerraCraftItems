package io.tanice.terracraftitems.paper.util.constance;

import org.bukkit.NamespacedKey;

public final class PDCKey {
    public static final String PLUGIN_NAME = "terracraft";
    
    public static final NamespacedKey TERRA_COMPONENT_KEY = new NamespacedKey(PLUGIN_NAME, "component");
    public static final NamespacedKey STATE_KEY = new NamespacedKey(PLUGIN_NAME, "state");
    
    public static final NamespacedKey CONTENT_KEY = new NamespacedKey(PLUGIN_NAME, "content");
    public static final NamespacedKey EXPR_KEY = new NamespacedKey(PLUGIN_NAME,"expr");

    public static final NamespacedKey COMMAND_KEY = new NamespacedKey(PLUGIN_NAME, "command");
    public static final NamespacedKey DURABILITY_KEY = new NamespacedKey(PLUGIN_NAME, "terra_durability");

    public static final NamespacedKey MAX_DAMAGE_KEY = new NamespacedKey(PLUGIN_NAME,"max_damage");
    public static final NamespacedKey DAMAGE_KEY = new NamespacedKey(PLUGIN_NAME,"damage");
    public static final NamespacedKey BREAK_LOSS_KEY = new NamespacedKey(PLUGIN_NAME,"break_loss");

    public static final NamespacedKey TERRA_NAME_KEY = new NamespacedKey(PLUGIN_NAME,"terr_name");

    public static final NamespacedKey UPDATE_CODE_KEY = new NamespacedKey(PLUGIN_NAME,"update_code");
}
