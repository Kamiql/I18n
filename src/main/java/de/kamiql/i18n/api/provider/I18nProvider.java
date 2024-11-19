package de.kamiql.i18n.api.provider;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.logging.Logger;

public class I18nProvider {
    private final Plugin plugin;

    private static YamlConfiguration config;
    private final Logger logger;
    private final String error = """
                    {message} ->
                    {e}
                    """;

    public I18nProvider(@NotNull Plugin plugin, @Nullable YamlConfiguration config) {
        this.plugin = plugin;
        this.logger = Logger.getLogger(plugin.getName() + " <I18n>");
        this.config = config;

        try {
            if (config != null) {
                logInitialization();
            } else {
                throw new RuntimeException("I18n configuration is null");
            }
        } catch (Exception e) {
            logger.severe(error
                    .replace("{message}", "Couldn´t enable")
                    .replace("{e}", e.getMessage())
            );
        }
    }

    public I18nProvider(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.logger = Logger.getLogger(plugin.getName() + " <I18n>");

        try {
            this.config = loadDefaultConfig();
            logInitialization();
        } catch (Exception e) {
            logger.severe(error
                    .replace("{message}", "Couldn´t load config")
                    .replace("{e}", e.getMessage())
            );
        }
    }

    private YamlConfiguration loadDefaultConfig() {
        plugin.saveResource("language/i18n-default.yml", false);
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "language/i18n-default.yml"));
    }

    private void logInitialization()
    {
        String message =
                """
                
                <gray>===========================================================================================================
                <yellow>        _                                                _____           _                 \s
                <yellow>        | |                                              /  ___|         | |                \s
                <yellow>        | |     __ _ _ __   __ _ _   _  __ _  __ _  ___  \\ --. _   _ ___| |_ ___ _ __ ___  \s
                <yellow>        | |    / _ | '_ \\ / _ | | | |/ _ |/ _ |/ _ \\  --. \\ | | / __| __/ _ \\ '_  _ \\ \s
                <yellow>        | |___| (_| | | | | (_| | |_| | (_| | (_| |  __/ /\\__/ / |_| \\__ \\ ||  __/ | | | | |\s
                <yellow>        \\_____/\\__,_|_| |_|\\__, |\\__,_|\\__,_|\\__, |\\___| \\____/ \\__, |___/\\__\\___|_| |_| |_|\s
                <yellow>                            __/ |             __/ |              __/ |                      \s
                <yellow>                           |___/             |___/              |___/                       \s
                <gray>============================================================================================================
                <gray>Language System by: <aqua>kamiql
                <gray>Version: <aqua>{VERSION}
                <gray>
                <gray><red>NOTE: This is in Development
                <gray>============================================================================================================
                """;

        Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize(message
                .replace("{VERSION}", plugin.getPluginMeta().getVersion())));
    }

    public static YamlConfiguration getConfig() { return config; }
}
