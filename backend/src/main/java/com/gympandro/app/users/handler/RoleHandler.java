package com.gympandro.app.users.handler;

import com.gympandro.app.Json;
import com.gympandro.app.users.repo.RoleRepo;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RoleHandler {
    public void handle(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod().toUpperCase();
        if (!method.equals("GET")) {
            send(ex, 405, "{\"error\":\"Method Not Allowed\"}");
            return;
        }

        try {
            var roles = RoleRepo.findAll();
            send(ex, 200, Json.toJson(roles));
        } catch (Exception e) {
            e.printStackTrace();
            send(ex, 500, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private void send(HttpExchange ex, int code, String body) throws IOException {
        var bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(code, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.close();
    }
}