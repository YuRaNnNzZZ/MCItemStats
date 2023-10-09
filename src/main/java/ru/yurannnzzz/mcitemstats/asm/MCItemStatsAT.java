package ru.yurannnzzz.mcitemstats.asm;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

public class MCItemStatsAT extends AccessTransformer {
    public MCItemStatsAT() throws IOException {
        super("mcitemstats_at.cfg");
    }
}
