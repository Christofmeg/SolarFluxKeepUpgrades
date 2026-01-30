package com.christofmeg.solarfluxkeepupgrades;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class SolarPanelUpgradeRecipeSerializerShaped implements RecipeSerializer<SolarPanelUpgradeRecipeShaped> {

    @Override
    public @NotNull SolarPanelUpgradeRecipeShaped fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
        ShapedRecipe vanilla = RecipeSerializer.SHAPED_RECIPE.fromJson(id, json);
        return new SolarPanelUpgradeRecipeShaped(
                id,
                vanilla.getGroup(),
                vanilla.category(),
                vanilla.getWidth(),
                vanilla.getHeight(),
                vanilla.getIngredients(),
                vanilla.getResultItem(RegistryAccess.EMPTY)
        );
    }

    @Override
    public SolarPanelUpgradeRecipeShaped fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
        ShapedRecipe vanilla = RecipeSerializer.SHAPED_RECIPE.fromNetwork(id, buf);
        return new SolarPanelUpgradeRecipeShaped(
                id,
                vanilla.getGroup(),
                vanilla.category(),
                vanilla.getWidth(),
                vanilla.getHeight(),
                vanilla.getIngredients(),
                vanilla.getResultItem(RegistryAccess.EMPTY)
        );
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull SolarPanelUpgradeRecipeShaped recipe) {
        RecipeSerializer.SHAPED_RECIPE.toNetwork(buf, recipe);
    }
}