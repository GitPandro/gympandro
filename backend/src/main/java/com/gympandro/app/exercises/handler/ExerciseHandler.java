package com.gympandro.app.exercises.handler;

import com.gympandro.app.Json;
import com.gympandro.app.exercises.model.Exercise;
import com.gympandro.app.exercises.repo.ExerciseRepo;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ExerciseHandler {

    public void handle(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod().toUpperCase();
        String path = ex.getRequestURI().getPath();

        try {
            if (method.equals("GET")) {
                handleGet(ex, path);
            } else if (method.equals("POST")) {
                handlePost(ex);
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
        // GET /api/exercises - Get all exercises
        // GET /api/exercises/{id} - Get exercise by id
        // GET /api/exercises/equipment/{equipment} - Get exercises by equipment

        String[] parts = path.split("/");

        if (parts.length == 3) {
            // GET /api/exercises
            var exercises = ExerciseRepo.findAll();
            send(ex, 200, Json.toJson(exercises));

        } else if (parts.length == 4) {
            // GET /api/exercises/{id}
            UUID id = UUID.fromString(parts[3]);
            Exercise exercise = ExerciseRepo.findById(id);
            if (exercise != null) {
                send(ex, 200, Json.toJson(exercise));
            } else {
                send(ex, 404, "{\"error\":\"Exercise not found\"}");
            }

        } else if (parts.length == 5 && parts[3].equals("equipment")) {
            // GET /api/exercises/equipment/{equipment}
            String equipment = parts[4];
            var exercises = ExerciseRepo.findByEquipment(equipment);
            send(ex, 200, Json.toJson(exercises));

        } else {
            send(ex, 404, "{\"error\":\"Not Found\"}");
        }
    }

    private void handlePost(HttpExchange ex) throws Exception {
        // POST /api/exercises - Create new exercise
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Exercise exercise = Json.fromJson(body, Exercise.class);

        if (exercise.id == null) {
            exercise.id = UUID.randomUUID();
        }

        ExerciseRepo.create(exercise);
        send(ex, 201, Json.toJson(exercise));
    }

    private void handlePut(HttpExchange ex) throws Exception {
        // PUT /api/exercises - Update exercise
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Exercise exercise = Json.fromJson(body, Exercise.class);

        ExerciseRepo.update(exercise);
        send(ex, 200, Json.toJson(exercise));
    }

    private void handleDelete(HttpExchange ex, String path) throws Exception {
        // DELETE /api/exercises/{id}
        String[] parts = path.split("/");

        if (parts.length == 4) {
            UUID id = UUID.fromString(parts[3]);
            ExerciseRepo.delete(id);
            send(ex, 200, "{\"message\":\"Exercise deleted successfully\"}");
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