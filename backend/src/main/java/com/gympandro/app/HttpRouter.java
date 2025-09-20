package com.gympandro.app;

import com.gympandro.app.clients.Client;
import com.gympandro.app.clients.ClientRepo;
import com.gympandro.app.db.Db;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpRouter implements HttpHandler {
    @Override
    public void handle(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        String method = ex.getRequestMethod();

        // CORS base per test
        ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        ex.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        if ("OPTIONS".equalsIgnoreCase(method)) {
            ex.sendResponseHeaders(204, -1);
            ex.close();
            return;
        }

        try {
            if ("/api/health".equals(path) && "GET".equals(method)) {
                ok(ex, "{\"status\":\"UP\"}");
                return;
            }

            if ("/api/clients".equals(path)) {
                if ("GET".equals(method)) {
                    List<Client> all = ClientRepo.findAll();
                    okJson(ex, Json.toJson(all));
                    return;
                } else if ("POST".equals(method)) {
                    String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Client c = Json.fromJson(body, Client.class);
                    Client saved = ClientRepo.insert(c);
                    okJson(ex, Json.toJson(saved));
                    return;
                }
            }

            // 404
            notFound(ex);
        } catch (Exception e) {
            e.printStackTrace();
            error(ex, 500, "{\"error\":\"" + e.getMessage().replace("\"","'") + "\"}");
        }
    }

    private void ok(HttpExchange ex, String body) throws IOException {
        ex.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(200, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.close();
    }

    private void okJson(HttpExchange ex, String json) throws IOException { ok(ex, json); }

    private void notFound(HttpExchange ex) throws IOException { error(ex, 404, "{\"error\":\"Not Found\"}"); }

    private void error(HttpExchange ex, int code, String json) throws IOException {
        ex.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(code, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.close();
    }
}