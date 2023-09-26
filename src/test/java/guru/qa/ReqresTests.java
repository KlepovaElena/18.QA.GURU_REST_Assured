package guru.qa;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresTests {

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @DisplayName("User can successfully create a new user")
    @Test
    void SuccessfulCreateUser() {
        String authData = "{ \"name\": \"Elena\", \"job\": \"QA\"}";
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(authData)
                .when()
                .post(basePath)
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("Elena"))
                .body("job", is("QA"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }

    @DisplayName("User can get successfully registered")
    @Test
    void SuccessfuRegisterUser() {
        String authData = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(authData)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @DisplayName("User gets an error when registering with missing password")
    @Test
    void MissingPasswordRegisterUser() {
        String authData = "{ \"email\": \"sydney@fife\"}";
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(authData)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @DisplayName("User gets an error when registering with missing email")
    @Test
    void MissingEmailRegisterUser() {
        String authData = "{ \"password\": \"pistol\"}";
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(authData)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    @DisplayName("User gets an error when a user is not found")
    void UserNotFound() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get("/users/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    @Test
    @DisplayName("User can successfully update an existing user")
    void SuccessfuUpdateUser() {
        String updateUserBody = "{\"name\": \"Elena\", \"job\": \"QA\"}";
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(updateUserBody)
                .when()
                .patch("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("Elena"))
                .body("job", is("QA"))
                .body("updatedAt", notNullValue());
    }
}
