package dev.luxmiyu.anything.item;

import dev.luxmiyu.anything.material.Material;
import dev.luxmiyu.anything.material.Tier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class AnySwordItem extends SwordItem {
    final Tier tier;

    public AnySwordItem(Tier tier, ToolMaterial material, Settings settings) {
        super(material, settings);

        this.tier = tier;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Material.appendTooltip(this, stack, context, tooltip, type, tier);
    }
}
