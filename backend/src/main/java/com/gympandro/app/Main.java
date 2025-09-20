package com.gympandro.app;

import com.gympandro.app.db.Db;
import com.sun.net.httpserver.HttpServer;
import org.flywaydb.core.Flyway;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws Exception {
        Properties cfg = new Properties();
        try (var in = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in == null)
                throw new RuntimeException("application.properties not found");
            cfg.load(in);
        }
        int port = Integer.parseInt(cfg.getProperty("server.port", "8081"));

        // Init DB
        Db.init(
                cfg.getProperty("db.url"),
                cfg.getProperty("db.user"),
                cfg.getProperty("db.pass"));

        Flyway.configure()
                .dataSource(
                        cfg.getProperty("db.url"),
                        cfg.getProperty("db.user"),
                        cfg.getProperty("db.pass"))
                .locations("classpath:db/migration")
                .load()
                .migrate();

        // HTTP server (JDK)
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Static (Bootstrap UI) → serve / and /plans
        server.createContext("/", exchange -> {
            try {
                Path index = Path.of("../public/index.html").normalize().toAbsolutePath();
                byte[] bytes = Files.readAllBytes(index);
                exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
                exchange.sendResponseHeaders(200, bytes.length);
                exchange.getResponseBody().write(bytes);
            } catch (IOException e) {
                byte[] msg = "Not Found".getBytes();
                exchange.sendResponseHeaders(404, msg.length);
                exchange.getResponseBody().write(msg);
            } finally {
                exchange.close();
            }
        });

        // Simple router for /api/*
        server.createContext("/api", new HttpRouter());

        server.setExecutor(null);
        server.start();
        System.out.println("GymPandro running on http://localhost:" + port);
    }
}