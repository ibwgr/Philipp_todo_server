package ch.ibw.appl.todo.server.shared.infra;

import ch.ibw.appl.todo.server.item.infra.ItemController;
import spark.Service;

import static spark.Spark.get;

public class HttpServer {

    private final String httpPort;
    private Service server;

    public HttpServer(String httpPort) {
        this.httpPort = httpPort;
    }

    public void start() {
        server = Service.ignite();
        server.port(Integer.parseInt(httpPort));

        // create controller for resource --> /item
        new ItemController().createRoutes(server);

        server.awaitInitialization();


        // before filter
        server.before(((request, response) -> {
            if (!request.headers("accept").contains("application/json") ){
                response.status(406);
                response.body("Not Accepteble");
            }
        }));

        server.after(((request, response) ->{
            response.type("application/json");
        }));

    }

    public void stop() {
        server.stop();
        server.awaitStop();
    }
}