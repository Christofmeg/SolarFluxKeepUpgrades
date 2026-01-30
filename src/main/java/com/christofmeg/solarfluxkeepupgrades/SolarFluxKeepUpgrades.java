package com.christofmeg.solarfluxkeepupgrades;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("removal")
@Mod(SolarFluxKeepUpgrades.MOD_ID)
public class SolarFluxKeepUpgrades {

    public static final String MOD_ID = "solarfluxkeepupgrades";

    public SolarFluxKeepUpgrades() {
        MinecraftForge.EVENT_BUS.register(this);
        RecipeRegistry.init(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
