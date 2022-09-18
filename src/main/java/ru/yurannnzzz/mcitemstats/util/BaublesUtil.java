package ru.yurannnzzz.mcitemstats.util;

import baubles.api.IBauble;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class BaublesUtil {
    @Nullable
    public static String getBaubleType(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof IBauble) {
            return ((IBauble) stack.getItem()).getBaubleType(stack).name();
        }

        return null;
    }

    public static boolean isBauble(ItemStack stack) {
        return stack != null && stack.getItem() instanceof IBauble;
    }
}
