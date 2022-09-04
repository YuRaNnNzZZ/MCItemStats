package ru.yurannnzzz.mcitemstats.events;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class ItemTooltipEventHandler {
    private final Map<ItemFood, PotionEffect> potionEffectCache = new HashMap<ItemFood, PotionEffect>();
    private final Map<String, Block> toolClasses;

    public ItemTooltipEventHandler() {
        toolClasses = new HashMap<String, Block>();
        toolClasses.put("pickaxe", Blocks.stone);
        toolClasses.put("shovel", Blocks.dirt);
        toolClasses.put("axe", Blocks.log);
        toolClasses.put("sickle", Blocks.leaves);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void handleItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.itemStack;
        Item item = stack.getItem();

        if (event.showAdvancedItemTooltips && stack.isItemDamaged()) {
            String origDurabilityLine = "Durability: " + (stack.getMaxDamage() - stack.getItemDamageForDisplay()) + " / " + stack.getMaxDamage();

            for (String line : event.toolTip) {
                if (line.equals(origDurabilityLine)) {
                    event.toolTip.remove(line);

                    break;
                }
            }
        }

        if (item instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) item;

            int reduction = armor.getArmorMaterial().getDamageReductionAmount(armor.armorType);

            if (reduction > 0) {
                event.toolTip.add(EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted("gui.mcitemstats.protection", reduction));
            }
        }

        if (item instanceof ItemTool) {
            ItemTool tool = (ItemTool) item;

            for (String toolClass : tool.getToolClasses(stack)) {
                event.toolTip.add(EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocalFormatted("gui.mcitemstats.harvestlevel", tool.getHarvestLevel(stack, toolClass), toolClass));

                if (toolClasses.containsKey(toolClass)) {
                    event.toolTip.add(EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocalFormatted("gui.mcitemstats.harvestspeed", tool.getDigSpeed(stack, toolClasses.get(toolClass), 0), toolClass));
                }
            }
        }

        if (item instanceof ItemFood) {
            ItemFood food = (ItemFood) item;

            int healAmount = food.func_150905_g(stack);
            float saturationModifier = food.func_150906_h(stack);

            if (healAmount > 0) {
                event.toolTip.add(EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocalFormatted("gui.mcitemstats.food.heal", healAmount));

                if (saturationModifier > 0.0F) {
                    event.toolTip.add(EnumChatFormatting.GOLD + StatCollector.translateToLocalFormatted("gui.mcitemstats.food.saturation", String.format("%.1f", healAmount * saturationModifier * 2.0F)));
                }
            }

            int potionId = ObfuscationReflectionHelper.getPrivateValue(ItemFood.class, food, "potionId");
            float potionEffectProbability = ObfuscationReflectionHelper.getPrivateValue(ItemFood.class, food, "potionEffectProbability");

            if (potionId > 0 && potionEffectProbability > 0.0F) {
                if (!potionEffectCache.containsKey(food)) {
                    int potionDuration = ObfuscationReflectionHelper.getPrivateValue(ItemFood.class, food, "potionDuration");
                    int potionAmplifier = ObfuscationReflectionHelper.getPrivateValue(ItemFood.class, food, "potionAmplifier");

                    potionEffectCache.put(food, new PotionEffect(potionId, potionDuration * 20, potionAmplifier));
                }

                PotionEffect potionEffect = potionEffectCache.get(food);

                String potionName = StatCollector.translateToLocal(potionEffect.getEffectName()).trim();
                if (potionEffect.getAmplifier() > 0) {
                    potionName = potionName + " " + StatCollector.translateToLocal("potion.potency." + potionEffect.getAmplifier()).trim();
                }

                event.toolTip.add((Potion.potionTypes[potionEffect.getPotionID()].isBadEffect() ? EnumChatFormatting.RED : EnumChatFormatting.AQUA) + StatCollector.translateToLocalFormatted("gui.mcitemstats.food.potion", potionName, Potion.getDurationString(potionEffect), Math.round(potionEffectProbability * 100.0F)));
            }
        }

        if (stack.isItemStackDamageable()) {
            event.toolTip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("gui.mcitemstats.durability", stack.getMaxDamage() - stack.getItemDamageForDisplay(), stack.getMaxDamage()));
        }
    }

    @SubscribeEvent
    public void onGuiScreenOpen(GuiOpenEvent event) {
        this.potionEffectCache.clear();
    }
}
