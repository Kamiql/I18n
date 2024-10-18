package de.kamiql.i18n.core.source;

import de.kamiql.i18n.api.provider.I18nProvider;
import de.kamiql.i18n.core.annotations.Optional;
import de.kamiql.i18n.core.annotations.Required;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This is the core of my internationalization plugin - Build your custom messages!
 */
public class I18n {

    private final String key;
    private final Player player;
    private final boolean hasPrefix;
    private final String customPrefix;
    private final Map<String, Object> placeholders;

    private final YamlConfiguration config = I18nProvider.getConfig();

    private I18n(Builder builder) {
        this.key = builder.key;
        this.player = builder.player;
        this.hasPrefix = builder.hasPrefix;
        this.customPrefix = builder.customPrefix;
        this.placeholders = builder.placeholders;
    }

    /**
     * Send the message, translated with Minimessage
     */
    public void sendMessageAsComponent() {
        player.sendMessage(getMessageAsComponent());
    }

    /**
     * Broadcast the message, translated with Minimessage
     */
    public void broadcastMessageAsComponent() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(getMessageAsComponent());
        }
    }

    /**
     * Get the message, translated with Minimessage
     * @return the message as Component
     */
    public Component getMessageAsComponent() {
        return MiniMessage.miniMessage().deserialize(getFormattedMessage());
    }

    private String getFormattedMessage() {
        String locale = player.getLocale().toLowerCase(Locale.ROOT).replace("_", "-");

        YamlConfiguration config = I18nProvider.getConfig();
        List<String> messages = config.getStringList("translations." + key + "." + locale);

        if (messages.isEmpty()) {
            messages = config.getStringList("translations." + key + "." + config.getString("defaultLocale", "en"));
        }

        if (messages.isEmpty()) {
            return "<gray>No Message for key \"<yellow>" + key + "<gray>\"!";
        }

        String message = String.join("\n", messages);

        if (hasPrefix) {
            String prefix = (customPrefix != null) ? (customPrefix + " ") : config.getString("prefix");

            String[] messageLines = message.split("\n");
            StringBuilder prefixedMessageBuilder = new StringBuilder();

            for (String line : messageLines) {
                prefixedMessageBuilder.append(prefix).append(line).append("\n");
            }

            message = prefixedMessageBuilder.toString().trim();
        }

        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            String placeholderKey = entry.getKey();
            Object placeholderValue = entry.getValue();
            String placeholderString = placeholderValue.toString();
            message = message.replace("{" + placeholderKey + "}", placeholderString);
        }

        return message;
    }



    /**
     * This is the I18n builder, which is used to create messages based on the players local language!
     *
     * @author kamiql
     * @apiNote the current builder may change
     */
    public static class Builder {
        private final String key;
        private final Player player;
        private boolean hasPrefix = false;
        private String customPrefix = null;
        private final Map<String, Object> placeholders = new HashMap<>();

        public Builder(@NotNull String key, @NotNull Player player) {
            this.key = key;
            this.player = player;
        }

        /**
         * Should the message have a prefix?
         *
         * @param hasPrefix Default: false
         */
        @Optional
        public Builder hasPrefix(boolean hasPrefix) {
            this.hasPrefix = hasPrefix;
            return this;
        }

        /**
         * Set a custom message prefix
         *
         * @param prefix The choosen prefix
         */
        @Optional
        public Builder withPrefix(@NotNull String prefix) {
            this.customPrefix = prefix;
            return this;
        }

        /**
         * Select custom Placeholders e.g:
         * #withPlaceholder("PLAYER", Object)
         * Make sure you don´t include the {} brackets
         *
         * @param key Placeholder name
         * @param value Any replacement
         */
        @Optional
        public Builder withPlaceholder(@NotNull String key, @NotNull Object value) {
            this.placeholders.put(key, value);
            return this;
        }

        /**
         * Build the message
         */
        @Required
        public I18n build() {
            return new I18n(this);
        }
    }
}
