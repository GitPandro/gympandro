package com.gympandro.app.sheets.repo;

import com.gympandro.app.db.Db;
import com.gympandro.app.sheets.model.SheetExercise;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.*;

public class SheetExerciseRepo {

    public static List<SheetExercise> findBySheetId(UUID sheetId) throws SQLException {
        String sql = """
                SELECT se.id, se.sheet_id, se.exercise_id, se.sets, se.reps, se."restSec", se.notes,
                       se."createdAt", se."createdBy", se."modifiedAt", se."modifiedBy",
                       e.name AS exerciseName, e.equipment AS exerciseEquipment
                FROM sheet_exercises se
                LEFT JOIN exercises e ON se.exercise_id = e.id
                WHERE se.sheet_id = ?
                ORDER BY se."createdAt"
                """;

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, sheetId);

            try (ResultSet rs = ps.executeQuery()) {
                List<SheetExercise> list = new ArrayList<>();
                while (rs.next()) {
                    SheetExercise se = new SheetExercise();
                    se.id = (UUID) rs.getObject("id");
                    se.sheet_id = (UUID) rs.getObject("sheet_id");
                    se.exercise_id = (UUID) rs.getObject("exercise_id");
                    se.sets = rs.getInt("sets");
                    se.reps = rs.getInt("reps");
                    se.restSec = rs.getString("restSec");
                    se.notes = rs.getString("notes");
                    se.createdAt = rs.getObject("createdAt", ZonedDateTime.class);
                    se.createdBy = (UUID) rs.getObject("createdBy");
                    se.modifiedAt = rs.getObject("modifiedAt", ZonedDateTime.class);
                    se.modifiedBy = (UUID) rs.getObject("modifiedBy");
                    se.exerciseName = rs.getString("exerciseName");
                    se.exerciseEquipment = rs.getString("exerciseEquipment");
                    list.add(se);
                }
                return list;
            }
        }
    }

    public static SheetExercise findById(UUID id) throws SQLException {
        String sql = """
                SELECT se.id, se.sheet_id, se.exercise_id, se.sets, se.reps, se."restSec", se.notes,
                       se."createdAt", se."createdBy", se."modifiedAt", se."modifiedBy",
                       e.name AS exerciseName, e.equipment AS exerciseEquipment
                FROM sheet_exercises se
                LEFT JOIN exercises e ON se.exercise_id = e.id
                WHERE se.id = ?
                """;

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SheetExercise se = new SheetExercise();
                    se.id = (UUID) rs.getObject("id");
                    se.sheet_id = (UUID) rs.getObject("sheet_id");
                    se.exercise_id = (UUID) rs.getObject("exercise_id");
                    se.sets = rs.getInt("sets");
                    se.reps = rs.getInt("reps");
                    se.restSec = rs.getString("restSec");
                    se.notes = rs.getString("notes");
                    se.createdAt = rs.getObject("createdAt", ZonedDateTime.class);
                    se.createdBy = (UUID) rs.getObject("createdBy");
                    se.modifiedAt = rs.getObject("modifiedAt", ZonedDateTime.class);
                    se.modifiedBy = (UUID) rs.getObject("modifiedBy");
                    se.exerciseName = rs.getString("exerciseName");
                    se.exerciseEquipment = rs.getString("exerciseEquipment");
                    return se;
                }
                return null;
            }
        }
    }

    public static void create(SheetExercise se) throws SQLException {
        String sql = """
                INSERT INTO sheet_exercises (id, sheet_id, exercise_id, sets, reps, "restSec", notes, "createdBy", "modifiedBy")
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, se.id);
            ps.setObject(2, se.sheet_id);
            ps.setObject(3, se.exercise_id);
            ps.setInt(4, se.sets);
            ps.setInt(5, se.reps);
            ps.setString(6, se.restSec);
            ps.setString(7, se.notes);
            ps.setObject(8, se.createdBy);
            ps.setObject(9, se.modifiedBy);
            ps.executeUpdate();
        }
    }

    public static void update(SheetExercise se) throws SQLException {
        String sql = """
                UPDATE sheet_exercises
                SET sets = ?, reps = ?, "restSec" = ?, notes = ?, "modifiedBy" = ?
                WHERE id = ?
                """;

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, se.sets);
            ps.setInt(2, se.reps);
            ps.setString(3, se.restSec);
            ps.setString(4, se.notes);
            ps.setObject(5, se.modifiedBy);
            ps.setObject(6, se.id);
            ps.executeUpdate();
        }
    }

    public static void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM sheet_exercises WHERE id = ?";

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    public static void deleteBySheetId(UUID sheetId) throws SQLException {
        String sql = "DELETE FROM sheet_exercises WHERE sheet_id = ?";

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, sheetId);
            ps.executeUpdate();
        }
    }
}