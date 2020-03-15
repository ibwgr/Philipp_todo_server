import spark.Service;

import java.util.ArrayList;
import java.util.List;

public class ItemController {
    List<Item> items;
    // constructors
    public ItemController() {
        items = new ArrayList<>();
        Item item1 = new Item();
        item1.description = "Hallo World Item";
        items.add(item1);
    }

    public void createRoutes(Service server) {
        server.get("/items", (request, response) -> {

            response.type("application/json");

            return items;

        }, items -> new JSONSerializer().serialize(items));
    }
}
