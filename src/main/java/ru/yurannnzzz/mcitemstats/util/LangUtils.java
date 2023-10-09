package ru.yurannnzzz.mcitemstats.util;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.util.StringTranslate;
import ru.yurannnzzz.mcitemstats.MCItemStatsMod;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.logging.Logger;

public class LangUtils {
    private static final Logger LOGGER = Logger.getLogger(MCItemStatsMod.MODID);
    private static final LanguageRegistry registry = LanguageRegistry.instance();

    static {
        LOGGER.setParent(FMLLog.getLogger());
    }

    public static void load(String path) {
        for (Object key : StringTranslate.getInstance().getLanguageList().keySet()) {
            String lang = (String) key;

            InputStream inputStream = null;
            Properties properties = new Properties();

            try {
                inputStream = MCItemStatsMod.class.getResourceAsStream(path + lang + ".properties");
                if (inputStream == null) continue;

                properties.load(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                registry.addStringLocalization(properties, lang);
            } catch (Exception e) {
                LOGGER.info("Unable to load language for " + lang + ": " + e.getMessage());
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    // ignored
                }
            }
        }
    }

    public static String format(String key, Object... args) {
        String localized = registry.getStringLocalization(key);
        if (localized.isEmpty()) {
            localized = key;
        }

        return String.format(localized, args);
    }
}
