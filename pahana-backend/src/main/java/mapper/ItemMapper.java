package mapper;

import dtos.ItemDto;
import models.Item;

public class ItemMapper {
    public static Item toM(ItemDto dto) {
        Item m = new Item();
        m.setItemId(dto.getItId());
        m.setItemName(dto.getItName());
        m.setItemPrice(dto.getItPrice());
        return m;
    }

    public static ItemDto toD(Item m) {
        ItemDto dto = new ItemDto();
        dto.setItId(m.getItemId());
        dto.setItName(m.getItemName());
        dto.setItPrice(m.getItemPrice());
        return dto;
    }
}