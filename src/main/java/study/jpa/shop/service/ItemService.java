package study.jpa.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.jpa.shop.domain.item.Item;
import study.jpa.shop.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }
}
