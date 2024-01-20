package org.example.cart;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.example.product.ProductAPI;
import org.example.tax.TaxService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

  private final ConcurrentHashMap<String, Cart> CARTS = new ConcurrentHashMap<>();

  private final ProductAPI productAPI;
  private final TaxService taxService;

  public void addLineItem(String userId, Cart.LineItem lineItem) {
    productAPI.getProduct(lineItem.getName());
    var cart = CARTS.get(userId);
    if (cart == null) {
      cart = Cart.builder().items(new ArrayList<>()).build();
      CARTS.put(userId, cart);
    }
    cart.addItem(lineItem);
  }

  public Cart get(String userId) {
    var cart = CARTS.get(userId);
    if (cart == null) {
      cart = Cart.builder().items(new ArrayList<>()).build();
    }

    var subTotal = 0.0F;
    for (var item : cart.getItems()) {
      // get price
      var price = productAPI.getProduct(item.getName()).getPrice();
      subTotal += price * item.getQuantity();
    }
    var tax = taxService.calculate(subTotal);
    var total = tax + subTotal;
    cart.setSubtotal(subTotal);
    cart.setTax(tax);
    cart.setTotal(total);
    return cart;
  }
}
