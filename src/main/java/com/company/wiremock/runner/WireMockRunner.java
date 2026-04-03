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

public class WireMockRunner {

    private static final Logger log = LoggerFactory.getLogger(WireMockRunner.class);

    private static final int PORT = 8080;

    public static void main(String[] args) throws InterruptedException {

        WireMockServer server = new WireMockServer(
            WireMockConfiguration.options()
                .port(PORT)
                .usingFilesUnderClasspath("src/main/resources")
                .notifier(new ConsoleNotifier(true))
        );

        server.start();
        log.info("=================================================");
        log.info("  WireMock Hybrid Server started on port {}", PORT);
        log.info("  JSON stubs  : loaded from resources/mappings/");
        log.info("  Java stubs  : registered below with priority 1");
        log.info("=================================================");

        new UsersApiStubs(server).register();
        new OrdersApiStubs(server).register();
        new ProductsApiStubs(server).register();
        new SearchApiStubs(server).register();

        log.info("All Java stubs registered successfully.");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down WireMock server...");
            server.stop();
        }));

        Thread.currentThread().join();
    }
}
