package me.wolfyscript.customcrafting.handlers;

import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.configs.MainConfig;
import me.wolfyscript.customcrafting.configs.custom_data.KnowledgeBookData;
import me.wolfyscript.customcrafting.configs.recipebook.RecipeBookConfig;
import me.wolfyscript.customcrafting.recipes.types.workbench.ShapedCraftRecipe;
import me.wolfyscript.customcrafting.recipes.types.workbench.ShapelessCraftRecipe;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.config.ConfigAPI;
import me.wolfyscript.utilities.api.custom_items.CustomItem;
import me.wolfyscript.utilities.api.custom_items.api_references.WolfyUtilitiesRef;
import me.wolfyscript.utilities.api.language.Language;
import me.wolfyscript.utilities.api.language.LanguageAPI;
import me.wolfyscript.utilities.api.utils.NamespacedKey;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class ConfigHandler {

    private final CustomCrafting customCrafting;
    private final WolfyUtilities api;
    private final ConfigAPI configAPI;
    private final LanguageAPI languageAPI;
    private MainConfig mainConfig;
    private RecipeBookConfig recipeBookConfig;

    public ConfigHandler(CustomCrafting customCrafting) {
        this.api = WolfyUtilities.getAPI(customCrafting);
        this.customCrafting = customCrafting;
        this.configAPI = api.getConfigAPI();
        this.languageAPI = api.getLanguageAPI();
    }

    public void load() {
        this.mainConfig = new MainConfig(configAPI, customCrafting);
        mainConfig.loadDefaults();
        configAPI.registerConfig(mainConfig);
        try {
            loadLang();
        } catch (IOException e) {
            e.printStackTrace();
        }
        api.getConfigAPI().setPrettyPrinting(mainConfig.isPrettyPrinting());

        {
            //Creating the knowledgebook item and recipe
            NamespacedKey knowledgebookKey = new NamespacedKey("customcrafting", "knowledge_book");
            CustomItem knowledgeBook = new CustomItem(Material.WRITTEN_BOOK);
            knowledgeBook.setDisplayName(WolfyUtilities.translateColorCodes("&6Knowledge Book"));
            knowledgeBook.addLoreLine(WolfyUtilities.translateColorCodes("&7Contains some interesting recipes..."));
            knowledgeBook.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
            knowledgeBook.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            ((KnowledgeBookData) knowledgeBook.getCustomData("knowledge_book")).setEnabled(true);
            customCrafting.saveItem(knowledgebookKey, knowledgeBook);

            ShapelessCraftRecipe knowledgeBookCraft = new ShapelessCraftRecipe();
            knowledgeBookCraft.setIngredient('A', 0, new CustomItem(Material.BOOK));
            knowledgeBookCraft.setIngredient('B', 0, new CustomItem(Material.CRAFTING_TABLE));
            knowledgeBookCraft.setResult(0, new CustomItem(new WolfyUtilitiesRef(knowledgebookKey)));
            knowledgeBookCraft.setNamespacedKey(knowledgebookKey);
            knowledgeBookCraft.save();
        }
        {
            //Creating the advanced workbench item and recipe
            NamespacedKey workbenchkKey = new NamespacedKey("customcrafting", "workbench");

            CustomItem advancedWorkbench = new CustomItem(Material.CRAFTING_TABLE);
            advancedWorkbench.setDisplayName(WolfyUtilities.translateColorCodes("&6Advanced Workbench"));
            advancedWorkbench.addLoreLine(WolfyUtilities.translateColorCodes("&7Workbench for advanced crafting"));
            advancedWorkbench.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
            advancedWorkbench.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            customCrafting.saveItem(workbenchkKey, advancedWorkbench);

            ShapedCraftRecipe workbenchCraft = new ShapedCraftRecipe();
            workbenchCraft.setMirrorHorizontal(false);
            workbenchCraft.setIngredient('B', 0, new CustomItem(Material.GOLD_INGOT));
            workbenchCraft.setIngredient('E', 0, new CustomItem(Material.CRAFTING_TABLE));
            workbenchCraft.setIngredient('H', 0, new CustomItem(Material.GLOWSTONE_DUST));
            workbenchCraft.setResult(0, new CustomItem(new WolfyUtilitiesRef(workbenchkKey)));
            workbenchCraft.setNamespacedKey(workbenchkKey);
            workbenchCraft.save();
        }
        this.recipeBookConfig = new RecipeBookConfig(customCrafting);
    }

    public void loadLang() throws IOException {
        String chosenLang = customCrafting.getConfigHandler().getConfig().getString("language");
        customCrafting.saveResource("lang/en_US.json", true);
        customCrafting.saveResource("lang/de_DE.json", true);

        Language fallBackLanguage = new Language(customCrafting, "en_US");
        languageAPI.registerLanguage(fallBackLanguage);
        System.out.println("Loaded fallback language \"en_US\" v" + fallBackLanguage.getVersion() + " translated by " + fallBackLanguage.getAuthors().stream().collect(Collectors.joining()));

        File file = new File(customCrafting.getDataFolder(), "lang/" + chosenLang + ".json");
        if (file.exists()) {
            Language language = new Language(customCrafting, chosenLang);
            languageAPI.registerLanguage(language);
            languageAPI.setActiveLanguage(language);
            System.out.println("Loaded active language \"" + chosenLang + "\" v" + language.getVersion() + " translated by " + language.getAuthors().stream().collect(Collectors.joining()));
        }
    }

    public MainConfig getConfig() {
        return mainConfig;
    }

    public RecipeBookConfig getRecipeBookConfig() {
        return recipeBookConfig;
    }
}