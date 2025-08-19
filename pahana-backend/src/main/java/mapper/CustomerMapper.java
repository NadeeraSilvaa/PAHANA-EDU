package mapper;

import dtos.CustomerDto;
import models.Customer;

public class CustomerMapper {
    public static Customer toM(CustomerDto dto) {
        Customer m = new Customer();
        m.setAccountNumber(dto.getAccNum());
        m.setName(dto.getCustName());
        m.setAddress(dto.getCustAddr());
        m.setPhone(dto.getCustPhone());
        m.setUnitsConsumed(dto.getCustUnits());
        return m;
    }

    public static CustomerDto toD(Customer m) {
        CustomerDto dto = new CustomerDto();
        dto.setAccNum(m.getAccountNumber());
        dto.setCustName(m.getName());
        dto.setCustAddr(m.getAddress());
        dto.setCustPhone(m.getPhone());
        dto.setCustUnits(m.getUnitsConsumed());
        return dto;
    }
}