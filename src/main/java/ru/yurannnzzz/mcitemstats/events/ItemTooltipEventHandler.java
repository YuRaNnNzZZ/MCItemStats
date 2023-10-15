package ru.yurannnzzz.mcitemstats.events;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ISpecialArmor;
import ru.yurannnzzz.mcitemstats.util.LangUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemTooltipEventHandler {
    private static final Map<ItemFood, PotionEffect> potionEffectCache = new HashMap<ItemFood, PotionEffect>();
    private static GuiScreen lastGui = null;

    public static void handleTooltip(ItemStack stack, EntityPlayer player, boolean advancedTooltips, ArrayList tooltip) {
        /*if (advancedTooltips) {
            for (Object line : tooltip) {
                if (line.equals("Durability: " + (stack.getMaxDamage() - stack.getItemDamageForDisplay()) + " / " + stack.getMaxDamage())) {
                    tooltip.remove(line);

                    break;
                }
            }
        }*/

        Item item = stack.getItem();

        if (item instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) item;

            int reduction = armor.damageReduceAmount;

            if (item instanceof ISpecialArmor) {
                ISpecialArmor sArmor = (ISpecialArmor) item;

                reduction = sArmor.getArmorDisplay(player, stack, armor.armorType);
            }

            if (reduction > 0) {
                tooltip.add("\u00A7b" + LangUtils.format("gui.mcitemstats.protection", reduction));
            }
        }

        int damage = item.getDamageVsEntity(null);
        if (damage > 1) {
            tooltip.add("\u00A73" + LangUtils.format("gui.mcitemstats.attackdamage", damage));
        }

        if (item instanceof ItemTool) {
            ItemTool tool = (ItemTool) item;

            EnumToolMaterial material = EnumToolMaterial.valueOf(tool.getToolMaterialName());

            tooltip.add("\u00A72" + LangUtils.format("gui.mcitemstats.harvestlevel", material.getHarvestLevel()));
            tooltip.add("\u00A72" + LangUtils.format("gui.mcitemstats.harvestspeed", tool.efficiencyOnProperMaterial));
        }

        if (item instanceof ItemFood) {
            ItemFood food = (ItemFood) item;

            int healAmount = food.getHealAmount();
            float saturationModifier = food.getSaturationModifier();

            if (healAmount > 0) {
                tooltip.add("\u00A72" + LangUtils.format("gui.mcitemstats.food.heal", healAmount));

                if (saturationModifier > 0.0f) {
                    tooltip.add("\u00A76" + LangUtils.format("gui.mcitemstats.food.saturation", healAmount * saturationModifier * 2f));
                }
            }

            if (food.potionId > 0 && food.potionEffectProbability > 0.0F) {
                if (lastGui == null || !lastGui.equals(FMLClientHandler.instance().getClient().currentScreen)) {
                    lastGui = FMLClientHandler.instance().getClient().currentScreen;
                    potionEffectCache.clear();
                }

                if (!potionEffectCache.containsKey(food)) {
                    potionEffectCache.put(food, new PotionEffect(food.potionId, food.potionDuration * 20, food.potionAmplifier));
                }

                PotionEffect potionEffect = potionEffectCache.get(food);

                String potionName = StatCollector.translateToLocal(potionEffect.getEffectName()).trim();
                if (potionEffect.getAmplifier() > 0) {
                    potionName = potionName + " " + StatCollector.translateToLocal("potion.potency." + potionEffect.getAmplifier()).trim();
                }

                tooltip.add("\u00A7" + (Potion.potionTypes[food.potionId].isBadEffect() ? "4" : "b") + LangUtils.format("gui.mcitemstats.food.potion", potionName, Potion.getDurationString(potionEffect), Math.round(food.potionEffectProbability * 100.0F)));
            }
        }

        if (stack.isItemStackDamageable()) {
            tooltip.add("\u00A77" + LangUtils.format("gui.mcitemstats.durability", stack.getMaxDamage() - stack.getItemDamageForDisplay(), stack.getMaxDamage()));
        }
    }
}
