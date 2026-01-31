package com.christofmeg.solarfluxkeepupgrades;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.inv.SimpleInventory;
import org.zeith.solarflux.block.SolarPanelBlockItem;

import java.util.*;

public class SolarPanelUpgradeRecipeShapeless extends ShapelessRecipe {

    public SolarPanelUpgradeRecipeShapeless(ResourceLocation id, String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(id, group, category, result, ingredients);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.SOLAR_PANEL_UPGRADE_SERIALIZER_SHAPELESS.get();
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> remains = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        UpgradeSplit split = UpgradeSplit.splitUpgrades(inv);
        Iterator<ItemStack> it = split.toGrid.iterator();
        for (int i = 0; i < inv.getContainerSize() && it.hasNext(); i++) {
            if (inv.getItem(i).isEmpty()) {
                remains.set(i, it.next());
            }
        }
        return remains;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer inv, @NotNull RegistryAccess access) {
        ItemStack result = super.assemble(inv, access);
        UpgradeSplit split = UpgradeSplit.splitUpgrades(inv);
        if (!hasAnyItems(split.toOutput)) {
            result.removeTagKey("Upgrades");
            return result;
        }
        split.toOutput.writeToNBT(result.getOrCreateTag(), "Upgrades");
        return result;
    }

    private static boolean hasAnyItems(SimpleInventory inv) {
        for (ItemStack stack : inv.items) {
            if (!stack.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static class UpgradeSplit {

        final List<ItemStack> toGrid = new ArrayList<>();
        final SimpleInventory toOutput = new SimpleInventory(5);

        private static UpgradeSplit splitUpgrades(CraftingContainer inv) {
            Map<Item, Integer> counts = collectUpgrades(inv);
            UpgradeSplit split = new UpgradeSplit();

            int extractLimit = getMaxExtractableUpgrades(inv);
            int extracted = 0;
            int outputSlot = 0;

            for (Map.Entry<Item, Integer> entry : counts.entrySet()) {
                Item item = entry.getKey();
                int total = entry.getValue();
                int maxStack = item.getDefaultInstance().getMaxStackSize();

                while (total > 0 && extracted < extractLimit) {
                    int size = Math.min(total, maxStack);
                    split.toGrid.add(new ItemStack(item, size));
                    total -= size;
                    extracted++;
                }

                while (total > 0 && outputSlot < split.toOutput.getSlots()) {
                    int size = Math.min(total, maxStack);
                    split.toOutput.setStackInSlot(
                            outputSlot++,
                            new ItemStack(item, size)
                    );
                    total -= size;
                }
            }

            return split;
        }
    }

    private static int getMaxExtractableUpgrades(CraftingContainer inv) {
        int limit = 0;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            if (inv.getItem(i).isEmpty()) {
                limit++;
            }
        }
        return limit;
    }

    private static Map<Item, Integer> collectUpgrades(CraftingContainer inv) {
        Map<Item, Integer> counts = new HashMap<>();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.getItem() instanceof SolarPanelBlockItem && stack.hasTag()) {
                System.out.println("true");
                CompoundTag tag = stack.getTag();
                if (tag != null && tag.contains("Upgrades")) {
                    SimpleInventory upgradeInv = new SimpleInventory(5);
                    upgradeInv.readFromNBT(tag, "Upgrades");
                    for (ItemStack upgrade : upgradeInv.items) {
                        if (!upgrade.isEmpty()) {
                            counts.merge(
                                    upgrade.getItem(),
                                    upgrade.getCount(),
                                    Integer::sum
                            );
                        }
                    }
                }
            }
        }
        return counts;
    }

}