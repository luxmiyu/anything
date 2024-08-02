package dev.luxmiyu.anything.event;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.luxmiyu.anything.Anything;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class AnyEvents {
    private static boolean isTntItem(Item item) {
        Item[] tntItems = new Item[]{
            Registries.ITEM.get(Identifier.of(Anything.MOD_ID, "tnt_sword")),
            Registries.ITEM.get(Identifier.of(Anything.MOD_ID, "tnt_pickaxe")),
            Registries.ITEM.get(Identifier.of(Anything.MOD_ID, "tnt_axe")),
            Registries.ITEM.get(Identifier.of(Anything.MOD_ID, "tnt_shovel")),
            Registries.ITEM.get(Identifier.of(Anything.MOD_ID, "tnt_hoe")),
            Registries.ITEM.get(Identifier.of(Anything.MOD_ID, "tnt_helmet")),
            Registries.ITEM.get(Identifier.of(Anything.MOD_ID, "tnt_chestplate")),
            Registries.ITEM.get(Identifier.of(Anything.MOD_ID, "tnt_leggings")),
            Registries.ITEM.get(Identifier.of(Anything.MOD_ID, "tnt_boots")),
        };

        for (Item tntItem : tntItems) {
            if (tntItem.equals(item)) return true;
        }

        return false;
    }

    public static void init() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("anything").executes(AnythingCommand::execute));
        });

        BlockEvent.BREAK.register((world, pos, state, player, xp) -> {
            if (world.isClient) return EventResult.pass();
            if (player == null) return EventResult.pass();

            ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
            Item item = stack.getItem();

            if (isTntItem(item)) {
                if (!player.isCreative()) {
                    stack.setCount(0);
                }
                world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 12f, World.ExplosionSourceType.TNT);
            }

            return EventResult.pass();
        });

        EntityEvent.LIVING_HURT.register(((entity, source, amount) -> {
            if (entity.getWorld().isClient) return EventResult.pass();

            if (source.getAttacker() instanceof PlayerEntity attacker) {
                World world = attacker.getWorld();

                ItemStack stack = attacker.getStackInHand(Hand.MAIN_HAND);
                Item item = stack.getItem();

                if (isTntItem(item)) {
                    if (!attacker.isCreative()) {
                        stack.setCount(0);
                    }
                    world.createExplosion(null, entity.getX(), entity.getY(), entity.getZ(), 12f, World.ExplosionSourceType.TNT);
                }
            }

            if (entity instanceof PlayerEntity receiver) {
                World world = receiver.getWorld();

                Iterable<ItemStack> armor = receiver.getArmorItems();

                boolean hasTntArmor = false;

                for (ItemStack stack : armor) {
                    Item item = stack.getItem();

                    if (isTntItem(item)) {
                        hasTntArmor = true;
                        stack.setCount(0);
                    }
                }

                if (hasTntArmor) {
                    world.createExplosion(null, receiver.getX(), receiver.getY(), receiver.getZ(), 12f, World.ExplosionSourceType.TNT);
                }
            }

            return EventResult.pass();
        }));
    }
}
