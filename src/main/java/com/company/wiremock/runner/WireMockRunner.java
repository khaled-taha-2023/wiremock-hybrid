package com.company.wiremock.runner;

import com.company.wiremock.stubs.OrdersApiStubs;
import com.company.wiremock.stubs.ProductsApiStubs;
import com.company.wiremock.stubs.SearchApiStubs;
import com.company.wiremock.stubs.UsersApiStubs;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for the Hybrid WireMock Server.
 *
 * Strategy:
 *  - JSON stubs in resources/mappings/ → loaded automatically (legacy, untouched)
 *  - Java stubs registered programmatically → new stubs, higher priority
 *
 * Priority rule:
 *  - Java stubs use priority = 1  → always win over JSON stubs (priority = 5 default)
 *  - This avoids conflicts without touching old JSON files
 */
public class WireMockRunner {

    private static final Logger log = LoggerFactory.getLogger(WireMockRunner.class);

    private static final int PORT = 8080;

    public static void main(String[] args) throws InterruptedException {

        // ── 1. Configure & start the server ──────────────────────────────────
        WireMockServer server = new WireMockServer(
            WireMockConfiguration.options()
                .port(PORT)
                // Loads all JSON files from src/main/resources/mappings/ automatically
                .usingFilesUnderClasspath("src/main/resources")
                // Verbose request/response logging — set to false in prod
                .notifier(new ConsoleNotifier(true))
        );

        server.start();
        log.info("=================================================");
        log.info("  WireMock Hybrid Server started on port {}", PORT);
        log.info("  JSON stubs  : loaded from resources/mappings/");
        log.info("  Java stubs  : registered below with priority 1");
        log.info("=================================================");

        // ── 2. Register Java-coded stubs (new approach) ───────────────────────
        // Add new stub classes here as your project grows.
        // Each class handles one API domain.
        new UsersApiStubs(server).register();
        new OrdersApiStubs(server).register();
        new ProductsApiStubs(server).register();
        new SearchApiStubs(server).register();

        log.info("All Java stubs registered successfully.");

        // ── 3. Keep the process alive ─────────────────────────────────────────
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down WireMock server...");
            server.stop();
        }));

        Thread.currentThread().join();
    }
}
