package com.gympandro.app;

import com.gympandro.app.db.Db;
import com.sun.net.httpserver.HttpServer;
import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws Exception {
        Properties cfg = new Properties();
        try (var in = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in == null)
                throw new RuntimeException("application.properties not found");
            cfg.load(in);
        }

        // Flyway migration
        Flyway.configure()
                .dataSource(
                        cfg.getProperty("db.url"),
                        cfg.getProperty("db.user"),
                        cfg.getProperty("db.pass"))
                .locations("classpath:db/migration")
                .load()
                .migrate();

        // Init DB
        Db.init(cfg.getProperty("db.url"), cfg.getProperty("db.user"), cfg.getProperty("db.pass"));

        // Start HTTP server
        int port = Integer.parseInt(cfg.getProperty("server.port", "8081"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new HttpRouter());
        server.setExecutor(null);
        server.start();
        System.out.println("GymPandro backend running at http://localhost:" + port);
    }
}