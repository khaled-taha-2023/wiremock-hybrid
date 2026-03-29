package com.company.wiremock.stubs;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;


public class OrdersApiStubs extends BaseStub {

    public OrdersApiStubs(WireMockServer server) {
        super(server);
    }

    @Override
    protected void registerStubs() {
        getOrder();
        getOrdersByStatus();
        placeOrder();
        cancelOrder();
    }

    private void getOrder() {
        server.stubFor(
            get(urlPathMatching("/api/orders/new1"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                          "orderId": "ORD-001",
                          "userId": 1,
                          "status": "CONFIRMED",
                          "items": [
                            { "productId": "P001", "quantity": 2, "price": 49.99 },
                            { "productId": "P002", "quantity": 1, "price": 19.99 }
                          ],
                          "total": 119.97
                        }
                        """))
        );
    }

    private void getOrdersByStatus() {
        server.stubFor(
            get(urlPathEqualTo("/api/orders"))
                .withQueryParam("status", equalTo("PENDING"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                          "orders": [
                            { "orderId": "ORD-002", "status": "PENDING", "total": 75.00 },
                            { "orderId": "ORD-003", "status": "PENDING", "total": 120.50 }
                          ],
                          "count": 2
                        }
                        """))
        );
    }

    private void placeOrder() {
        server.stubFor(
            post(urlEqualTo("/api/orders"))
                .withHeader("Content-Type", containing("application/json"))
                .withRequestBody(matchingJsonPath("$.userId"))
                .withRequestBody(matchingJsonPath("$.items"))
                .willReturn(aResponse()
                    .withStatus(201)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                          "orderId": "ORD-999",
                          "status": "CONFIRMED",
                          "message": "Order placed successfully"
                        }
                        """))
        );
    }

    private void cancelOrder() {
        server.stubFor(
            delete(urlPathMatching("/api/orders/new1"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                          "message": "Order cancelled successfully"
                        }
                        """))
        );
    }
}
