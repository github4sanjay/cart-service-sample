package org.example.tax;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaxServiceTest {

  @Autowired private TaxService taxService;

  @Test
  @DisplayName("test tax calculator when price is 100 expect tax to be 12.5")
  public void testTaxCalculation() {
    var tax = taxService.calculate(100);
    Assertions.assertEquals(12.5, tax);
  }
}
