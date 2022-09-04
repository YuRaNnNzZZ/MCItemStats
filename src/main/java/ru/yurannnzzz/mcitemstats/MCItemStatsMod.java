package ru.yurannnzzz.mcitemstats;

import cpw.mods.fml.common.SidedProxy;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import ru.yurannnzzz.mcitemstats.proxy.CommonProxy;

@Mod(
        modid = MCItemStatsMod.MODID,
        acceptableRemoteVersions = "*",
        useMetadata = true
)
public class MCItemStatsMod
{
    public static final String MODID = "mcitemstats";

    @SidedProxy(clientSide = "ru.yurannnzzz.mcitemstats.proxy.ClientProxy", serverSide = "ru.yurannnzzz.mcitemstats.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }
}
