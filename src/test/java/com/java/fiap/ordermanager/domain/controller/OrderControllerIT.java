package com.java.fiap.ordermanager.domain.controller;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.java.fiap.ordermanager.domain.entity.Orders;
import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor
class OrderControllerIT {

  @LocalServerPort protected int port;

  @BeforeEach
  void setup() {
    RestAssured.port = port;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Nested
  class Find {

    @Test
    void mustReturnByIdOrdersSuccessfully() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .get("/order/{id}", "2f5d9a63-e7f2-4f6e-91b9-3b61a7c829f2")
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("schemas/order.dto.schema.json"));
    }

    @Test
    void mustReturnAllOrdersSuccessfully() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .get("/order")
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("schemas/order.dto.list.schema.json"));
    }

    @Test
    void mustReturnIfCustomerHasOpenOrdersSuccessfully() {
      Long customerId = 1L;

      Boolean response =
          given()
              .contentType(MediaType.APPLICATION_JSON_VALUE)
              .when()
              .get("/order/open_orders/{customerId}", customerId)
              .then()
              .statusCode(HttpStatus.OK.value())
              .extract()
              .as(Boolean.class);

      assertThat(response).isTrue();
    }
  }

  @Nested
  class Delete {

    @Test
    void mustDeleteOrderSuccessfully() {
       given()
          .when()
          .delete("/order/{id}", "2f5d9a63-e7f2-4f6e-91b9-3b61a7c829f3")
          .then()
          .statusCode(HttpStatus.NO_CONTENT.value());
    }
  }
}
