package com.christofmeg.solarfluxkeepupgrades;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;

public class RecipeRegistry {

    private static final DeferredRegister<RecipeSerializer<?>> RECIPES_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, SolarFluxKeepUpgrades.MOD_ID);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, SolarFluxKeepUpgrades.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<SolarPanelUpgradeRecipeShaped>> SOLAR_PANEL_UPGRADE_SHAPED_PROVIDER_TYPE = RECIPE_TYPES.register("crafting_shaped", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(SolarFluxKeepUpgrades.MOD_ID, "crafting_shaped")));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SolarPanelUpgradeRecipeShaped>> SOLAR_PANEL_UPGRADE_SHAPED_PROVIDER_SERIALIZER = RECIPES_SERIALIZERS.register("crafting_shaped", SolarPanelUpgradeRecipeSerializerShaped::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<SolarPanelUpgradeRecipeShaped>> SOLAR_PANEL_UPGRADE_SHAPELESS_PROVIDER_TYPE = RECIPE_TYPES.register("crafting_shapeless", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(SolarFluxKeepUpgrades.MOD_ID, "crafting_shapeless")));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SolarPanelUpgradeRecipeShaped>> SOLAR_PANEL_UPGRADE_SHAPELESS_PROVIDER_SERIALIZER = RECIPES_SERIALIZERS.register("crafting_shapeless", SolarPanelUpgradeRecipeSerializerShapeless::new);

//    public static final DeferredHolder<RecipeType<?>, RecipeType<SolarFluxFusionRecipe>> SOLAR_FLUX_FUSION_PROVIDER_TYPE = RECIPE_TYPES.register("fusion_crafting", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(SolarFluxKeepUpgrades.MOD_ID, "fusion_crafting")));
//    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SolarFluxFusionRecipe>> SOLAR_FLUX_FUSION_PROVIDER_SERIALIZER = RECIPES_SERIALIZERS.register("fusion_crafting", SolarFluxFusionRecipe.Serializer::new);

//    public static RegistryObject<RecipeSerializer<SolarFluxFusionRecipe>> SOLAR_FLUX_FUSION_SERIALIZER = null;


    public static void init(@Nonnull IEventBus modEventBus) {
        RECIPES_SERIALIZERS.register(modEventBus);
//        if (ModList.get().isLoaded("draconicevolution")) {
//           CompatDraconicEvolution.registerFusionSerializer();
//        }
    }

}
