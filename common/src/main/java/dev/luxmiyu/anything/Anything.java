package dev.luxmiyu.anything;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.luxmiyu.anything.event.AnyEvents;
import dev.luxmiyu.anything.item.*;
import dev.luxmiyu.anything.material.All;
import dev.luxmiyu.anything.material.Material;
import dev.luxmiyu.anything.material.Materials;
import dev.luxmiyu.anything.material.Tier;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class Anything {
    public static final String MOD_ID = "anything";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
//    public static final Random RANDOM = Random.create();

    public static final DeferredRegister<ItemGroup> TABS = DeferredRegister.create(MOD_ID, RegistryKeys.ITEM_GROUP);
    public static final RegistrySupplier<ItemGroup> ANYTHING_TAB = TABS.getRegistrar().register(
        Identifier.of(MOD_ID, "anything_tab"),
        () -> CreativeTabRegistry.create(
            Text.translatable("anything.display_name"),
            () -> new ItemStack(Registries.ITEM.get(Identifier.of(MOD_ID, "cherry_leaves_axe")))
        )
    );

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, RegistryKeys.ITEM);
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(MOD_ID, RegistryKeys.ARMOR_MATERIAL);

    private static Item.Settings armorSettings() {
        return new Item.Settings().arch$tab(ANYTHING_TAB).maxCount(1);
    }
    private static Item.Settings toolSettings(ToolMaterial material, float baseAttackDamage, float attackSpeed) {
        return new Item.Settings().arch$tab(ANYTHING_TAB).maxCount(1).attributeModifiers(PickaxeItem.createAttributeModifiers(material, baseAttackDamage, attackSpeed));
    }

    public static Block[] validBlocks() {
        List<Block> blockList = new ArrayList<>();

        for (String name : All.BLOCKS) {
            blockList.add(Registries.BLOCK.get(Identifier.of("minecraft", name)));
        }

        return blockList.toArray(new Block[0]);
    }

//    public static Block randomValidBlock() {
//        Block[] validBlocks = validBlocks();
//        int index = RANDOM.nextInt(validBlocks.length);
//
//        return validBlocks[index];
//    }

    public static boolean isValidBlock(Block block) {
        for (Block validBlock : validBlocks()) {
            if (block.equals(validBlock)) {
                return true;
            }
        }

        return false;
    }

    public static Item getItem(String name) {
        return Registries.ITEM.get(Identifier.of(MOD_ID, name));
    }

    public enum Type { SWORD, PICKAXE, AXE, SHOVEL, HOE, HELMET, CHESTPLATE, LEGGINGS, BOOTS }
    public static Item getItem(Block block, Type type) {
        if (!isValidBlock(block)) return null;

        String typeName = switch (type) {
            case SWORD -> "sword";
            case PICKAXE -> "pickaxe";
            case AXE -> "axe";
            case SHOVEL -> "shovel";
            case HOE -> "hoe";
            case HELMET -> "helmet";
            case CHESTPLATE -> "chestplate";
            case LEGGINGS -> "leggings";
            case BOOTS -> "boots";
        };

        Identifier blockId = Registries.BLOCK.getId(block);
        String blockName = blockId.getPath();

        return getItem(blockName + "_" + typeName);
    }

    public static ItemStack getStack(Block block, Type type) {
        Item item = getItem(block, type);
        return item == null ? null : new ItemStack(item);
    }

    public static void init() {
        LOGGER.info("Initializing...");

        for (String name : All.BLOCKS) {
            Block block = Registries.BLOCK.get(Identifier.of("minecraft", name));

            Material material = Materials.from(block);
            Materials.Tiers tiers = Materials.getTiers(block);

            Tier tier = tiers == null ? Tier.C : tiers.tier();
            Tier tierDurability = tiers == null ? Tier.C : tiers.durability();

            ITEMS.register(name + "_sword", () -> new AnySwordItem(tier, material, toolSettings(material, 3f, -2.4f)));
            ITEMS.register(name + "_pickaxe", () -> new AnyPickaxeItem(tier, material, toolSettings(material, 1f, -2.8f)));
            ITEMS.register(name + "_axe", () -> new AnyAxeItem(tier, material, toolSettings(material, 5f, -3f)));
            ITEMS.register(name + "_shovel", () -> new AnyShovelItem(tier, material, toolSettings(material, 3f, -2.4f)));
            ITEMS.register(name + "_hoe", () -> new AnyHoeItem(tier, material, toolSettings(material, -3f, 0f)));

            ArmorMaterial armorMaterial = Materials.armor(block, name);
            RegistrySupplier<ArmorMaterial> armorSupplier = ARMOR_MATERIALS.register(name, () -> armorMaterial);
            int armorDurabilityMultiplier = Material.getArmorDurabilityMultiplier(tierDurability);

            ITEMS.register(name + "_helmet", () -> new AnyArmorItem(tier, armorSupplier, ArmorItem.Type.HELMET, armorSettings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(armorDurabilityMultiplier))));
            ITEMS.register(name + "_chestplate", () -> new AnyArmorItem(tier, armorSupplier, ArmorItem.Type.CHESTPLATE, armorSettings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(armorDurabilityMultiplier))));
            ITEMS.register(name + "_leggings", () -> new AnyArmorItem(tier, armorSupplier, ArmorItem.Type.LEGGINGS, armorSettings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(armorDurabilityMultiplier))));
            ITEMS.register(name + "_boots", () -> new AnyArmorItem(tier, armorSupplier, ArmorItem.Type.BOOTS, armorSettings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(armorDurabilityMultiplier))));
        }

        LOGGER.info("Registered {} items for {} blocks.", All.BLOCKS.length * 9, All.BLOCKS.length);

        ARMOR_MATERIALS.register();
        ITEMS.register();

        AnyEvents.init();
    }
}
