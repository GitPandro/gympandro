package com.gympandro.app.sheets.handler;

import com.gympandro.app.Json;
import com.gympandro.app.exercises.repo.ExerciseRepo;
import com.gympandro.app.sheets.model.SheetExercise;
import com.gympandro.app.sheets.repo.SheetExerciseRepo;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SheetExerciseHandler {

    public void handle(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod().toUpperCase();
        String path = ex.getRequestURI().getPath();

        try {
            if (method.equals("GET")) {
                handleGet(ex, path);
            } else if (method.equals("PUT")) {
                handlePut(ex);
            } else if (method.equals("DELETE")) {
                handleDelete(ex, path);
            } else {
                send(ex, 405, "{\"error\":\"Method Not Allowed\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            send(ex, 500, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private void handleGet(HttpExchange ex, String path) throws Exception {
        // GET /api/sheet-exercises/{id} - Get single sheet exercise by id
        String[] parts = path.split("/");

        if (parts.length == 4) {
            UUID id = UUID.fromString(parts[3]);
            SheetExercise exercise = SheetExerciseRepo.findById(id);
            if (exercise != null) {
                send(ex, 200, Json.toJson(exercise));
            } else {
                send(ex, 404, "{\"error\":\"Sheet exercise not found\"}");
            }
        } else {
            send(ex, 404, "{\"error\":\"Not Found\"}");
        }
    }

    private void handlePut(HttpExchange ex) throws Exception {
        // PUT /api/sheet-exercises - Update sheet exercise
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        SheetExercise exercise = Json.fromJson(body, SheetExercise.class);

        SheetExerciseRepo.update(exercise);
        send(ex, 200, Json.toJson(exercise));
    }

    private void handleDelete(HttpExchange ex, String path) throws Exception {
        // DELETE /api/sheet-exercises/{id}
        String[] parts = path.split("/");

        if (parts.length == 4) {
            UUID id = UUID.fromString(parts[3]);
            SheetExerciseRepo.delete(id);
            send(ex, 200, "{\"message\":\"Sheet exercise deleted successfully\"}");
        } else {
            send(ex, 404, "{\"error\":\"Not Found\"}");
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