package org.example.cart;

import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
public class Cart {
  private List<LineItem> items;
  private float subtotal;
  private float tax;
  private float total;

  public void addItem(LineItem lineItem) {
    items.add(lineItem);
  }

  @Getter
  @Setter
  @Builder
  public static class LineItem {
    private String name;
    private float quantity;
  }
}
