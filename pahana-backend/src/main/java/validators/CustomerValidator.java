package validators;

import models.Customer;

public class CustomerValidator {
    public static boolean isValid(Customer cust) {
        if (cust.getName() == null || cust.getName().isEmpty()) return false;
        if (cust.getPhone() == null || cust.getPhone().length() < 10) return false;
        return true;
    }
}