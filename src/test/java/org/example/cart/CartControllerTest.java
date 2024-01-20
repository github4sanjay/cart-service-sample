package org.example.cart;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.MediaType;
import org.mockserver.springtest.MockServerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@MockServerTest("product.api.url=http://localhost:${mockServerPort}")
class CartControllerTest {

  private static MockServerClient mockServerClient;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName("test cart when line items are added then expect cart with correct data")
  void testCartWhenLineItemsAddedExpectCartWithCorrectData() throws Exception {
    mockServerClient
        .when(request().withMethod("GET").withPath("/backend-take-home-test-data/cornflakes.json"))
        .respond(
            response()
                .withStatusCode(200)
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(
                    """
                    {
                     "title": "Corn Flakes",
                     "price": 2.52
                     }
                    """));

    mockServerClient
        .when(request().withMethod("GET").withPath("/backend-take-home-test-data/weetabix.json"))
        .respond(
            response()
                .withStatusCode(200)
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(
                    """
                    {
                     "title": "Weetabix",
                     "price": 9.98
                     }
                    """));

    var lineItemRequest1 =
        CartController.AddLineItemRequest.builder()
            .lineItem(CartController.LineItem.builder().quantity(1).name("cornflakes").build())
            .build();

    this.mockMvc
        .perform(
            post("/api/v1/cart/44d6eac3-f83a-4f15-8af9-a2c4d55d1f2a/line-item")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(lineItemRequest1)))
        .andExpect(status().isNoContent());

    var lineItemRequest2 =
        CartController.AddLineItemRequest.builder()
            .lineItem(CartController.LineItem.builder().quantity(1).name("cornflakes").build())
            .build();

    this.mockMvc
        .perform(
            post("/api/v1/cart/44d6eac3-f83a-4f15-8af9-a2c4d55d1f2a/line-item")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(lineItemRequest2)))
        .andExpect(status().isNoContent());

    var lineItemRequest3 =
        CartController.AddLineItemRequest.builder()
            .lineItem(CartController.LineItem.builder().quantity(1).name("weetabix").build())
            .build();

    this.mockMvc
        .perform(
            post("/api/v1/cart/44d6eac3-f83a-4f15-8af9-a2c4d55d1f2a/line-item")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(lineItemRequest3)))
        .andExpect(status().isNoContent());

    var result =
        this.mockMvc
            .perform(
                get("/api/v1/cart/44d6eac3-f83a-4f15-8af9-a2c4d55d1f2a")
                    .contentType("application/json"))
            .andExpect(status().isOk())
            .andReturn();

    var content = result.getResponse().getContentAsString();
    var cart = objectMapper.readValue(content, CartController.CartResponse.class);

    Assertions.assertEquals(3, cart.getItems().size());
    Assertions.assertEquals("15.02", cart.getSubtotal());
    Assertions.assertEquals("1.88", cart.getTax());
    Assertions.assertEquals("16.90", cart.getTotal());
  }
}
