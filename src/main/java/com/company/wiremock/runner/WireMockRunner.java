package com.company.wiremock.runner;

import com.company.wiremock.stubs.*;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WireMockRunner {
    private static final Logger log = LoggerFactory.getLogger(WireMockRunner.class);
    private static final int PORT = 8080;

    public static void main(String[] args) {

        WireMockServer server = new WireMockServer(
            WireMockConfiguration.options()
                .port(PORT)
                .usingFilesUnderClasspath("src/main/resources")
                .notifier(new ConsoleNotifier(true))
        );
        registerStubs(server);
        server.start();

        log.info("=================================================");
        log.info("  WireMock Hybrid Server started on port {}", PORT);
        log.info("  JSON stubs  : loaded from resources/mappings/");
        log.info("  Java stubs  : registered");
        log.info("=================================================");
    }

    private static void registerStubs(WireMockServer server) {
        Reflections reflections = new Reflections("com.company.wiremock.stubs");

        reflections.getSubTypesOf(BaseStub.class)
                .stream()
                .map(clazz -> {
                    try {
                        return clazz.getConstructor(WireMockServer.class).newInstance(server);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to instantiate stub: " + clazz.getSimpleName(), e);
                    }
                })
                .forEach(BaseStub::register);
    }
}
