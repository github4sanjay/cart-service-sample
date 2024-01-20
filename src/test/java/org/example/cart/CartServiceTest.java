package org.example.cart;

import static org.mockito.Mockito.when;

import org.example.product.ProductAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CartServiceTest {

  @Autowired private CartService cartService;
  @MockBean private ProductAPI productAPI;

  @Test
  @DisplayName("test cart when some line added expect correct subtotal, tax and total")
  public void testAddLineItem() {
    when(productAPI.getProduct("fake-name"))
        .thenReturn(ProductAPI.Product.builder().name("fake-name").price(10).build());
    cartService.addLineItem(
        "fake-user-id", Cart.LineItem.builder().name("fake-name").quantity(2).build());

    var cart = cartService.get("fake-user-id");
    Assertions.assertEquals(20, cart.getSubtotal());
    Assertions.assertEquals(2.5, cart.getTax());
    Assertions.assertEquals(22.5, cart.getTotal());
  }

  @Test
  @DisplayName("test cart when no line added expect subtotal, tax and total to be 0")
  public void testCartWhenNoLineItem() {

    var cart = cartService.get("fake-user-id-1");
    Assertions.assertEquals(0, cart.getSubtotal());
    Assertions.assertEquals(0, cart.getTax());
    Assertions.assertEquals(0, cart.getTotal());
  }
}
