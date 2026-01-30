package com.christofmeg.solarfluxkeepupgrades;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.zeith.solarflux.block.SolarPanelBlockItem;

public class SolarPanelUpgradeRecipeShaped extends ShapedRecipe {

    public SolarPanelUpgradeRecipeShaped(ResourceLocation id, String group, CraftingBookCategory category, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
        super(id, group, category, width, height, ingredients, result);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.SOLAR_PANEL_UPGRADE_SERIALIZER_SHAPED.get();
    }

    @Override
    public boolean matches(CraftingContainer inv, @NotNull Level level) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.getItem() instanceof SolarPanelBlockItem) {
                CompoundTag tag = stack.getTag();
                if (tag != null && tag.contains("Upgrades")) {
                    return false;
                }
            }
        }
        return super.matches(inv, level);
    }

}