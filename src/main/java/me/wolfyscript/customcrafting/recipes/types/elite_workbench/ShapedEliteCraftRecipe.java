package me.wolfyscript.customcrafting.recipes.types.elite_workbench;

import me.wolfyscript.customcrafting.recipes.types.IShapedCraftingRecipe;
import me.wolfyscript.customcrafting.recipes.types.workbench.CraftingData;
import me.wolfyscript.customcrafting.utils.geom.Vec2d;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.custom_items.CustomItem;
import me.wolfyscript.utilities.api.utils.NamespacedKey;
import me.wolfyscript.utilities.api.utils.inventory.InventoryUtils;
import me.wolfyscript.utilities.libraries.com.fasterxml.jackson.core.JsonGenerator;
import me.wolfyscript.utilities.libraries.com.fasterxml.jackson.databind.JsonNode;
import me.wolfyscript.utilities.libraries.com.fasterxml.jackson.databind.SerializerProvider;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapedEliteCraftRecipe extends EliteCraftingRecipe implements IShapedCraftingRecipe {

    private String[] shape, shapeMirrorHorizontal, shapeMirrorVertical, shapeRotated;
    private boolean mirrorHorizontal, mirrorVertical, mirrorRotation;

    public ShapedEliteCraftRecipe(NamespacedKey namespacedKey, JsonNode node) {
        super(namespacedKey, node);
        this.shapeless = false;
        constructShape();
        JsonNode mirrorNode = node.path("mirror");
        this.mirrorHorizontal = mirrorNode.path("horizontal").asBoolean(true);
        this.mirrorVertical = mirrorNode.path("vertical").asBoolean(false);
        this.mirrorRotation = mirrorNode.path("rotation").asBoolean(false);
    }

    public ShapedEliteCraftRecipe(){
        super();
        this.shapeless = false;
    }

    public ShapedEliteCraftRecipe(EliteCraftingRecipe eliteCraftingRecipe){
        super(eliteCraftingRecipe);
        this.shapeless = false;
        constructShape();
        this.mirrorHorizontal = true;
        this.mirrorVertical = false;
        this.mirrorRotation = false;
    }

    public ShapedEliteCraftRecipe(ShapedEliteCraftRecipe eliteCraftingRecipe){
        this((EliteCraftingRecipe)eliteCraftingRecipe);
        this.mirrorHorizontal = eliteCraftingRecipe.mirrorHorizontal();
        this.mirrorVertical = eliteCraftingRecipe.mirrorVertical();
        this.mirrorRotation = eliteCraftingRecipe.mirrorRotation();
    }

    @Override
    public String[] getShapeMirrorHorizontal() {
        return shapeMirrorHorizontal;
    }

    @Override
    public String[] getShapeMirrorVertical() {
        return shapeMirrorVertical;
    }

    @Override
    public String[] getShapeRotated() {
        return shapeRotated;
    }

    @Override
    public String[] getShape() {
        return shape;
    }

    @Override
    public boolean mirrorHorizontal() {
        return mirrorHorizontal;
    }

    @Override
    public boolean mirrorVertical() {
        return mirrorVertical;
    }

    @Override
    public boolean mirrorRotation() {
        return mirrorRotation;
    }

    @Override
    public void constructShape() {
        Map<Character, List<CustomItem>> ingredients = getIngredients();
        String[] shape = new String[6];
        int index = 0;
        int row = 0;
        for (int i = 0; i < 36; i++) {
            char ingrd = LETTERS[i];
            List<CustomItem> items = ingredients.get(ingrd);
            if (items == null || InventoryUtils.isCustomItemsListEmpty(items)) {
                if (shape[row] != null) {
                    shape[row] = shape[row] + " ";
                } else {
                    shape[row] = " ";
                }
            } else {
                if (shape[row] != null) {
                    shape[row] = shape[row] + ingrd;
                } else {
                    shape[row] = String.valueOf(ingrd);
                }
            }
            index++;
            if ((index % 6) == 0) {
                row++;
            }
        }
        this.shape = WolfyUtilities.formatShape(shape).toArray(new String[0]);
        this.shapeMirrorVertical = new String[]{"      ", "      ", "      ", "      ", "      ", "      "};
        int j = 0;
        for (int i = shape.length - 1; i > 0; i--) {
            this.shapeMirrorVertical[j] = shape[i];
            j++;
        }
        this.shapeMirrorVertical = WolfyUtilities.formatShape(this.shapeMirrorVertical).toArray(new String[0]);
        this.shapeMirrorHorizontal = this.shape.clone();
        for (int i = 0; i < this.shapeMirrorHorizontal.length; i++) {
            this.shapeMirrorHorizontal[i] = new StringBuilder(this.shapeMirrorHorizontal[i]).reverse().toString();
        }
        this.shapeRotated = this.shapeMirrorVertical.clone();
        for (int i = 0; i < this.shapeRotated.length; i++) {
            this.shapeRotated[i] = new StringBuilder(this.shapeRotated[i]).reverse().toString();
        }

        int size = Math.max(this.shape.length, this.shape[0].length());
        if(size <= 3){
            requiredGridSize = 3;
        }else if(size <= 4){
            requiredGridSize = 4;
        }else if(size <= 5){
            requiredGridSize = 5;
        }else if(size <= 6){
            requiredGridSize = 6;
        }
    }

    public void setMirrorHorizontal(boolean mirrorHorizontal) {
        this.mirrorHorizontal = mirrorHorizontal;
    }

    public void setMirrorVertical(boolean mirrorVertical) {
        this.mirrorVertical = mirrorVertical;
    }

    public void setMirrorRotation(boolean mirrorRotation) {
        this.mirrorRotation = mirrorRotation;
    }

    @Override
    public boolean isShapeless() {
        return false;
    }

    @Override
    public CraftingData check(List<List<ItemStack>> matrix) {
        CraftingData craftingData = checkShape(matrix, getShape());
        if (craftingData != null) {
            return craftingData;
        }
        if (mirrorHorizontal()) {
            craftingData = checkShape(matrix, getShapeMirrorHorizontal());
            if (craftingData != null) {
                return craftingData;
            }
        }
        if (mirrorVertical()) {
            craftingData = checkShape(matrix, getShapeMirrorVertical());
            if (craftingData != null) {
                return craftingData;
            }
        }
        if (mirrorHorizontal() && mirrorVertical() && mirrorRotation()) {
            craftingData = checkShape(matrix, getShapeRotated());
            return craftingData;
        }
        return null;
    }

    private CraftingData checkShape(List<List<ItemStack>> matrix, String[] shape) {
        List<Character> containedKeys = new ArrayList<>();
        HashMap<Vec2d, CustomItem> foundItems = new HashMap<>();
        if (getIngredients() == null || getIngredients().isEmpty()) {
            return null;
        }
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                if ((matrix.get(i).get(j) != null && i < shape.length && j < shape[i].length() && shape[i].charAt(j) != ' ')) {
                    CustomItem item = checkIngredient(matrix.get(i).get(j), getIngredients().get(shape[i].charAt(j)));
                    if (item == null) {
                        return null;
                    } else {
                        foundItems.put(new Vec2d(j, i), item);
                        containedKeys.add(shape[i].charAt(j));
                    }
                } else if (!(matrix.get(i).get(j) == null && (i >= shape.length || j >= shape[i].length() || shape[i].charAt(j) == ' '))) {
                    return null;
                }
            }
        }
        if (containedKeys.containsAll(getIngredients().keySet())) {
            return new CraftingData(this, foundItems);
        }
        return null;
    }

    private CustomItem checkIngredient(ItemStack input, List<CustomItem> ingredients) {
        for (CustomItem ingredient : ingredients) {
            if (ingredient.isSimilar(input, isExactMeta())) {
                return ingredient.clone();
            }
        }
        return null;
    }

    @Override
    public ShapedEliteCraftRecipe clone() {
        return new ShapedEliteCraftRecipe(this);
    }

    @Override
    public void writeToJson(JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        super.writeToJson(gen, serializerProvider);

        gen.writeObjectField("shape", shape);
        {
            gen.writeObjectFieldStart("mirror");
            gen.writeBooleanField("horizontal", this.mirrorHorizontal);
            gen.writeBooleanField("vertical", this.mirrorVertical);
            gen.writeBooleanField("rotation", this.mirrorRotation);
            gen.writeEndObject();
        }
    }
}