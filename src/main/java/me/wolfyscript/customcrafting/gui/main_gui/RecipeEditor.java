package me.wolfyscript.customcrafting.gui.main_gui;

import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.data.TestCache;
import me.wolfyscript.customcrafting.gui.ExtendedGuiWindow;
import me.wolfyscript.customcrafting.recipes.types.ICustomRecipe;
import me.wolfyscript.utilities.api.inventory.GuiHandler;
import me.wolfyscript.utilities.api.inventory.GuiUpdateEvent;
import me.wolfyscript.utilities.api.inventory.InventoryAPI;
import me.wolfyscript.utilities.api.inventory.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.button.buttons.ActionButton;
import me.wolfyscript.utilities.api.utils.NamespacedKey;
import me.wolfyscript.utilities.api.utils.chat.ClickData;
import me.wolfyscript.utilities.api.utils.inventory.PlayerHeadUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

public class RecipeEditor extends ExtendedGuiWindow {

    public RecipeEditor(InventoryAPI inventoryAPI, CustomCrafting customCrafting) {
        super("recipe_editor", inventoryAPI, 45, customCrafting);
    }

    @Override
    public void onInit() {
        registerButton(new ActionButton("back", new ButtonState("none", "back", PlayerHeadUtils.getViaValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY0Zjc3OWE4ZTNmZmEyMzExNDNmYTY5Yjk2YjE0ZWUzNWMxNmQ2NjllMTljNzVmZDFhN2RhNGJmMzA2YyJ9fX0="), (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            guiHandler.openPreviousInv();
            return true;
        })));
        registerButton(new ActionButton("create_recipe", new ButtonState("create_recipe", Material.ITEM_FRAME, (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            changeToCreator(guiHandler);
            return true;
        })));
        registerButton(new ActionButton("edit_recipe", new ButtonState("edit_recipe", Material.REDSTONE, (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            ((TestCache) guiHandler.getCustomCache()).getChatLists().setCurrentPageRecipes(1);
            customCrafting.getChatUtils().sendRecipeListExpanded(player);
            guiHandler.setChatInputAction((guiHandler1, player1, s, args) -> {
                if (args.length > 1) {
                    ICustomRecipe recipe = customCrafting.getRecipeHandler().getRecipe(new NamespacedKey(args[0], args[1]));
                    if (recipe == null) {
                        api.sendPlayerMessage(player, "none", "recipe_editor", "not_existing", new String[]{"%recipe%", args[0] + ":" + args[1]});
                        return true;
                    }
                    if (customCrafting.getRecipeHandler().loadRecipeIntoCache(recipe, guiHandler1)) {
                        Bukkit.getScheduler().runTaskLater(customCrafting, () -> changeToCreator(guiHandler), 1);
                        return false;
                    } else {
                        api.sendPlayerMessage(player1, "none", "recipe_editor", "invalid_recipe", new String[]{"%recipe_type%", ((TestCache) guiHandler.getCustomCache()).getSetting().name()});
                        return true;
                    }
                }
                return false;
            });
            Bukkit.getScheduler().runTask(customCrafting, guiHandler::close);
            return true;
        })));
        registerButton(new ActionButton("delete_recipe", new ButtonState("delete_recipe", Material.BARRIER, (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            ((TestCache) guiHandler.getCustomCache()).getChatLists().setCurrentPageRecipes(1);
            customCrafting.getChatUtils().sendRecipeListExpanded(player);
            guiHandler.setChatInputAction((guiHandler1, player1, s, args) -> {
                if (args.length > 1) {
                    ICustomRecipe recipe = customCrafting.getRecipeHandler().getRecipe(new NamespacedKey(args[0], args[1]));
                    if (recipe == null) {
                        api.sendPlayerMessage(player, "none", "recipe_editor", "not_existing", new String[]{"%recipe%", args[0] + ":" + args[1]});
                        return true;
                    }
                    api.sendPlayerMessage(player1, "none", "recipe_editor", "delete.confirm", new String[]{"%recipe%", recipe.getNamespacedKey().toString()});
                    api.sendActionMessage(player1, new ClickData("$inventories.none.recipe_editor.messages.delete.confirmed$", (wolfyUtilities, player2) -> {
                        guiHandler1.openCluster();
                        Bukkit.getScheduler().runTaskAsynchronously(customCrafting, () -> recipe.delete(player2));
                    }), new ClickData("$inventories.none.recipe_editor.messages.delete.declined$", (wolfyUtilities, player2) -> guiHandler1.openCluster()));
                    guiHandler1.cancelChatEvent();
                    return true;
                }
                return false;
            });
            Bukkit.getScheduler().runTask(customCrafting, guiHandler::close);
            return true;
        })));
    }

    @EventHandler
    public void onUpdate(GuiUpdateEvent event) {
        if (event.verify(this)) {
            event.setButton(0, "back");
            event.setButton(20, "create_recipe");
            event.setButton(22, "edit_recipe");
            event.setButton(24, "delete_recipe");
        }
    }

    private void changeToCreator(GuiHandler guiHandler) {
        switch (((TestCache)guiHandler.getCustomCache()).getSetting()) {
            case WORKBENCH:
            case ELITE_WORKBENCH:
            case STONECUTTER:
            case BREWING_STAND:
            case GRINDSTONE:
            case CAULDRON:
            case ANVIL:
                guiHandler.changeToInv("recipe_creator", ((TestCache) guiHandler.getCustomCache()).getSetting().getId());
                break;
            case FURNACE:
            case CAMPFIRE:
            case SMOKER:
            case BLAST_FURNACE:
                guiHandler.changeToInv("recipe_creator", "cooking");
        }
    }
}