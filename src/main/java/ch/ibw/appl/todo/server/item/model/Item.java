package ch.ibw.appl.todo.server.item.model;

import java.util.Date;

public class Item {

    public String description;
    public long id;

    
    public Item(){}

    public Item(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
