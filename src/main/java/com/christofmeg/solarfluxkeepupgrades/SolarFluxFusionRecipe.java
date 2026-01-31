package com.christofmeg.solarfluxkeepupgrades;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.draconicevolution.api.crafting.IFusionDataTransfer;
import com.brandon3055.draconicevolution.api.crafting.IFusionInjector;
import com.brandon3055.draconicevolution.api.crafting.IFusionInventory;
import com.brandon3055.draconicevolution.api.crafting.IFusionRecipe;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.NotNull;
import org.zeith.solarflux.block.SolarPanelBlockItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SolarFluxFusionRecipe implements IFusionRecipe {
    private final ResourceLocation id;
    private final ItemStack result;
    private final Ingredient catalyst;
    private final long totalEnergy;
    private final TechLevel techLevel;
    private final Collection<SolarFluxFusionRecipe.FusionIngredient> ingredients;

    public SolarFluxFusionRecipe(ResourceLocation id, ItemStack result, Ingredient catalyst, long totalEnergy, TechLevel techLevel, Collection<SolarFluxFusionRecipe.FusionIngredient> ingredients) {
        this.id = id;
        this.result = result;
        this.catalyst = catalyst;
        this.totalEnergy = totalEnergy;
        this.techLevel = techLevel;
        this.ingredients = ingredients;
    }

    public TechLevel getRecipeTier() {
        return this.techLevel;
    }

    public long getEnergyCost() {
        return this.totalEnergy;
    }

    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.ingredients.stream().map((fusionIngredient) -> fusionIngredient.ingredient).collect(Collectors.toCollection(NonNullList::create));
    }

    public List<IFusionRecipe.IFusionIngredient> fusionIngredients() {
        return ImmutableList.copyOf(this.ingredients);
    }

    public Ingredient getCatalyst() {
        return this.catalyst;
    }

    public @NotNull ItemStack assemble(@NotNull IFusionInventory inv, @NotNull RegistryAccess registryAccess) {
        ItemStack stack = this.result.copy();
        if (stack.getItem() instanceof IFusionDataTransfer) {
            ((IFusionDataTransfer)stack.getItem()).transferIngredientData(stack, inv);
        }

        return stack;
    }

    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return this.result;
    }

    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.SOLAR_FLUX_FUSION_SERIALIZER.get();
    }

    @Override
    public boolean matches(IFusionInventory inv, @NotNull Level level) {
        if (!this.getCatalyst().test(inv.getCatalystStack())) {
            return false;
        } else {
            List<IFusionInjector> injectors = new ArrayList<>(inv.getInjectors());
            for(Ingredient ingredient : this.getIngredients()) {
                IFusionInjector match = injectors.stream().filter((e) -> ingredient.test(e.getInjectorStack())).findFirst().orElse(null);
                if (match == null) {
                    return false;
                } else if (match.getInjectorStack().getItem() instanceof SolarPanelBlockItem) {
                    CompoundTag tag = match.getInjectorStack().getTag();
                    if (tag != null && tag.contains("Upgrades")) {
                        return false;
                    }
                }
                injectors.remove(match);
            }
            return injectors.stream().allMatch((e) -> e.getInjectorStack().isEmpty());
        }
    }

    public static class FusionIngredient implements IFusionRecipe.IFusionIngredient {
        private final Ingredient ingredient;
        private final boolean consume;

        public FusionIngredient(Ingredient ingredient, boolean consume) {
            this.ingredient = ingredient;
            this.consume = consume;
        }

        public Ingredient get() {
            return this.ingredient;
        }

        public boolean consume() {
            return this.consume;
        }

        protected void write(FriendlyByteBuf buffer) {
            buffer.writeBoolean(this.consume);
            this.ingredient.toNetwork(buffer);
        }

        protected static SolarFluxFusionRecipe.FusionIngredient read(FriendlyByteBuf buffer) {
            boolean consume = buffer.readBoolean();
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            return new SolarFluxFusionRecipe.FusionIngredient(ingredient, consume);
        }
    }

    public static class Serializer implements RecipeSerializer<SolarFluxFusionRecipe> {
        public Serializer() {
        }

        public @NotNull SolarFluxFusionRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
            Ingredient catalyst = CraftingHelper.getIngredient(GsonHelper.getAsJsonObject(json, "catalyst"), false);
            List<SolarFluxFusionRecipe.FusionIngredient> fusionIngredients = new ArrayList<>();

            for(JsonElement element : GsonHelper.getAsJsonArray(json, "ingredients")) {
                Ingredient ingredient;
                if (element.isJsonObject() && element.getAsJsonObject().has("ingredient")) {
                    ingredient = CraftingHelper.getIngredient(element.getAsJsonObject().get("ingredient"), false);
                } else {
                    ingredient = CraftingHelper.getIngredient(element, false);
                }

                boolean isConsumed = !element.isJsonObject() || GsonHelper.getAsBoolean(element.getAsJsonObject(), "consume", true);
                fusionIngredients.add(new SolarFluxFusionRecipe.FusionIngredient(ingredient, isConsumed));
            }

            long totalEnergy = GsonHelper.getAsLong(json, "total_energy");
            TechLevel techLevel = TechLevel.valueOf(GsonHelper.getAsString(json, "tier", TechLevel.DRACONIUM.name()));
            return new SolarFluxFusionRecipe(id, result, catalyst, totalEnergy, techLevel, fusionIngredients);
        }

        public SolarFluxFusionRecipe fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buffer) {
            ItemStack result = buffer.readItem();
            Ingredient catalyst = Ingredient.fromNetwork(buffer);
            int count = buffer.readByte();
            List<SolarFluxFusionRecipe.FusionIngredient> fusionIngredients = new ArrayList<>();

            for(int i = 0; i < count; ++i) {
                fusionIngredients.add(SolarFluxFusionRecipe.FusionIngredient.read(buffer));
            }

            long totalEnergy = buffer.readLong();
            TechLevel techLevel = TechLevel.VALUES[Mth.clamp(buffer.readByte(), 0, TechLevel.values().length - 1)];
            return new SolarFluxFusionRecipe(id, result, catalyst, totalEnergy, techLevel, fusionIngredients);
        }

        public void toNetwork(FriendlyByteBuf buffer, SolarFluxFusionRecipe recipe) {
            buffer.writeItemStack(recipe.result, false);
            recipe.catalyst.toNetwork(buffer);
            buffer.writeByte(recipe.ingredients.size());

            for(SolarFluxFusionRecipe.FusionIngredient ingredient : recipe.ingredients) {
                ingredient.write(buffer);
            }

            buffer.writeLong(recipe.totalEnergy);
            buffer.writeByte(recipe.techLevel.index);
        }
    }
}