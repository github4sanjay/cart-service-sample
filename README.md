# Cart Service API
This repository contains the code for the Cart Service API, which is built using Java 17 and Spring Boot 3. The primary purpose of this API is to manage shopping cart functionalities and interact with a Product API using RestTemplate. The project also employs ProblemDetail for effective error handling.

Prerequisites
Java 17
Spring Boot 3
Gradle
Getting Started
To run the Cart Service API locally, follow these steps:

1. Clone this repository:
git clone {path}
2. Navigate to the project directory:
cd {folder}
3. Build the project:
./gradlew clean build
4. Run the application:
./gradlew bootRun

The Cart Service API will now be accessible at http://localhost:8080.

# Shopping Cart Endpoints
1. ```POST /api/v1/cart/{userId}/line-item```: Add a product to the cart.
2. ```POST /api/v1/cart/{userId}```: Get a cart state.

# API Documentation
This application uses spring-doc to automate the generation of API documentation. Swagger UI is accessible at http://localhost:8080/swagger-ui/index.html.

# Error Handling
The Cart Service API leverages ProblemDetail for standardized error responses. When an error occurs, the API will return a JSON response following the ProblemDetail format.

Example:

```json
{
  "type": "https://api.products.com/errors/not-found",
  "title": "Product Not Found",
  "status": 404,
  "detail": "Product with name sdfs not found",
  "instance": "/api/v1/cart/sdfsdfs/line-item",
  "errorCode": "PRODUCT_NOT_FOUND",
  "timestamp": "2024-01-20T14:17:54.907339Z"
}
```

# External Dependencies
This project relies on the use of ```RestTemplate``` to communicate with the Product API. Ensure that the Product API is running and its endpoint is correctly configured in the application.properties file.
