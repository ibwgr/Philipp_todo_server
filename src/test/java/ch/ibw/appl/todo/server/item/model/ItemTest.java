package ch.ibw.appl.todo.server.item.model;

import ch.ibw.appl.todo.server.FunctionalTest;
import ch.ibw.appl.todo.server.shared.service.JSONSerializer;
import com.despegar.http.client.HttpResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ItemTest extends FunctionalTest {

    @Test
    public void getItems_ResponseCodeIs200_Ok(){
        HttpResponse httpResponse = this.executeGet("/items");
        int code = httpResponse.code();

        Assert.assertEquals(200, code);
    }

    @Test
    public void getItems_AcceptTypeJSON_ReturnsResponseCode200(){
        HttpResponse httpResponse = this.executeGet("/items","text/html, application/json");
        int code = httpResponse.code();

        Assert.assertEquals(200, code);
    }

    @Test
    public void getItems_AcceptTypeNotJSON_ReturnsResponseCode406(){
        HttpResponse httpResponse = this.executeGet("/items","text/html, application/xml");
        int code = httpResponse.code();

        Assert.assertEquals(406, code);
    }

    @Test
    public void getItems_getDescriptionFirstItem_DescriptionOk(){
        HttpResponse httpResponse = this.executeGet("/items");
        int code = httpResponse.code();
        String body = new String(httpResponse.body());

        List<Item> deserializedItems = new JSONSerializer().deserialize(body, new TypeReference<ArrayList<Item>>() {});
        String description = deserializedItems.get(0).description;

        Assert.assertEquals(200, code);
        Assert.assertEquals("Hallo World ch.ibw.appl.todo.server.item.model.Item", description);
    }

    @Test
    public void getItemByID_ReturnsItemIfExist_OK(){
        HttpResponse httpResponse = this.executeGet("/items/1");
        int code = httpResponse.code();
        String body = new String(httpResponse.body());

        Assert.assertEquals(200, code);

        Item deserializedItem = new JSONSerializer().deserialize(body, new TypeReference<Item>() {});
        String description = deserializedItem.description;

        Assert.assertEquals("Hallo World ch.ibw.appl.todo.server.item.model.Item", description);
    }

    @Test
    public void getItemByID_ItemDoesNotExist_ReturnResponseCode404(){
        HttpResponse httpResponse = this.executeGet("/items/26");
        int code = httpResponse.code();
        String body = new String(httpResponse.body());

        Assert.assertEquals(404, code);
    }

    @Test
    public void createUnexistingItemIsOK(){
        HttpResponse httpResponse = this.executePost("/items", new Item("another item"));
        Assert.assertEquals(HttpStatus.CREATED_201, httpResponse.code());

        httpResponse = this.executeGet("/items/3");
        Assert.assertEquals(200, httpResponse.code());

    }

    @Test
    public void deleteExistingItem_ItemDoesExist_ItemDeleted_OK(){
        // create
        HttpResponse httpResponse = this.executePost("/items", new Item("new item"));
        httpResponse = this.executeGet("/items/3");
        int code = httpResponse.code();

        Assert.assertEquals(200, code);

        // delete
        httpResponse = this.executeDelete("/items/3");
        code = httpResponse.code();

        Assert.assertEquals(200, code);

        // delete again
        httpResponse = this.executeGet("/items/3");
        code = httpResponse.code();

        Assert.assertEquals(404, code);

    }

    @Test
    public void deleteInexistingItem_ReturnsItemNotFound(){
        HttpResponse httpResponse = this.executeDelete("/items/6");
        int code = httpResponse.code();

        Assert.assertEquals(404, code);

    }


    @Test
    public void getSearchedItem_FindItem_ReturnCorrectItem(){
        HttpResponse httpResponse = this.executeGet("/items?filter=description:kaufen");
        int code = httpResponse.code();
        Assert.assertEquals(HttpStatus.OK_200, code);

        String body = new String(httpResponse.body());
        List<Item> deserializedItemList = new JSONSerializer().deserialize(body, new TypeReference<List<Item>>() {});
        Assert.assertEquals(1, deserializedItemList.size());

        String description = deserializedItemList.get(0).description;

        Assert.assertEquals("Einkaufen für Geburtstag", description);
    }

    @Test
    public void getSearchedItem_ItemNotFound_ReturnNoMatches(){
        HttpResponse httpResponse = this.executeGet("/items?filter=description:Klettern");
        int code = httpResponse.code();
        Assert.assertEquals(HttpStatus.OK_200, code);

        String body = new String(httpResponse.body());
        List<Item> deserializedItemList = new JSONSerializer().deserialize(body, new TypeReference<List<Item>>() {});
        Assert.assertEquals(0, deserializedItemList.size());
    }

}
