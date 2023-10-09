package ru.yurannnzzz.mcitemstats;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions("ru.yurannnzzz.mcitemstats")
public class MCItemStatsCoreMod implements IFMLLoadingPlugin {
    @Override
    public String[] getLibraryRequestClass() {
        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {
                "ru.yurannnzzz.mcitemstats.asm.MCItemStatsAT",
                "ru.yurannnzzz.mcitemstats.asm.ItemStackTransformer"
        };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }
}
