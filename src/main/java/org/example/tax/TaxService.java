package org.example.tax;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TaxService {

  @Value("${product.tax}")
  private float tax;

  public float calculate(float price) {
    return tax / 100 * price;
  }
}
