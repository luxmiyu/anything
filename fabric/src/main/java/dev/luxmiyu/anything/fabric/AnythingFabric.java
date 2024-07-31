package dev.luxmiyu.anything.fabric;

import net.fabricmc.api.ModInitializer;
import dev.luxmiyu.anything.Anything;

public final class AnythingFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Anything.init();
    }
}
