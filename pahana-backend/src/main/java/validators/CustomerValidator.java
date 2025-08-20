package validators;

import models.Customer;

public class CustomerValidator {
    public static boolean isValid(Customer cust) {
        return cust.getName() != null && !cust.getName().isEmpty() &&
                cust.getAddress() != null && !cust.getAddress().isEmpty() &&
                cust.getPhone() != null && !cust.getPhone().isEmpty();
    }
}