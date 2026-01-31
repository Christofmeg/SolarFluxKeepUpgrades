package com.christofmeg.solarfluxkeepupgrades;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

public class RecipeRegistry {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPES_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SolarFluxKeepUpgrades.MOD_ID);

    public static final RegistryObject<RecipeSerializer<SolarPanelUpgradeRecipeShaped>> SOLAR_PANEL_UPGRADE_SERIALIZER_SHAPED = RECIPES_SERIALIZERS.register("crafting_shaped", SolarPanelUpgradeRecipeSerializerShaped::new);
    public static final RegistryObject<RecipeSerializer<SolarPanelUpgradeRecipeShapeless>> SOLAR_PANEL_UPGRADE_SERIALIZER_SHAPELESS = RECIPES_SERIALIZERS.register("crafting_shapeless", SolarPanelUpgradeRecipeSerializerShapeless::new);
    public static RegistryObject<RecipeSerializer<SolarFluxFusionRecipe>> SOLAR_FLUX_FUSION_SERIALIZER = null;


    public static void init(@Nonnull IEventBus modEventBus) {
        RECIPES_SERIALIZERS.register(modEventBus);
        if (ModList.get().isLoaded("draconicevolution")) {
            CompatDraconicEvolution.registerFusionSerializer();
        }
    }

}
