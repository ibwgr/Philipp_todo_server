package ch.ibw.appl.todo.server;

import ch.ibw.appl.todo.server.shared.infra.HttpServer;

public class Main {
    public static void main(String[] args) {
        new HttpServer("4567").start();

    }
}
