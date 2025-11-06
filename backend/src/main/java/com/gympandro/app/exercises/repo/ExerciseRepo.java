package com.gympandro.app.exercises.repo;

import com.gympandro.app.db.Db;
import com.gympandro.app.exercises.model.Exercise;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.*;

public class ExerciseRepo {

    public static List<Exercise> findAll() throws SQLException {
        String sql = """
                SELECT id, name, equipment, description,
                       "createdAt", "createdBy", "modifiedAt", "modifiedBy"
                FROM exercises
                ORDER BY name
                """;

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            List<Exercise> list = new ArrayList<>();
            while (rs.next()) {
                Exercise e = new Exercise();
                e.id = (UUID) rs.getObject("id");
                e.name = rs.getString("name");
                e.equipment = rs.getString("equipment");
                e.description = rs.getString("description");
                e.createdAt = rs.getObject("createdAt", ZonedDateTime.class);
                e.createdBy = (UUID) rs.getObject("createdBy");
                e.modifiedAt = rs.getObject("modifiedAt", ZonedDateTime.class);
                e.modifiedBy = (UUID) rs.getObject("modifiedBy");
                list.add(e);
            }
            return list;
        }
    }

    public static Exercise findById(UUID id) throws SQLException {
        String sql = """
                SELECT id, name, equipment, description,
                       "createdAt", "createdBy", "modifiedAt", "modifiedBy"
                FROM exercises
                WHERE id = ?
                """;

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Exercise e = new Exercise();
                    e.id = (UUID) rs.getObject("id");
                    e.name = rs.getString("name");
                    e.equipment = rs.getString("equipment");
                    e.description = rs.getString("description");
                    e.createdAt = rs.getObject("createdAt", ZonedDateTime.class);
                    e.createdBy = (UUID) rs.getObject("createdBy");
                    e.modifiedAt = rs.getObject("modifiedAt", ZonedDateTime.class);
                    e.modifiedBy = (UUID) rs.getObject("modifiedBy");
                    return e;
                }
                return null;
            }
        }
    }

    public static List<Exercise> findByEquipment(String equipment) throws SQLException {
        String sql = """
                SELECT id, name, equipment, description,
                       "createdAt", "createdBy", "modifiedAt", "modifiedBy"
                FROM exercises
                WHERE equipment = ?
                ORDER BY name
                """;

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, equipment);

            try (ResultSet rs = ps.executeQuery()) {
                List<Exercise> list = new ArrayList<>();
                while (rs.next()) {
                    Exercise e = new Exercise();
                    e.id = (UUID) rs.getObject("id");
                    e.name = rs.getString("name");
                    e.equipment = rs.getString("equipment");
                    e.description = rs.getString("description");
                    e.createdAt = rs.getObject("createdAt", ZonedDateTime.class);
                    e.createdBy = (UUID) rs.getObject("createdBy");
                    e.modifiedAt = rs.getObject("modifiedAt", ZonedDateTime.class);
                    e.modifiedBy = (UUID) rs.getObject("modifiedBy");
                    list.add(e);
                }
                return list;
            }
        }
    }

    public static void create(Exercise exercise) throws SQLException {
        String sql = """
                INSERT INTO exercises (id, name, equipment, description, "createdBy", "modifiedBy")
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, exercise.id);
            ps.setString(2, exercise.name);
            ps.setString(3, exercise.equipment);
            ps.setString(4, exercise.description);
            ps.setObject(5, exercise.createdBy);
            ps.setObject(6, exercise.modifiedBy);
            ps.executeUpdate();
        }
    }

    public static void update(Exercise exercise) throws SQLException {
        String sql = """
                UPDATE exercises
                SET name = ?, equipment = ?, description = ?, "modifiedBy" = ?
                WHERE id = ?
                """;

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, exercise.name);
            ps.setString(2, exercise.equipment);
            ps.setString(3, exercise.description);
            ps.setObject(4, exercise.modifiedBy);
            ps.setObject(5, exercise.id);
            ps.executeUpdate();
        }
    }

    public static void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM exercises WHERE id = ?";

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }
}