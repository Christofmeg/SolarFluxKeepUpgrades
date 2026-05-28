package com.christofmeg.solarfluxkeepupgrades;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.zeith.solarflux.block.SolarPanelBlockItem;
import org.zeith.solarflux.items.data.PanelDataComponent;

@EventBusSubscriber(modid = SolarFluxKeepUpgrades.MOD_ID)
@Mod(SolarFluxKeepUpgrades.MOD_ID)
public class SolarFluxKeepUpgrades {

    public static final String MOD_ID = "solarfluxkeepupgrades";

    @SubscribeEvent
    public static void onCraftedEvent(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        if (!(event.getInventory() instanceof CraftingContainer craftingContainer)) return;
        if (event.getCrafting().isEmpty()) return;
        for (ItemStack stack : craftingContainer.getItems()) {
            if (stack.isEmpty()) continue;
            if (!(stack.getItem() instanceof SolarPanelBlockItem)) continue;
            PanelDataComponent com = stack.get(PanelDataComponent.TYPE.get());
            if (com == null || com.isEmpty()) continue;
            for (ItemStack item : com.upgrades()) {
                player.getInventory().placeItemBackInInventory(item.copy());
            }
        }
    }

}
