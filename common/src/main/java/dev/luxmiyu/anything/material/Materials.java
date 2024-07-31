package dev.luxmiyu.anything.material;

import com.google.common.base.Suppliers;
import dev.luxmiyu.anything.Anything;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class Materials {
    private static final BlockTest SANDSTONE_LIKE = new BlockTest().contains("sandstone");
    private static final BlockTest SAND_LIKE = new BlockTest()
        .blocks(Blocks.SNOW_BLOCK)
        .contains("sand", "gravel", "powder", "leaves")
        .not(SANDSTONE_LIKE);

    private static final BlockTest DIRT_LIKE = new BlockTest()
        .blocks(Blocks.MYCELIUM, Blocks.TARGET, Blocks.HAY_BLOCK, Blocks.DRIED_KELP_BLOCK, Blocks.MOSS_BLOCK, Blocks.PODZOL)
        .contains("dirt", "wool", "clay", "mud", "grass", "sponge", "coral");

    private static final BlockTest GLASS_LIKE = new BlockTest().blocks(Blocks.TINTED_GLASS).contains("glass", "ice");

    private static final BlockTest WOOD_LIKE = new BlockTest()
        .blocks(Blocks.CRAFTING_TABLE, Blocks.LOOM, Blocks.BARREL, Blocks.BOOKSHELF, Blocks.CHISELED_BOOKSHELF, Blocks.NOTE_BLOCK, Blocks.JUKEBOX,
            Blocks.NETHER_WART_BLOCK, Blocks.CARTOGRAPHY_TABLE, Blocks.FLETCHING_TABLE, Blocks.BEE_NEST, Blocks.BEEHIVE)
        .contains("wood", "log", "planks", "hyphae", "stem", "mushroom", "roots", "bamboo", "pumpkin");

    private static final BlockTest STONE_LIKE = new BlockTest()
        .blocks(Blocks.NETHERRACK, Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM, Blocks.DISPENSER, Blocks.OBSERVER, Blocks.DROPPER, Blocks.SMOKER, Blocks.FURNACE,
            Blocks.BLAST_FURNACE, Blocks.SHROOMLIGHT, Blocks.HONEYCOMB_BLOCK, Blocks.HONEY_BLOCK, Blocks.SLIME_BLOCK)
        .contains("ore", "stone", "cobblestone", "bricks", "terracotta", "andesite", "granite", "diorite", "deepslate", "netherrack", "piston", "prismarine",
            "lantern", "tuff", "sculk", "froglight");

    private static final BlockTest IRON_LIKE = new BlockTest().blocks(Blocks.QUARTZ_BRICKS, Blocks.END_STONE, Blocks.END_STONE_BRICKS).contains("reinforced");

    private static final BlockTest EPIC = new BlockTest().blocks(Blocks.ANCIENT_DEBRIS).contains("shulker", "obsidian");

    private static final BlockTest LEGENDARY = new BlockTest()
        .blocks(Blocks.DIAMOND_BLOCK, Blocks.NETHERITE_BLOCK, Blocks.BEDROCK);

    private static final BlockTest EXPLOSIVE = new BlockTest().contains("tnt");

    private static final BlockTest ADMIN = new BlockTest()
        .blocks(Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.REPEATING_COMMAND_BLOCK, Blocks.BARRIER, Blocks.STRUCTURE_BLOCK, Blocks.JIGSAW);



    public record Tiers(Tier tier, Tier durability, Tier speed) {}

    @Nullable
    public static Tiers getTiers(Block block) {
        // Tier SS
        if (ADMIN.test(block)) return new Tiers(Tier.SS, Tier.SS, Tier.SS);

        // Tier S-A
        if (LEGENDARY.test(block)) return new Tiers(Tier.S, Tier.S, Tier.S);
        if (EPIC.test(block)) return new Tiers(Tier.A, Tier.A, Tier.A);
        if (EXPLOSIVE.test(block)) return new Tiers(Tier.A, Tier.F, Tier.A);

        // Tier B
        if (GLASS_LIKE.test(block)) return new Tiers(Tier.B, Tier.F, Tier.A);

        // Tier E-F
        if (SAND_LIKE.test(block)) return new Tiers(Tier.F, Tier.F, Tier.F);
        if (DIRT_LIKE.test(block)) return new Tiers(Tier.E, Tier.E, Tier.E);
        if (WOOD_LIKE.test(block)) return new Tiers(Tier.E, Tier.C, Tier.E);

        // Tier C-D
        if (IRON_LIKE.test(block)) return new Tiers(Tier.C, Tier.A, Tier.C);
        if (STONE_LIKE.test(block)) return new Tiers(Tier.D, Tier.B, Tier.D);

        return null;
    }

    public static Material from(Block block) {
        Tiers tiers = getTiers(block);

        if (tiers == null) return Material.getDefault(block);

        return Material.of(tiers.tier, tiers.durability, tiers.speed, block);
    }

    public static ArmorMaterial armor(Block block, String name) {
        Tiers tiers = getTiers(block);
        Tier tier = tiers == null ? Tier.B : tiers.tier;

        EnumMap<ArmorItem.Type, Integer> enumMap = new EnumMap<>(ArmorItem.Type.class);

        int[] enumArray;

        int enchantibility = 14;
        float toughness = 0f;
        float knockbackResistance = 0f;

        RegistryEntry<SoundEvent> sound = SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;

        switch (tier) {
            case Tier.FF, Tier.F, Tier.E: enumArray = new int[]{1, 2, 3, 1, 3}; enchantibility = 10; break;
            case Tier.D, Tier.C: enumArray = new int[]{1, 4, 5, 2, 4}; enchantibility = 12; break;
            case Tier.B, Tier.A: enumArray = new int[]{2, 5, 6, 2, 5}; enchantibility = 16; toughness = 2f; break;
            case Tier.S: enumArray = new int[]{3, 6, 8, 3, 11}; enchantibility = 20; toughness = 3f; break;
            case Tier.SS: enumArray = new int[]{10, 16, 20, 10, 26}; enchantibility = 25; toughness = 5f; break;
            default: enumArray = new int[]{2, 5, 6, 2, 5};
        }

        enumMap.put(ArmorItem.Type.BOOTS, enumArray[0]);
        enumMap.put(ArmorItem.Type.LEGGINGS, enumArray[1]);
        enumMap.put(ArmorItem.Type.CHESTPLATE, enumArray[2]);
        enumMap.put(ArmorItem.Type.HELMET, enumArray[3]);
        enumMap.put(ArmorItem.Type.BODY, enumArray[4]);

        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(Identifier.of(Anything.MOD_ID, name)));

        Supplier<Ingredient> repairIngredient = Suppliers.memoize(() -> Ingredient.ofItems(block));

        return new ArmorMaterial(enumMap, enchantibility, sound, repairIngredient, list, toughness, knockbackResistance);
    }
}
