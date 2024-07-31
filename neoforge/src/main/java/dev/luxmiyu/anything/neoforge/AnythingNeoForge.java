package dev.luxmiyu.anything.neoforge;

import net.neoforged.fml.common.Mod;

import dev.luxmiyu.anything.Anything;

@Mod(Anything.MOD_ID)
public final class AnythingNeoForge {
    public AnythingNeoForge() {
        // Run our common setup.
        Anything.init();
    }
}
