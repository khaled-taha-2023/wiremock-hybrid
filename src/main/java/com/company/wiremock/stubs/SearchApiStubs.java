package com.company.wiremock.stubs;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class SearchApiStubs extends BaseStub {

    public SearchApiStubs(WireMockServer server) {
        super(server);
    }

    @Override
    protected void registerStubs() {
        searchByTypeProduct();
        searchByeNewProductType();
        searchByTypeService();
        searchByCategoryType();
        updateItemStatusActive();
        updateItemStatusInactive();
    }

    private void searchByCategoryType() {
        server.stubFor(
                get(urlEqualTo("/api/search?category=electronics&sort=price"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                        {
                          "type": "product",
                          "category": "electronics",
                          "sort": "price",
                          "results": [
                            { "id": "P001", "name": "Laptop",  "price": 999.99 },
                            { "id": "P002", "name": "Monitor", "price": 299.99 },
                            { "id": "P003", "name": "Monitor", "price": 299.99 },
                            { "id": "P004", "name": "Monitor", "price": 299.99 }
                          ],
                          "total": 4
                        }
                        """))
        );
    }

    private void searchByTypeProduct() {
        server.stubFor(
                get(urlPathEqualTo("/api/search"))
                        .withQueryParam("category", equalTo("electronics"))
                        .withQueryParam("sort",     equalTo("price"))
                        .withQueryParam("type",     equalTo("product"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                    {
                      "type": "product",
                      "category": "electronics",
                      "sort": "price",
                      "results": [
                        { "id": "P001", "name": "Laptop",  "price": 999.99 },
                        { "id": "P002", "name": "Monitor", "price": 299.99 }
                      ],
                      "total": 2
                    }
                    """))
        );
    }

    private void searchByeNewProductType() {
        server.stubFor(
                get(urlEqualTo("/api/search?category=electronics&sort=price&type=newProduct"))    // ← differs here
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                        {
                          "type": "newProduct",
                          "category": "electronics",
                          "sort": "price",
                          "results": [
                            { "id": "P001", "name": "Laptop",  "price": 999.99 },
                            { "id": "P002", "name": "Monitor", "price": 299.99 }
                          ],
                          "total": 2
                        }
                        """))
        );
    }

    private void searchByTypeService() {
        server.stubFor(
                get(urlEqualTo("/api/search?category=electronics&sort=price&type=service"))      // ← differs here
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                        {
                          "type": "service",
                          "category": "electronics",
                          "sort": "price",
                          "results": [
                            { "id": "S001", "name": "Repair Service",   "price": 49.99 },
                            { "id": "S002", "name": "Warranty Service", "price": 29.99 },
                            { "id": "S003", "name": "Warranty Service", "price": 29.99 }
                          ],
                          "total": 3
                        }
                        """))
        );
    }

    private void updateItemStatusActive() {
        String body = """
                {
                  "name": "item1",
                  "category": "cat1",
                  "status": "active"
                }
                """;
        server.stubFor(
                put(urlPathMatching("/api/items/\\d+"))
                        .withHeader("Content-Type", containing("application/json"))
                        .withRequestBody(equalToJson(body, true, true))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                        {
                          "id": 1,
                          "status": "active",
                          "message": "Item is now active",
                          "updatedAt": "2026-01-01T00:00:00Z"
                        }
                        """))
        );
    }

    private void updateItemStatusInactive() {
        String body = """
                {
                  "name": "item1",
                  "category": "cat1",
                  "status": "inactive"
                }
                """;
        server.stubFor(
                put(urlPathMatching("/api/items/\\d+"))
                        .withHeader("Content-Type", containing("application/json"))
                        .withRequestBody(equalToJson(body, true, true))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                        {
                          "id": 1,
                          "status": "inactive",
                          "message": "Item is now inactive",
                          "updatedAt": "2026-01-01T00:00:00Z"
                        }
                        """))
        );
    }
}
