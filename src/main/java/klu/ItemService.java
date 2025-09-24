package klu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepo itemRepository;

    public List<Items> getAllItems() {
        return itemRepository.findAll();
    }

    public Items getItemById(int id) {
        return itemRepository.findById(id).orElse(null);
    }

    public Items addItem(Items item) {
        return itemRepository.save(item);
    }

    public Items updateItem(int id, Items item) {
        if (itemRepository.existsById(id)) {
            item.setPid(id);
            return itemRepository.save(item);
        }
        return null;
    }

    public String deleteItem(int id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return "Item deleted successfully";
        }
        return "Item not found";
    }
}
