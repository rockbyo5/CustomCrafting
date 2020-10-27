package me.wolfyscript.customcrafting.gui.recipebook_editor;

import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.data.CacheButtonAction;
import me.wolfyscript.customcrafting.data.TestCache;
import me.wolfyscript.customcrafting.gui.ExtendedGuiWindow;
import me.wolfyscript.utilities.api.inventory.GuiHandler;
import me.wolfyscript.utilities.api.inventory.GuiUpdate;
import me.wolfyscript.utilities.api.inventory.InventoryAPI;
import me.wolfyscript.utilities.api.inventory.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.button.buttons.ActionButton;
import me.wolfyscript.utilities.api.utils.inventory.PlayerHeadUtils;
import org.bukkit.Material;

public class EditorMain extends ExtendedGuiWindow {

    public EditorMain(InventoryAPI inventoryAPI, CustomCrafting customCrafting) {
        super("editor_main", inventoryAPI, 54, customCrafting);
    }

    @Override
    public void onInit() {
        registerButton(new ActionButton("back", new ButtonState("none", "back", PlayerHeadUtils.getViaValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY0Zjc3OWE4ZTNmZmEyMzExNDNmYTY5Yjk2YjE0ZWUzNWMxNmQ2NjllMTljNzVmZDFhN2RhNGJmMzA2YyJ9fX0="), (CacheButtonAction) (cache, guiHandler, player, inventory, i, inventoryClickEvent) -> {
            guiHandler.openCluster("none");
            return true;
        })));
        registerButton(new ActionButton("switch_categories", Material.COMPASS, (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            ((TestCache) guiHandler.getCustomCache()).getRecipeBookEditor().setSwitchCategories(true);
            guiHandler.changeToInv("categories");
            return true;
        }));
        registerButton(new ActionButton("main_categories", Material.CHEST, (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            ((TestCache) guiHandler.getCustomCache()).getRecipeBookEditor().setSwitchCategories(false);
            guiHandler.changeToInv("categories");
            return true;
        }));
    }

    @Override
    public void onUpdateAsync(GuiUpdate update) {
        super.onUpdateAsync(update);
        GuiHandler<TestCache> guiHandler = update.getGuiHandler(TestCache.class);
        TestCache cache = guiHandler.getCustomCache();
        update.setButton(0, "back");
        update.setButton(20, "main_categories");
        update.setButton(24, "switch_categories");


    }
}
