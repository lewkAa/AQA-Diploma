package ru.netology.data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class ApiHelper {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(8080)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL).build();

    public static void successSend(DataHelper.Card card) {

        given()
                .spec(requestSpec)
                .body(card)
                .when()
                .post("/api/v1/pay")
                .then()
                .statusCode(200)
                .body("status", equalTo("APPROVED"));
    }

    public static void failSend(DataHelper.Card card) {

        given()
                .spec(requestSpec)
                .body(card)
                .when()
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
                .body("status", equalTo("DECLINED"));
    }

    public static void sendCard(DataHelper.Card card) {

        given()
                .spec(requestSpec)
                .body(card)
                .when()
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
                .body("error", equalTo("Bad Request"));
    }

}
