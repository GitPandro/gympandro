package com.gympandro.app.sheets.repo;

import com.gympandro.app.db.Db;
import com.gympandro.app.sheets.model.Sheet;

import java.sql.*;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.*;

public class SheetRepo {

    public static List<Sheet> findAll() throws SQLException {
        String sql = """
                SELECT s.id, s.user_id, s.title, s."startDate", s."endDate", s.status,
                       s."createdAt", s."createdBy", s."modifiedAt", s."modifiedBy"
                FROM sheets s
                ORDER BY s."createdAt" DESC
                """;

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            List<Sheet> list = new ArrayList<>();
            while (rs.next()) {
                Sheet s = new Sheet();
                s.id = (UUID) rs.getObject("id");
                s.user_id = (UUID) rs.getObject("user_id");
                s.title = rs.getString("title");
                s.startDate = rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null;
                s.endDate = rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null;
                s.status = rs.getString("status");
                s.createdAt = rs.getObject("createdAt", ZonedDateTime.class);
                s.createdBy = (UUID) rs.getObject("createdBy");
                s.modifiedAt = rs.getObject("modifiedAt", ZonedDateTime.class);
                s.modifiedBy = (UUID) rs.getObject("modifiedBy");
                list.add(s);
            }
            return list;
        }
    }

    public static Sheet findById(UUID id) throws SQLException {
        String sql = """
                SELECT s.id, s.user_id, s.title, s."startDate", s."endDate", s.status,
                       s."createdAt", s."createdBy", s."modifiedAt", s."modifiedBy",
                       u.name || ' ' || u.surname AS userName,
                       c.name || ' ' || c.surname AS creatorName
                FROM sheets s
                LEFT JOIN users u ON s.user_id = u.id
                LEFT JOIN users c ON s."createdBy" = c.id
                WHERE s.id = ?
                """;

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Sheet s = new Sheet();
                    s.id = (UUID) rs.getObject("id");
                    s.user_id = (UUID) rs.getObject("user_id");
                    s.title = rs.getString("title");
                    s.startDate = rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null;
                    s.endDate = rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null;
                    s.status = rs.getString("status");
                    s.createdAt = rs.getObject("createdAt", ZonedDateTime.class);
                    s.createdBy = (UUID) rs.getObject("createdBy");
                    s.modifiedAt = rs.getObject("modifiedAt", ZonedDateTime.class);
                    s.modifiedBy = (UUID) rs.getObject("modifiedBy");
                    s.userName = rs.getString("userName");
                    s.creatorName = rs.getString("creatorName");
                    return s;
                }
                return null;
            }
        }
    }

    public static List<Sheet> findByUserId(UUID userId) throws SQLException {
        String sql = """
                SELECT s.id, s.user_id, s.title, s."startDate", s."endDate", s.status,
                       s."createdAt", s."createdBy", s."modifiedAt", s."modifiedBy",
                       u.name || ' ' || u.surname AS userName,
                       c.name || ' ' || c.surname AS creatorName
                FROM sheets s
                LEFT JOIN users u ON s.user_id = u.id
                LEFT JOIN users c ON s."createdBy" = c.id
                WHERE s.user_id = ?
                ORDER BY s."createdAt" DESC
                """;

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                List<Sheet> list = new ArrayList<>();
                while (rs.next()) {
                    Sheet s = new Sheet();
                    s.id = (UUID) rs.getObject("id");
                    s.user_id = (UUID) rs.getObject("user_id");
                    s.title = rs.getString("title");
                    s.startDate = rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null;
                    s.endDate = rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null;
                    s.status = rs.getString("status");
                    s.createdAt = rs.getObject("createdAt", ZonedDateTime.class);
                    s.createdBy = (UUID) rs.getObject("createdBy");
                    s.modifiedAt = rs.getObject("modifiedAt", ZonedDateTime.class);
                    s.modifiedBy = (UUID) rs.getObject("modifiedBy");
                    s.userName = rs.getString("userName");
                    s.creatorName = rs.getString("creatorName");
                    list.add(s);
                }
                return list;
            }
        }
    }

    public static List<Sheet> findByStatus(String status) throws SQLException {
        String sql = """
                SELECT s.id, s.user_id, s.title, s."startDate", s."endDate", s.status,
                       s."createdAt", s."createdBy", s."modifiedAt", s."modifiedBy"
                FROM sheets s
                WHERE s.status = ?
                ORDER BY s."createdAt" DESC
                """;

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {
                List<Sheet> list = new ArrayList<>();
                while (rs.next()) {
                    Sheet s = new Sheet();
                    s.id = (UUID) rs.getObject("id");
                    s.user_id = (UUID) rs.getObject("user_id");
                    s.title = rs.getString("title");
                    s.startDate = rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null;
                    s.endDate = rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null;
                    s.status = rs.getString("status");
                    s.createdAt = rs.getObject("createdAt", ZonedDateTime.class);
                    s.createdBy = (UUID) rs.getObject("createdBy");
                    s.modifiedAt = rs.getObject("modifiedAt", ZonedDateTime.class);
                    s.modifiedBy = (UUID) rs.getObject("modifiedBy");
                    list.add(s);
                }
                return list;
            }
        }
    }

    public static void create(Sheet sheet) throws SQLException {
        String sql = """
                INSERT INTO sheets (id, user_id, title, "startDate", "endDate", status, "createdBy", "modifiedBy")
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, sheet.id);
            ps.setObject(2, sheet.user_id);
            ps.setString(3, sheet.title);
            ps.setDate(4, sheet.startDate != null ? Date.valueOf(sheet.startDate) : null);
            ps.setDate(5, sheet.endDate != null ? Date.valueOf(sheet.endDate) : null);
            ps.setString(6, sheet.status != null ? sheet.status : "ACTIVE");
            ps.setObject(7, sheet.createdBy);
            ps.setObject(8, sheet.modifiedBy);
            ps.executeUpdate();
        }
    }

    public static void update(Sheet sheet) throws SQLException {
        String sql = """
                UPDATE sheets
                SET title = ?, "startDate" = ?, "endDate" = ?, status = ?, "modifiedBy" = ?
                WHERE id = ?
                """;

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sheet.title);
            ps.setDate(2, sheet.startDate != null ? Date.valueOf(sheet.startDate) : null);
            ps.setDate(3, sheet.endDate != null ? Date.valueOf(sheet.endDate) : null);
            ps.setString(4, sheet.status);
            ps.setObject(5, sheet.modifiedBy);
            ps.setObject(6, sheet.id);
            ps.executeUpdate();
        }
    }

    public static void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM sheets WHERE id = ?";

        try (Connection c = Db.conn();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }
}