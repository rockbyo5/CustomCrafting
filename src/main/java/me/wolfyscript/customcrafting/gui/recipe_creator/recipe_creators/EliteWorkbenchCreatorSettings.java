package me.wolfyscript.customcrafting.gui.recipe_creator.recipe_creators;

import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.data.CCCache;
import me.wolfyscript.customcrafting.gui.recipe_creator.buttons.ExactMetaButton;
import me.wolfyscript.customcrafting.gui.recipe_creator.buttons.PriorityButton;
import me.wolfyscript.customcrafting.recipes.types.elite_workbench.EliteCraftingRecipe;
import me.wolfyscript.utilities.api.inventory.gui.GuiCluster;
import me.wolfyscript.utilities.api.inventory.gui.GuiUpdate;
import me.wolfyscript.utilities.api.inventory.gui.button.buttons.ToggleButton;

public class EliteWorkbenchCreatorSettings extends RecipeCreator {

    public EliteWorkbenchCreatorSettings(GuiCluster<CCCache> cluster, CustomCrafting customCrafting) {
        super(cluster, "elite_workbench_settings", 27, customCrafting);
    }

    @Override
    public void onInit() {
        super.onInit();

        registerButton(new ExactMetaButton());
        registerButton(new PriorityButton());
    }

    @Override
    public void onUpdateSync(GuiUpdate<CCCache> guiUpdate) {

    }

    @Override
    public void onUpdateAsync(GuiUpdate<CCCache> update) {
        super.onUpdateAsync(update);
        update.setButton(0, "none", "back");
        CCCache cache = update.getGuiHandler().getCustomCache();
        EliteCraftingRecipe workbench = cache.getEliteCraftingRecipe();

        ((ToggleButton<CCCache>) getButton("exact_meta")).setState(update.getGuiHandler(), workbench.isExactMeta());
        ((ToggleButton<CCCache>) getButton("hidden")).setState(update.getGuiHandler(), workbench.isHidden());

        update.setButton(10, "hidden");
        update.setButton(12, "recipe_creator", "conditions");
        update.setButton(14, "exact_meta");
        update.setButton(16, "priority");
    }

    @Override
    public boolean validToSave(CCCache cache) {
        EliteCraftingRecipe workbench = cache.getEliteCraftingRecipe();
        return workbench.getIngredients() != null && !workbench.getResult().isEmpty();
    }
}
