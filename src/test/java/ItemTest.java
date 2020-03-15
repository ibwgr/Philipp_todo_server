import com.despegar.http.client.HttpResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ItemTest extends FunctionalTest{

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
        Assert.assertEquals("Hallo World Item", description);
    }
}
