package dev.luxmiyu.anything.event;

import com.mojang.brigadier.context.CommandContext;
import dev.luxmiyu.anything.Anything;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AnythingCommand {
    public static int execute(CommandContext<ServerCommandSource> objectCommandContext) {
        try {
            ServerCommandSource source = objectCommandContext.getSource();
            PlayerEntity player = source.getPlayer();

            if (player == null) {
                source.sendFeedback(() -> Text.translatable("message.anything.command_not_player"), false);
                return 0;
            }

            if (!player.hasPermissionLevel(2)) {
                source.sendFeedback(() -> Text.translatable("message.anything.command_no_permission"), false);
                return 0;
            }

            World world = player.getWorld();
            if (world == null) return 0;

            BlockPos pos = player.getBlockPos();

            int i = 0;
            int j = 0;
            final int width = (int) Math.sqrt(Anything.validBlocks().length);

            for (Block block : Anything.validBlocks()) {
                int x = i * 2;
                int z = j * 2;

                ArmorStandEntity stand = new ArmorStandEntity(EntityType.ARMOR_STAND, world);
                stand.refreshPositionAndAngles(pos.add(x, 0, z), 0, 0);
                stand.setShowArms(true);

                stand.equipStack(EquipmentSlot.MAINHAND, Anything.getStack(block, Anything.Type.SWORD));
                stand.equipStack(EquipmentSlot.OFFHAND, Anything.getStack(block, Anything.Type.PICKAXE));
                stand.equipStack(EquipmentSlot.HEAD, Anything.getStack(block, Anything.Type.HELMET));
                stand.equipStack(EquipmentSlot.CHEST, Anything.getStack(block, Anything.Type.CHESTPLATE));
                stand.equipStack(EquipmentSlot.LEGS, Anything.getStack(block, Anything.Type.LEGGINGS));
                stand.equipStack(EquipmentSlot.FEET, Anything.getStack(block, Anything.Type.BOOTS));

                world.setBlockState(pos.add(x, 1, z), Blocks.AIR.getDefaultState());
                world.setBlockState(pos.add(x, 0, z), Blocks.AIR.getDefaultState());
                world.setBlockState(pos.add(x, -1, z), Blocks.QUARTZ_BRICKS.getDefaultState());

                world.spawnEntity(stand);

                i++;

                if (i > width) {
                    i = 0;
                    j++;
                }
            }


            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
