package ru.yurannnzzz.mcitemstats;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import ru.yurannnzzz.mcitemstats.util.LangUtils;

@Mod(
        modid = MCItemStatsMod.MODID,
        useMetadata = true
)
public class MCItemStatsMod {
    public static final String MODID = "mcitemstats";

    @Mod.Init
    public void init(FMLInitializationEvent event) {
        LangUtils.load("/lang/" + MODID + "/");
    }
}
