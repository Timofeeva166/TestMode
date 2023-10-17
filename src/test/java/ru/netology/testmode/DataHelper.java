package ru.netology.testmode;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataHelper {
    @Value
    public static class AuthInfo {
        private String login;
        private String password;
        private String status;
    }

    private DataHelper() {
    }

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static void sendRequest(AuthInfo info) {
        given()
                .spec(requestSpec)
                .body(info)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String getLogin() {
        Faker faker = new Faker(new Locale("en"));
        String login = faker.name().username();
        return login;
    }

    public static String getPassword() {
        Faker faker = new Faker(new Locale("en"));
        String password = faker.internet().password();
        return password;
    }

    public static class Registration {

        private Registration() {}

        public static AuthInfo getRegisteredAuthInfo(String status) {
            AuthInfo registeredInfo = new AuthInfo(getLogin(), getPassword(), status);
            sendRequest(registeredInfo);
            return registeredInfo;
        }

        public static AuthInfo getAuthInfo(String status) {
            AuthInfo info = new AuthInfo(getLogin(), getPassword(), status);
            return info;
        }
    }
}
