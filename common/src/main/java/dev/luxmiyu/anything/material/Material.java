package dev.luxmiyu.anything.material;

import com.google.common.base.Suppliers;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class Material implements ToolMaterial {
    private TagKey<Block> inverseTag;
    private int itemDurability;
    private float miningSpeed;
    private float attackDamage;
    private int enchantability;
    private Supplier<Ingredient> repairIngredient;

    public Material() {
        this.inverseTag = null;
        this.itemDurability = 1;
        this.miningSpeed = 1;
        this.attackDamage = 1;
        this.enchantability = 22;
        this.repairIngredient = null;
    }

    public static Material of(Tier miningTier, Tier durabilityTier, Tier speedTier, ItemConvertible repairItem) {
        Material material = new Material();

        switch (miningTier) {
            case FF, F, E: material.inverseTag = BlockTags.INCORRECT_FOR_WOODEN_TOOL; break;
            case D: material.inverseTag = BlockTags.INCORRECT_FOR_STONE_TOOL; break;
            case C: material.inverseTag = BlockTags.INCORRECT_FOR_IRON_TOOL; break;
            case B: material.inverseTag = BlockTags.INCORRECT_FOR_GOLD_TOOL; break;
            case A: material.inverseTag = BlockTags.INCORRECT_FOR_DIAMOND_TOOL; break;
            case S, SS: material.inverseTag = BlockTags.INCORRECT_FOR_NETHERITE_TOOL; break;
            default: throw new IllegalArgumentException("Invalid mining tier: " + miningTier);
        }

        switch (durabilityTier) {
            case FF: material.itemDurability = 1; break;
            case F: material.itemDurability = 4; break;
            case E: material.itemDurability = 32; break;
            case D: material.itemDurability = 64; break;
            case C: material.itemDurability = 128; break;
            case B: material.itemDurability = 256; break;
            case A: material.itemDurability = 1024; break;
            case S: material.itemDurability = 2048; break;
            case SS: material.itemDurability = 16384; break;
            default: throw new IllegalArgumentException("Invalid durability tier: " + durabilityTier);
        }

        switch (speedTier) {
            case FF: material.miningSpeed = 0f; material.attackDamage = 0f; break;
            case F: material.miningSpeed = 1.5f; material.attackDamage = 0f; break;
            case E: material.miningSpeed = 2.5f; material.attackDamage = 0f; break;
            case D: material.miningSpeed = 4f; material.attackDamage = 1f; break;
            case C: material.miningSpeed = 6f; material.attackDamage = 2f; break;
            case B: material.miningSpeed = 8f; material.attackDamage = 3f; break;
            case A: material.miningSpeed = 9f; material.attackDamage = 4f; break;
            case S: material.miningSpeed = 12f; material.attackDamage = 5f; break;
            case SS: material.miningSpeed = 64f; material.attackDamage = 24f; break;
            default: throw new IllegalArgumentException("Invalid speed tier: " + miningTier);
        }

        material.enchantability = 22;

        Objects.requireNonNull(repairItem);
        material.repairIngredient = Suppliers.memoize(() -> Ingredient.ofItems(repairItem));

        return material;
    }

    public static Material getDefault(ItemConvertible repairItem) {
        return Material.of(Tier.C, Tier.B, Tier.C, repairItem);
    }

    public static int getArmorDurabilityMultiplier(Tier tier) {
        return switch (tier) {
            case FF -> 1;
            case F -> 3;
            case E -> 5;
            case D -> 8;
            case C -> 12;
            case B -> 16;
            case A -> 24;
            case S -> 32;
            case SS -> 1024;
        };
    }

    public static Material of(Tier overallTier, ItemConvertible repairItem) {
        return of(overallTier, overallTier, overallTier, repairItem);
    }

    public int getDurability() {
        return this.itemDurability;
    }

    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    public TagKey<Block> getInverseTag() {
        return this.inverseTag;
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public static void appendTooltip(Item item, ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, Tier tier) {
        String tierName = "";
        int color = 0xffffff;

        switch (tier) {
            case FF, F, E: tierName = "I"; color = 0x737373; break;
            case D: tierName = "II"; color = 0xaeada6; break;
            case C, A: tierName = "III"; color = 0xdbd1bb; break;
            case B: tierName = "IV"; color = 0xf0b27d; break;
            case S, SS: tierName = "V"; color = 0xd673e1; break;
        }

        tooltip.add(Text.literal(String.format("[Tier %s]", tierName)).withColor(color));
    }
}
