package com.company.wiremock.stubs;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class UsersApiStubs extends BaseStub {

    public UsersApiStubs(WireMockServer server) {
        super(server);
    }

    @Override
    protected void registerStubs() {
        getUser();
        getUserNotFound();
        createUser();
        updateUser();
        deleteUser();
    }

    private void getUser() {
        server.stubFor(
            get(urlPathMatching("/api/users/new1"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                          "id": 1,
                          "name": "Jane Doe",
                          "email": "jane.doe@example.com",
                          "status": "active",
                          "role": "admin"
                        }
                        """))
        );
    }

    private void getUserNotFound() {
        server.stubFor(
            get(urlEqualTo("/api/users/999"))
                .willReturn(aResponse()
                    .withStatus(404)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                          "error": "User not found",
                          "code": "USER_404"
                        }
                        """))
        );
    }

    private void createUser() {
        server.stubFor(
            post(urlEqualTo("/api/users"))
                .withHeader("Content-Type", containing("application/json"))
                .withRequestBody(matchingJsonPath("$.name"))
                .withRequestBody(matchingJsonPath("$.email"))
                .willReturn(aResponse()
                    .withStatus(201)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                          "id": 100,
                          "message": "User created successfully"
                        }
                        """))
        );
    }

    private void updateUser() {
        server.stubFor(
            put(urlPathMatching("/api/users/new1"))
                .withHeader("Content-Type", containing("application/json"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                          "id": 1,
                          "message": "User updated successfully"
                        }
                        """))
        );
    }

    private void deleteUser() {
        server.stubFor(
            delete(urlPathMatching("/api/users/new1"))
                .willReturn(aResponse()
                    .withStatus(204))
        );
    }
}
