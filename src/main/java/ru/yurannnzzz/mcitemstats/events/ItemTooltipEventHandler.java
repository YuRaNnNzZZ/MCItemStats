package ru.yurannnzzz.mcitemstats.events;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class ItemTooltipEventHandler {
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

        if (stack.isItemStackDamageable()) {
            event.toolTip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("gui.mcitemstats.durability", stack.getMaxDamage() - stack.getItemDamageForDisplay(), stack.getMaxDamage()));
        }
    }
}
