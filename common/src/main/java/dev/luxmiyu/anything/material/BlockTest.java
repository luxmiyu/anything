package dev.luxmiyu.anything.material;

import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockTest {
    private final List<Block> blocks;
    private final List<BlockTest> nots;
    private final List<String> ends;
    private final List<String> contains;
    private final List<Sandwich> sandwiches;

    public BlockTest() {
        this.blocks = new ArrayList<>();
        this.nots = new ArrayList<>();
        this.ends = new ArrayList<>();
        this.contains = new ArrayList<>();
        this.sandwiches = new ArrayList<>();
    }

    public BlockTest blocks(Block...blocks) {
        Collections.addAll(this.blocks, blocks);
        return this;
    }

    public BlockTest not(BlockTest...nots) {
        Collections.addAll(this.nots, nots);
        return this;
    }

    public BlockTest ends(String...ends) {
        Collections.addAll(this.ends, ends);
        return this;
    }

    public BlockTest contains(String...contains) {
        Collections.addAll(this.contains, contains);
        return this;
    }

    public BlockTest sandwiches(Sandwich...sandwiches) {
        Collections.addAll(this.sandwiches, sandwiches);
        return this;
    }

    public boolean test(Block block) {
        for (BlockTest bt : this.nots) {
            if (bt.test(block)) return false;
        }

        for (Block b : this.blocks) {
            if (b == block) return true;
        }

        String key = block.getTranslationKey(); // for example: block.minecraft.dirt

        for (String end : this.ends) {
            if (key.endsWith(end)) return true;
        }

        for (String contain : this.contains) {
            if (key.contains(contain)) return true;
        }

        for (Sandwich sandwich : this.sandwiches) {
            if (sandwich.test(key)) return true;
        }

        return false;
    }
}
