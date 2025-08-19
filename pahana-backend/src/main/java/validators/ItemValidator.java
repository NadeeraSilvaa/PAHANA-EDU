package validators;

import models.Item;

public class ItemValidator {
    public static boolean isValid(Item it) {
        if (it.getItemName() == null || it.getItemName().isEmpty()) return false;
        if (it.getItemPrice() <= 0) return false;
        return true;
    }
}