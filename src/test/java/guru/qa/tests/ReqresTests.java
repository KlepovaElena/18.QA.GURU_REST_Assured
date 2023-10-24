package guru.qa.tests;

import guru.qa.models.create.CreateUserModel;
import guru.qa.models.create.CreateUserResponseModel;
import guru.qa.models.register.RegisterUserModel;
import guru.qa.models.register.RegisterUserResponseModel;
import guru.qa.models.update.UpdateUserModel;
import guru.qa.models.update.UpdateUserResponseModel;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.specs.CreateSpec.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ReqresTests {

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @DisplayName("User can successfully create a new user")
    @Test
    void successfulCreateUser() {
        CreateUserModel authData = new CreateUserModel();
        authData.setName("Elena");
        authData.setJob("QA");

        CreateUserResponseModel response = step("Create user", () ->
                given(requestSpec)
                        .body(authData)
                        .when()
                        .post("/users")
                        .then()
                        .spec(createUserResponse201Spec)
                        .extract().as(CreateUserResponseModel.class));

        step("Verify response", () -> {
            assertThat(response.getName()).isEqualTo("Elena");
            assertThat(response.getJob()).isEqualTo("QA");
            assertThat(response.getId()).isNotNull();
            assertThat(response.getCreatedAt()).isNotNull();
        });
    }

    @DisplayName("User can get successfully registered")
    @Test
    void successfulRegisterUser() {
        RegisterUserModel authData = new RegisterUserModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("pistol");

        RegisterUserResponseModel response = step("Register user", () ->
                given(requestSpec)
                        .body(authData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(userResponse200Spec)
                        .extract().as(RegisterUserResponseModel.class));

        step("Verify response", () -> {
            assertThat(response.getId()).isEqualTo(4);
            assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
        });
    }

    @DisplayName("User gets an error when registering with missing password")
    @Test
    void missingPasswordRegisterUser() {

        RegisterUserModel authData = new RegisterUserModel();
        authData.setEmail("eve.holt@reqres.in");
        RegisterUserResponseModel response = step("Register user with missing password", () ->
                given(requestSpec)
                        .body(authData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(missingPasswordOrEmailResponse400Spec)
                        .extract().as(RegisterUserResponseModel.class));

        step("Verify response", () -> {
            assertThat(response.getError()).isEqualTo("Missing password");
        });
    }

    @DisplayName("User gets an error when registering with missing email")
    @Test
    void missingEmailRegisterUser() {

        RegisterUserModel authData = new RegisterUserModel();
        authData.setPassword("pistol");
        RegisterUserResponseModel response = step("Register user with missing email", () ->
                given(requestSpec)
                        .body(authData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(missingPasswordOrEmailResponse400Spec)
                        .extract().as(RegisterUserResponseModel.class));
        step("Verify response", () -> {
            assertThat(response.getError()).isEqualTo("Missing email or username");
        });
    }

    @Test
    @DisplayName("User gets an error when a user is not found")
    void userNotFound() {
        step("Get user that does not exist", () ->
        given(requestSpec)
                .when()
                .get("/users/23")
                .then()
                .spec(userNotFoundResponse404Spec));
    }

    @Test
    @DisplayName("User can successfully update an existing user")
    void successfulUpdateUser() {

        UpdateUserModel updateUserBody = new UpdateUserModel();
        updateUserBody.setName("Elena");
        updateUserBody.setJob("QA");
        UpdateUserResponseModel response = step("Update user", () ->
                given(requestSpec)
                        .body(updateUserBody)
                        .when()
                        .patch("/users/2")
                        .then()
                        .spec(userResponse200Spec)
                        .extract().as(UpdateUserResponseModel.class));
        step("Verify response", () -> {
            assertThat(response.getName()).isEqualTo("Elena");
            assertThat(response.getJob()).isEqualTo("QA");
            assertThat(response.getUpdatedAt()).isNotNull();
        });
    }
}