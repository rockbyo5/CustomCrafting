package me.wolfyscript.customcrafting.gui.recipe_creator.recipe_creators;

import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.data.PlayerStatistics;
import me.wolfyscript.customcrafting.data.TestCache;
import me.wolfyscript.customcrafting.gui.recipe_creator.buttons.CookingContainerButton;
import me.wolfyscript.customcrafting.gui.recipe_creator.buttons.HiddenButton;
import me.wolfyscript.customcrafting.gui.recipe_creator.buttons.SaveButton;
import me.wolfyscript.customcrafting.recipes.types.CustomCookingRecipe;
import me.wolfyscript.utilities.api.inventory.GuiUpdateEvent;
import me.wolfyscript.utilities.api.inventory.InventoryAPI;
import me.wolfyscript.utilities.api.inventory.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.button.buttons.ActionButton;
import me.wolfyscript.utilities.api.inventory.button.buttons.ChatInputButton;
import me.wolfyscript.utilities.api.inventory.button.buttons.ToggleButton;
import me.wolfyscript.utilities.api.utils.inventory.PlayerHeadUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

public class CookingCreator extends RecipeCreator {

    public CookingCreator(InventoryAPI inventoryAPI, CustomCrafting customCrafting) {
        super("cooking", inventoryAPI, 45, customCrafting);
    }

    @Override
    public void onInit() {
        registerButton(new ActionButton("back", new ButtonState("none", "back", PlayerHeadUtils.getViaValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY0Zjc3OWE4ZTNmZmEyMzExNDNmYTY5Yjk2YjE0ZWUzNWMxNmQ2NjllMTljNzVmZDFhN2RhNGJmMzA2YyJ9fX0="), (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            guiHandler.openCluster("none");
            return true;
        })));

        registerButton(new SaveButton());

        registerButton(new HiddenButton());

        registerButton(new CookingContainerButton(0, customCrafting));
        registerButton(new CookingContainerButton(1, customCrafting));

        registerButton(new ChatInputButton("xp", new ButtonState("xp", Material.EXPERIENCE_BOTTLE, (hashMap, guiHandler, player, itemStack, slot, help) -> {
            hashMap.put("%XP%", ((TestCache) guiHandler.getCustomCache()).getCookingRecipe().getExp());
            return itemStack;
        }), (guiHandler, player, s, args) -> {
            float xp;
            try {
                xp = Float.parseFloat(args[0]);
            } catch (NumberFormatException e) {
                api.sendPlayerMessage(player, "recipe_creator", "valid_number");
                return true;
            }
            ((TestCache) guiHandler.getCustomCache()).getCookingRecipe().setExp(xp);
            return false;
        }));
        registerButton(new ChatInputButton("cooking_time", new ButtonState("cooking_time", Material.COAL, (hashMap, guiHandler, player, itemStack, slot, help) -> {
            hashMap.put("%TIME%", ((TestCache) guiHandler.getCustomCache()).getCookingRecipe().getCookingTime());
            return itemStack;
        }), (guiHandler, player, s, args) -> {
            int time;
            try {
                time = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                api.sendPlayerMessage(player, "recipe_creator", "valid_number");
                return true;
            }
            ((TestCache) guiHandler.getCustomCache()).getCookingRecipe().setCookingTime(time);
            return false;
        }));
    }

    @EventHandler
    public void onUpdate(GuiUpdateEvent event) {
        if (event.verify(this)) {
            event.setButton(0, "back");
            TestCache cache = (TestCache) event.getGuiHandler().getCustomCache();
            ((ToggleButton) event.getGuiWindow().getButton("hidden")).setState(event.getGuiHandler(), cache.getCookingRecipe().isHidden());

            PlayerStatistics playerStatistics = CustomCrafting.getPlayerStatistics(event.getPlayer());

            event.setButton(3, "hidden");
            event.setButton(5, "recipe_creator", "conditions");
            event.setButton(20, "none", playerStatistics.getDarkMode() ? "glass_gray" : "glass_white");
            event.setButton(11, "cooking.container_0");
            event.setButton(24, "cooking.container_1");
            event.setButton(10, "none", playerStatistics.getDarkMode() ? "glass_gray" : "glass_white");
            event.setButton(12, "none", playerStatistics.getDarkMode() ? "glass_gray" : "glass_white");
            event.setButton(22, "xp");
            event.setButton(29, "cooking_time");
            event.setButton(44, "save");
        }
    }

    public boolean validToSave(TestCache cache) {
        switch (cache.getSetting()) {
            case BLAST_FURNACE:
            case SMOKER:
            case CAMPFIRE:
            case FURNACE:
                CustomCookingRecipe furnace = cache.getCookingRecipe();
                if (furnace.getSource() != null && !furnace.getSource().isEmpty() && furnace.getCustomResults() != null && !furnace.getCustomResults().isEmpty())
                    return true;
        }
        return false;
    }
}