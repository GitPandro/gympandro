package com.gympandro.app.users.repo;

import com.gympandro.app.db.Db;
import com.gympandro.app.users.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepo {

    public static List<User> findAll() throws SQLException {
        String sql = """
            SELECT u.id, u.username, u.name, u.surname, u.email, u.isActive,
                   u.role_id, r.name AS roleName
              FROM users u
              LEFT JOIN roles r ON u.role_id = r.id
              ORDER BY u.name;
            """;

        try (Connection c = Db.conn();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<User> list = new ArrayList<>();
            while (rs.next()) {
                User u = new User();
                u.id = (UUID) rs.getObject("id");
                u.username = rs.getString("username");
                u.name = rs.getString("name");
                u.surname = rs.getString("surname");
                u.email = rs.getString("email");
                u.isActive = rs.getBoolean("isActive");
                u.role_id = (UUID) rs.getObject("role_id");
                u.roleName = rs.getString("roleName");
                list.add(u);
            }
            return list;
        }
    }
}