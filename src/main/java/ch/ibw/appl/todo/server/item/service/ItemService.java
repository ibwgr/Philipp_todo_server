package ch.ibw.appl.todo.server.item.service;

import ch.ibw.appl.todo.server.item.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemService {
    private final List<Item> items;
    private long nextId = 0;

    public ItemService(Boolean isTest) {
        items = new ArrayList<>();
        if (isTest){
            Item item1 = new Item("Hallo World ch.ibw.appl.todo.server.item.model.Item");
            this.create(item1);
            Item item2 = new Item("Einkaufen fuer Geburtstag");
            this.create(item2);
            Item item3 = new Item("Ich bin Nr. 3");
            this.create(item3);
        }

    }

    public Item create(Item item) {
        item.id = ++nextId;
        items.add(item);
        return item;
    }

    public Item getById(long requestedId) {
        for (Item item : items) {
            if (item.id == requestedId) {
                return item;
            }
        }
        return null;
    }

    public String deleteById(long requestedId) {
        for (Item item : items) {
            if (item.id == requestedId) {
                items.remove(item);
                return "";
            }
        }
        return null;
    }

    public List<Item> search(String[] keyValue) {
        if (keyValue[0].equalsIgnoreCase("description")) {
            String searchTerm = keyValue[1].toLowerCase();
            List<Item> matches = new ArrayList<>();

            for (Item item : items) {
                if (item.description.toLowerCase().contains(searchTerm)) {
                    matches.add(item);
                }
            }

            return matches;
        }
        return null;
    }

    public List<Item> getAllItems() {
        return items;
    }
}
