/*
 *       ____ _  _ ____ ___ ____ _  _ ____ ____ ____ ____ ___ _ _  _ ____
 *       |    |  | [__   |  |  | |\/| |    |__/ |__| |___  |  | |\ | | __
 *       |___ |__| ___]  |  |__| |  | |___ |  \ |  | |     |  | | \| |__]
 *
 *       CustomCrafting Recipe creation and management tool for Minecraft
 *                      Copyright (C) 2021  WolfyScript
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.wolfyscript.customcrafting.data.cache.recipe_creator;

import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.recipes.CustomRecipeSmithing;
import me.wolfyscript.customcrafting.recipes.items.Ingredient;

public class RecipeCacheSmithing extends RecipeCache<CustomRecipeSmithing> {

    private Ingredient base;
    private Ingredient addition;
    private Ingredient template;

    private boolean preserveEnchants;
    private boolean preserveDamage;
    private boolean onlyChangeMaterial;

    RecipeCacheSmithing(CustomCrafting customCrafting) {
        super(customCrafting);
        this.preserveEnchants = true;
        this.preserveDamage = true;
        this.onlyChangeMaterial = false;
    }

    RecipeCacheSmithing(CustomCrafting customCrafting, CustomRecipeSmithing recipe) {
        super(customCrafting, recipe);
        this.base = recipe.getBase().clone();
        this.template = recipe.getTemplate() != null ? recipe.getTemplate().clone() : null;
        this.addition = recipe.getAddition().clone();
        this.preserveEnchants = recipe.isPreserveEnchants();
        this.preserveDamage = recipe.isPreserveDamage();
    }

    @Override
    public void setIngredient(int slot, Ingredient ingredient) {
        switch (slot) {
            case 0 -> setTemplate(ingredient);
            case 1 -> setBase(ingredient);
            case 2 -> setAddition(ingredient);
        }
    }

    @Override
    public Ingredient getIngredient(int slot) {
        return switch (slot) {
            case 0 -> getTemplate();
            case 1 -> getBase();
            case 2 -> getAddition();
            default -> throw new IllegalStateException("Unexpected Ingredient Slot: " + slot);
        };
    }

    @Override
    protected CustomRecipeSmithing constructRecipe() {
        return create(new CustomRecipeSmithing(key, customCrafting));
    }

    @Override
    protected CustomRecipeSmithing create(CustomRecipeSmithing recipe) {
        CustomRecipeSmithing recipeSmithing = super.create(recipe);
        // make sure ingredients are properly set before applying
        setTemplate(template);
        setBase(base);
        setAddition(addition);
        recipeSmithing.setTemplate(template);
        recipeSmithing.setBase(base);
        recipeSmithing.setAddition(addition);

        recipeSmithing.setPreserveEnchants(preserveEnchants);
        recipeSmithing.setPreserveDamage(preserveDamage);
        recipeSmithing.setOnlyChangeMaterial(onlyChangeMaterial);
        return recipeSmithing;
    }

    public void setTemplate(Ingredient template) {
        this.template = template;
        if (template == null || template.isEmpty()) {
            this.template = new Ingredient();
            this.template.setAllowEmpty(true);
        }
    }

    public Ingredient getTemplate() {
        return template;
    }

    public Ingredient getBase() {
        return base;
    }

    public void setBase(Ingredient base) {
        this.base = base;
        if (base == null || base.isEmpty()) {
            this.base = new Ingredient();
            this.base.setAllowEmpty(true);
        }
    }

    public Ingredient getAddition() {
        return addition;
    }

    public void setAddition(Ingredient addition) {
        this.addition = addition;
        if (addition == null || addition.isEmpty()) {
            this.addition = new Ingredient();
            this.addition.setAllowEmpty(true);
        }
    }

    public boolean isPreserveEnchants() {
        return preserveEnchants;
    }

    public void setPreserveEnchants(boolean preserveEnchants) {
        this.preserveEnchants = preserveEnchants;
    }

    public boolean isPreserveDamage() {
        return preserveDamage;
    }

    public void setPreserveDamage(boolean preserveDamage) {
        this.preserveDamage = preserveDamage;
    }

    public void setOnlyChangeMaterial(boolean onlyChangeMaterial) {
        this.onlyChangeMaterial = onlyChangeMaterial;
    }

    public boolean isOnlyChangeMaterial() {
        return onlyChangeMaterial;
    }
}
