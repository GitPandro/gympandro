package com.gympandro.app.clients;

import com.gympandro.app.db.Db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientRepo {
    public static List<Client> findAll() throws Exception {
        try (var con = Db.conn();
                var ps = con.prepareStatement("""
                            select id::text, full_name, email, phone, notes
                            from clients order by created_at desc
                        """)) {
            ResultSet rs = ps.executeQuery();
            List<Client> out = new ArrayList<>();
            while (rs.next()) {
                Client c = new Client();
                c.id = rs.getString(1);
                c.full_name = rs.getString(2);
                c.email = rs.getString(3);
                c.phone = rs.getString(4);
                c.notes = rs.getString(5);
                out.add(c);
            }
            return out;
        }
    }

    public static Client insert(Client in) throws Exception {
        if (in.full_name == null || in.full_name.isBlank()) {
            throw new IllegalArgumentException("full_name is required");
        }
        String id = UUID.randomUUID().toString();
        try (var con = Db.conn();
                var ps = con.prepareStatement("""
                            insert into clients(id, full_name, email, phone, notes)
                            values (?::uuid, ?, ?, ?, ?)
                        """)) {
            ps.setString(1, id);
            ps.setString(2, in.full_name);
            ps.setString(3, in.email);
            ps.setString(4, in.phone);
            ps.setString(5, in.notes);
            ps.executeUpdate();
        }
        Client out = new Client();
        out.id = id;
        out.full_name = in.full_name;
        out.email = in.email;
        out.phone = in.phone;
        out.notes = in.notes;
        return out;
    }
}