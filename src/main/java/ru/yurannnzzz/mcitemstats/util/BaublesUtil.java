package ru.yurannnzzz.mcitemstats.util;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class BaublesUtil {
    @Nullable
    public static String getBaubleType(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof IBauble) {
            BaubleType type = ((IBauble) stack.getItem()).getBaubleType(stack);

            if (type != null) {
                return type.name();
            }
        }

        return null;
    }

    public static boolean isBauble(ItemStack stack) {
        return stack != null && stack.getItem() instanceof IBauble;
    }
}
