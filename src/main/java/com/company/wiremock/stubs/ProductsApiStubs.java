package com.company.wiremock.stubs;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ProductsApiStubs extends BaseStub {

    public ProductsApiStubs(WireMockServer server) {
        super(server);
    }

    @Override
    protected void registerStubs() {
        getProduct();
        getProductOutOfStock();
        searchProducts();
        getProductInternalServerError();
    }

    private void getProduct() {
        server.stubFor(
            get(urlPathMatching("/api/products/new1"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                          "productId": "P001",
                          "name": "Wireless Headphones",
                          "price": 49.99,
                          "stock": 150,
                          "category": "Electronics"
                        }
                        """))
        );
    }

    private void getProductOutOfStock() {
        server.stubFor(
            get(urlEqualTo("/api/products/OUT_OF_STOCK"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                          "productId": "OUT_OF_STOCK",
                          "name": "Sold Out Item",
                          "price": 29.99,
                          "stock": 0,
                          "category": "Electronics"
                        }
                        """))
        );
    }

    private void searchProducts() {
        server.stubFor(
            get(urlPathEqualTo("/api/products"))
                .withQueryParam("search", matching(".+"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                          "results": [
                            { "productId": "P001", "name": "Wireless Headphones", "price": 49.99 },
                            { "productId": "P002", "name": "Wired Headphones",    "price": 19.99 }
                          ],
                          "total": 2
                        }
                        """))
        );
    }

    private void getProductInternalServerError() {
        server.stubFor(
            get(urlEqualTo("/api/products/ERROR"))
                .willReturn(aResponse()
                    .withStatus(500)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                          "error": "Internal Server Error",
                          "message": "Unexpected error occurred"
                        }
                        """))
        );
    }
}
