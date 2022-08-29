package ru.yurannnzzz.mcitemstats.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import ru.yurannnzzz.mcitemstats.events.ItemTooltipEventHandler;

public class ClientProxy extends CommonProxy {
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        MinecraftForge.EVENT_BUS.register(new ItemTooltipEventHandler());
    }
}
