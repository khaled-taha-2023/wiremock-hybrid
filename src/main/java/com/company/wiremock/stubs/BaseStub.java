package com.company.wiremock.stubs;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseStub {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final WireMockServer server;

    protected BaseStub(WireMockServer server) {
        this.server = server;
    }

    public final void register() {
        log.info("Registering Java stubs: {}", getClass().getSimpleName());
        registerStubs();
        log.info("Done registering: {}", getClass().getSimpleName());
    }
    protected abstract void registerStubs();
}
