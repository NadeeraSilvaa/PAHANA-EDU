package validators;

import models.Item;

public class ItemValidator {
    public static boolean isValid(Item it) {
        return it.getItemName() != null && !it.getItemName().isEmpty() &&
                it.getAuthor() != null && !it.getAuthor().isEmpty() &&
                it.getItemPrice() > 0 &&
                it.getCategory() != null && !it.getCategory().isEmpty();
    }
}