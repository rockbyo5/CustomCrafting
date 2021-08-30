package me.wolfyscript.customcrafting.data.cache.recipe_creator;

import me.wolfyscript.customcrafting.recipes.CustomRecipeBlasting;

public class RecipeCacheBlasting extends RecipeCacheCooking<CustomRecipeBlasting> {

    RecipeCacheBlasting() {
        super();
    }

    RecipeCacheBlasting(CustomRecipeBlasting recipe) {
        super(recipe);
    }

    @Override
    protected CustomRecipeBlasting constructRecipe() {
        return create(new CustomRecipeBlasting(key));
    }
}