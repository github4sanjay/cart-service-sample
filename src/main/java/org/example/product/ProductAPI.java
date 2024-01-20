package org.example.product;

import java.net.URI;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.error.GenericApplicationException;
import org.example.error.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductAPI {

  @Value("${product.api.url}")
  private URI serverUrl;

  private final RestTemplate restTemplate;

  public Product getProduct(String name) {
    var url = String.format("%s/backend-take-home-test-data/%s.json", serverUrl, name);
    try {
      return restTemplate.getForObject(url, Product.class);
    } catch (HttpClientErrorException ex) {
      log.error("Exception in getProduct product name={}", name, ex);
      if (ex.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404))) {
        throw new ProductNotFoundException(name);
      }
      throw new GenericApplicationException();
    } catch (HttpServerErrorException ex) {
      log.error("Exception in getProduct product name={}", name, ex);
      throw new GenericApplicationException();
    }
  }

  @Getter
  @Setter
  @Builder
  public static class Product {
    private String name;
    private float price;
  }
}
