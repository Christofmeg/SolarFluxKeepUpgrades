package com.christofmeg.solarfluxkeepupgrades;

public class CompatDraconicEvolution {

    public static void registerFusionSerializer() {
        RecipeRegistry.SOLAR_FLUX_FUSION_SERIALIZER =
                RecipeRegistry.RECIPES_SERIALIZERS.register(
                        "fusion_crafting",
                        SolarFluxFusionRecipe.Serializer::new
                );
    }
}