package com.christofmeg.solarfluxkeepupgrades;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;

public class SolarPanelUpgradeRecipeSerializerShapeless implements RecipeSerializer<SolarPanelUpgradeRecipeShapeless> {

    @Override
    public @NotNull SolarPanelUpgradeRecipeShapeless fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
        ShapelessRecipe vanilla = RecipeSerializer.SHAPELESS_RECIPE.fromJson(id, json);
        return new SolarPanelUpgradeRecipeShapeless(
                id,
                vanilla.getGroup(),
                vanilla.category(),
                vanilla.getResultItem(RegistryAccess.EMPTY),
                vanilla.getIngredients()
        );
    }

    @Override
    public SolarPanelUpgradeRecipeShapeless fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
        ShapelessRecipe vanilla = RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(id, buf);
        return new SolarPanelUpgradeRecipeShapeless(
                id,
                vanilla.getGroup(),
                vanilla.category(),
                vanilla.getResultItem(RegistryAccess.EMPTY),
                vanilla.getIngredients()
        );
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull SolarPanelUpgradeRecipeShapeless recipe) {
        RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buf, recipe);
    }
}