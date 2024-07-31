package dev.luxmiyu.anything.item;

import dev.luxmiyu.anything.material.Material;
import dev.luxmiyu.anything.material.Tier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

import java.util.List;

public class AnyArmorItem extends ArmorItem {
    final Tier tier;

    public AnyArmorItem(Tier tier, RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);

        this.tier = tier;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Material.appendTooltip(this, stack, context, tooltip, type, tier);
    }
}
