package org.example.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
  private final CartService cartService;

  @ResponseBody
  @PostMapping("/{userId}/line-item")
  public ResponseEntity<Void> addLineItem(
      @PathVariable String userId, @RequestBody @Valid AddLineItemRequest request) {
    var lineItem = request.getLineItem();
    log.info(
        "Add line item to cart name={}, quantity={}", lineItem.getName(), lineItem.getQuantity());
    cartService.addLineItem(
        userId,
        Cart.LineItem.builder().name(lineItem.getName()).quantity(lineItem.getQuantity()).build());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @ResponseBody
  @GetMapping("/{userId}")
  public CartResponse get(@PathVariable String userId) {
    var cart = cartService.get(userId);
    return CartResponse.builder()
        .items(
            cart.getItems().stream()
                .map(
                    lineItem ->
                        LineItem.builder()
                            .name(lineItem.getName())
                            .quantity(lineItem.getQuantity())
                            .build())
                .collect(Collectors.toList()))
        .subtotal(DECIMAL_FORMAT.format(cart.getSubtotal()))
        .tax(DECIMAL_FORMAT.format(cart.getTax()))
        .total(DECIMAL_FORMAT.format(cart.getTotal()))
        .build();
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AddLineItemRequest {

    @Valid @NotNull private LineItem lineItem;
  }

  @Getter
  @Setter
  @Builder
  public static class CartResponse {
    private List<LineItem> items;
    private String subtotal;
    private String tax;
    private String total;
  }

  @Getter
  @Setter
  @Builder
  public static class LineItem {
    @NotBlank(message = "name cannot be blank")
    private String name;

    @Min(value = 0, message = "quantity cannot be negative")
    private float quantity;
  }
}
