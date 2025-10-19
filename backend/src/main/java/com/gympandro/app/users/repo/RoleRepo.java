package com.gympandro.app.users.repo;

import com.gympandro.app.db.Db;
import com.gympandro.app.users.model.Role;

import java.sql.*;
import java.util.*;

public class RoleRepo {

    public static List<Role> findAll() throws SQLException {
        String sql = "SELECT id, name, description FROM roles ORDER BY name";
        try (Connection c = Db.conn();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Role> list = new ArrayList<>();
            while (rs.next()) {
                Role r = new Role();
                r.id = (UUID) rs.getObject("id");
                r.name = rs.getString("name");
                r.description = rs.getString("description");
                list.add(r);
            }
            return list;
        }
    }
}