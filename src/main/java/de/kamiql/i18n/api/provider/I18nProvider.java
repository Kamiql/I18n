package de.kamiql.i18n.api.provider;

import de.kamiql.i18n.core.annotations.Required;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.logging.Logger;

public class I18nProvider {
    private final Plugin plugin;
    private static I18nProvider instance;

    private static YamlConfiguration config;
    private final Logger logger;
    private final String error = """
                    {message} ->
                    {e}
                    """;

    public I18nProvider(@NotNull Plugin plugin)
    {
        instance = this;
        this.plugin = plugin;
        this.logger = Logger.getLogger(plugin.getName() + " <I18n>");
    }

    /**
     * Initialize the language system
     *
     * @param config should a custom config be used?
     * @return if the initialization is successfully
     */
    @Required
    public boolean initialize(@Nullable YamlConfiguration config)
    {
        try {
            setupLanguageFile(config);
            logInitialization();
            return true;
        } catch (Exception e) {
            logger.severe(error
                    .replace("{message}", "Couldn´t enable")
                    .replace("{e}", e.getMessage())
            );
            return false;
        }
    }

    private void setupLanguageFile(@Nullable YamlConfiguration yaml)
    {
        File file = (yaml != null)
                ? new File(plugin.getDataFolder(), "language/i18n.yml")
                : new File(plugin.getDataFolder(), "language/i18n-default.yml");

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            if (yaml != null) {
                yaml.save(file);
                plugin.saveResource("language/i18n.yml", true);
                config = yaml;
            } else {
                plugin.saveResource("language/i18n-default.yml", false);
                config = YamlConfiguration.loadConfiguration(file);
            }
        } catch (Exception e) {
            logger.severe(error.replace("{message}", "Failed to handle language file")
                    .replace("{e}", e.getMessage()));
        }
    }

    private void logInitialization()
    {
        String message =
                """
                
                ===========================================================================================================
                         _                                                _____           _                 \s
                        | |                                              /  ___|         | |                \s
                        | |     __ _ _ __   __ _ _   _  __ _  __ _  ___  \\ --. _   _ ___| |_ ___ _ __ ___  \s
                        | |    / _ | '_ \\ / _ | | | |/ _ |/ _ |/ _ \\  --. \\ | | / __| __/ _ \\ '_  _ \\ \s
                        | |___| (_| | | | | (_| | |_| | (_| | (_| |  __/ /\\__/ / |_| \\__ \\ ||  __/ | | | | |\s
                        \\_____/\\__,_|_| |_|\\__, |\\__,_|\\__,_|\\__, |\\___| \\____/ \\__, |___/\\__\\___|_| |_| |_|\s
                                            __/ |             __/ |              __/ |                      \s
                                           |___/             |___/              |___/                       \s
                ============================================================================================================
                Language System by: kamiql
                Version: {VERSION}
                ============================================================================================================
                """;

        logger.info(message
                .replace("{VERSION}", "2.0.15"));
    }

    public static YamlConfiguration getConfig() { return config; }
}
