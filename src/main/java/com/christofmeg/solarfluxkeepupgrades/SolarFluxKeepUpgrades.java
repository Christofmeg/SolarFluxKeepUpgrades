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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Optional;


@Mod(SolarFluxKeepUpgrades.MOD_ID)
public class SolarFluxKeepUpgrades {

    public static final String MOD_ID = "solarfluxkeepupgrades";

    public SolarFluxKeepUpgrades(IEventBus modBus) {
        modBus.register(this);
        RecipeRegistry.init(modBus);
    }

    @EventBusSubscriber(modid = SolarFluxKeepUpgrades.MOD_ID)
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
