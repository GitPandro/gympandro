package com.gympandro.app.sheets.handler;

import com.gympandro.app.Json;
import com.gympandro.app.sheets.model.Sheet;
import com.gympandro.app.sheets.model.SheetExercise;
import com.gympandro.app.sheets.repo.SheetRepo;
import com.gympandro.app.sheets.repo.SheetExerciseRepo;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SheetHandler {

    public void handle(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod().toUpperCase();
        String path = ex.getRequestURI().getPath();

        try {
            if (method.equals("GET")) {
                handleGet(ex, path);
            } else if (method.equals("POST")) {
                handlePost(ex, path);
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
        // GET /api/sheets - Get all sheets
        // GET /api/sheets/{id} - Get sheet by id
        // GET /api/sheets/user/{userId} - Get sheets by user
        // GET /api/sheets/status/{status} - Get sheets by status (ACTIVE/ARCHIVED)
        // GET /api/sheets/{sheetId}/exercises - Get exercises for a sheet

        String[] parts = path.split("/");

        if (parts.length == 3) {
            // GET /api/sheets
            var sheets = SheetRepo.findAll();
            send(ex, 200, Json.toJson(sheets));

        } else if (parts.length == 4) {
            // GET /api/sheets/{id}
            UUID id = UUID.fromString(parts[3]);
            Sheet sheet = SheetRepo.findById(id);
            if (sheet != null) {
                send(ex, 200, Json.toJson(sheet));
            } else {
                send(ex, 404, "{\"error\":\"Sheet not found\"}");
            }

        } else if (parts.length == 5) {
            if (parts[3].equals("user")) {
                // GET /api/sheets/user/{userId}
                UUID userId = UUID.fromString(parts[4]);
                var sheets = SheetRepo.findByUserId(userId);
                send(ex, 200, Json.toJson(sheets));

            } else if (parts[3].equals("status")) {
                // GET /api/sheets/status/{status}
                String status = parts[4].toUpperCase();
                var sheets = SheetRepo.findByStatus(status);
                send(ex, 200, Json.toJson(sheets));

            } else if (parts[4].equals("exercises")) {
                // GET /api/sheets/{sheetId}/exercises
                UUID sheetId = UUID.fromString(parts[3]);
                var exercises = SheetExerciseRepo.findBySheetId(sheetId);
                send(ex, 200, Json.toJson(exercises));

            } else {
                send(ex, 404, "{\"error\":\"Not Found\"}");
            }
        } else {
            send(ex, 404, "{\"error\":\"Not Found\"}");
        }
    }

    private void handlePost(HttpExchange ex, String path) throws Exception {
        // POST /api/sheets - Create new sheet
        // POST /api/sheets/{sheetId}/exercises - Add exercise to sheet

        String[] parts = path.split("/");
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        if (parts.length == 3) {
            // POST /api/sheets
            Sheet sheet = Json.fromJson(body, Sheet.class);

            if (sheet.id == null) {
                sheet.id = UUID.randomUUID();
            }
            if (sheet.status == null) {
                sheet.status = "ACTIVE";
            }

            SheetRepo.create(sheet);
            send(ex, 201, Json.toJson(sheet));

        } else if (parts.length == 5 && parts[4].equals("exercises")) {
            // POST /api/sheets/{sheetId}/exercises
            UUID sheetId = UUID.fromString(parts[3]);
            SheetExercise exercise = Json.fromJson(body, SheetExercise.class);

            if (exercise.id == null) {
                exercise.id = UUID.randomUUID();
            }
            exercise.sheet_id = sheetId;

            SheetExerciseRepo.create(exercise);
            send(ex, 201, Json.toJson(exercise));

        } else {
            send(ex, 404, "{\"error\":\"Not Found\"}");
        }
    }

    private void handlePut(HttpExchange ex) throws Exception {
        // PUT /api/sheets - Update sheet
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Sheet sheet = Json.fromJson(body, Sheet.class);

        SheetRepo.update(sheet);
        send(ex, 200, Json.toJson(sheet));
    }

    private void handleDelete(HttpExchange ex, String path) throws Exception {
        // DELETE /api/sheets/{id}
        String[] parts = path.split("/");

        if (parts.length == 4) {
            UUID id = UUID.fromString(parts[3]);
            SheetRepo.delete(id);
            send(ex, 200, "{\"message\":\"Sheet deleted successfully\"}");
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