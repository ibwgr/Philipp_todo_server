package ch.ibw.appl.todo.server.item.infra;

import ch.ibw.appl.todo.server.item.service.ItemService;
import ch.ibw.appl.todo.server.shared.service.JSONSerializer;
import ch.ibw.appl.todo.server.item.model.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.jetty.http.HttpStatus;
import spark.Service;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.halt;

public class ItemController {
    ItemService service = new ItemService();

    List<Item> items;
    // constructors
    public ItemController() {


    }

    public void createRoutes(Service server) {

        server.get("/items", (request, response) -> {
            String filter = request.queryParamOrDefault("filter","");
            if (!filter.isEmpty()){
                String[] keyValue = filter.split(":");
                return service.search(keyValue);
            }
            return service.getAllItems();

        }, items -> new JSONSerializer().serialize(items));



        server.get("/items/:id", (request, response) -> {
            long requestedId = Long.parseLong(request.params(":id")) ;
            return service.getById(requestedId);
        }, items -> new JSONSerializer().serialize(items));




        server.post("/items", (request, response) -> {

            Item item = new JSONSerializer().deserialize(request.body(), new TypeReference<Item>() {} );
            service.create(item);
            response.status(HttpStatus.CREATED_201);
            return service.create(item);

        }, items -> new JSONSerializer().serialize(items));



        server.delete("/items/:id", (request, response) -> {
            long requestedId = Long.parseLong(request.params("id")) ;
            return service.deleteById(requestedId);
        }, items -> new JSONSerializer().serialize(items));

    }

}
