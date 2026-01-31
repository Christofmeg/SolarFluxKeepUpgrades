package com.christofmeg.solarfluxkeepupgrades;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Optional;

@SuppressWarnings("removal")
@Mod(SolarFluxKeepUpgrades.MOD_ID)
public class SolarFluxKeepUpgrades {

    public static final String MOD_ID = "solarfluxkeepupgrades";

    public SolarFluxKeepUpgrades() {
        MinecraftForge.EVENT_BUS.register(this);
        RecipeRegistry.init(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //TODO integration to https://www.curseforge.com/minecraft/mc-mods/re-avaritia
    //TODO integration to https://www.curseforge.com/minecraft/mc-mods/the-twilight-forest

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
    }

}
