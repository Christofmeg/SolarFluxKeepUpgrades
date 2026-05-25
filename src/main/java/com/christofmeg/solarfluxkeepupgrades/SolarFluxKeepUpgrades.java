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

    /* Debugging
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = SolarFluxKeepUpgrades.MOD_ID)
    public static class Common {
        @SubscribeEvent
        public static void onCratedEvent(PlayerEvent.ItemCraftedEvent event) {
            Player player = event.getEntity();
            Level level = player.level();
            if (!level.isClientSide) {
                RecipeManager manager = level.getRecipeManager();
                Container container = event.getInventory();
                if (container instanceof CraftingContainer craftingContainer) {
                    Optional<? extends Recipe<CraftingContainer>> recipeOpt = manager.getRecipeFor(RecipeType.CRAFTING, craftingContainer, level);
                    if (recipeOpt.isPresent()) {
                        ResourceLocation recipeId = recipeOpt.get().getId();
                        player.sendSystemMessage(Component.literal("Crafted recipe ID: " + recipeId));
                    }
                }
            }
        }
    }*/

}
