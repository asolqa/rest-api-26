import io.restassured.RestAssured;
import models.lombok.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.Specifications.requestSpec;
import static specs.Specifications.responseWithBadRequest400Code;
import static specs.Specifications.responseWithCreated201Code;
import static specs.Specifications.responseWithNoContent204Code;
import static specs.Specifications.responseWithNotFound404Code;
import static specs.Specifications.successfulResponse200Code;

@SuppressWarnings("SpellCheckingInspection")
@DisplayName("API tests on regres.in")
class ReqresImprovedTest {

    @BeforeAll
    static void setUpConfig() {
        step("Set Base URI", () -> RestAssured.baseURI = "https://reqres.in");
        step("Set Base Path", () -> RestAssured.basePath = "/api");
    }

    @Test
    @DisplayName("Verify user found by ID has data as expected")
    void userSuccessfullyFoundByIDTest() {
        UsersResponse response =
                step("Make Request", () ->
                        given(requestSpec())
                                .when()
                                .get("/users/{id}", 3)
                                .then()
                                .spec(successfulResponse200Code())
                                .and().extract().as(UsersResponse.class)
                );


        step("Check Response", () -> {
                    assertThat(response.getData().getId()).isEqualTo(3);
                    assertThat(response.getData().getEmail()).isEqualTo("emma.wong@reqres.in");
                }
        );
    }

    @Test
    @DisplayName("Verify 404 status code error and empty response when user is not found by ID")
    void userNotFoundByIDTest() {
        UsersResponse response =
                step("Make Request", () ->
                        given(requestSpec())
                                .when()
                                .get("/users/{id}", 25)
                                .then()
                                .spec(responseWithNotFound404Code())
                                .and().extract().as(UsersResponse.class)
                );

        step("Check Response", () -> {
                    assertThat(response.getData()).isNull();
                    assertThat(response.getSupport()).isNull();
                }
        );
    }


    @Test
    @DisplayName("Verify \"blue turquoise\" is a color of 2005 by Pantone")
    void pantoneDataTest() {
        UnknownResponse response =
                step("Make Request", () ->
                        given(requestSpec())
                                .when()
                                .get("/uknown")
                                .then()
                                .spec(successfulResponse200Code())
                                .and().extract().as(UnknownResponse.class)
                );


        step("Check Response", () -> {
                    assertThat(response.getData()).hasSize(6);

                    ColorInformation lastItem = response.getData().get(5);
                    assertThat(lastItem.getName()).isEqualTo("blue turquoise");
                    assertThat(lastItem.getYear()).isEqualTo(2005);
                }
        );
    }

    @Test
    @DisplayName("Verify only predefined users can pass the registration")
    void unsuccessfulRegistrationTest() {
        RegistrationData registrationData = new RegistrationData("test.test@reqres.in", "testpswd");

        RegistrationError response =
                step("Make Request", () ->
                        given(requestSpec())
                                .when()
                                .body(registrationData)
                                .post("/register")
                                .then()
                                .spec(responseWithBadRequest400Code())
                                .and().extract().as(RegistrationError.class)
                );

        step("Check Response",
                () -> assertThat(response.getError()).contains("Note: Only defined users succeed registration")
        );
    }

    @Test
    @DisplayName("Verify user with valid data can be successfully created")
    void userSuccessfulCreationTest() {
        UserCreationRequest postData = new UserCreationRequest("morpheus", "leader");

        UserCreationResponse response =
                step("Make Request", () ->
                        given(requestSpec())
                                .when()
                                .body(postData)
                                .post("/users")
                                .then()
                                .spec(responseWithCreated201Code())
                                .and().extract().as(UserCreationResponse.class)
                );


        step("Check Response", () -> {
                    assertThat(response.getName()).isEqualTo("morpheus");
                    assertThat(response.getJob()).isEqualTo("leader");
                    assertThat(response.getId()).isNotNull();
                    assertThat(response.getCreatedAt()).isNotNull();
                }
        );
    }

    @Test
    @DisplayName("Verify 400 status code error when patch data is corrupted")
    void userCorruptedDataPatchTest() {
        String response =
                step("Make Request", () ->
                        given(requestSpec())
                                .when()
                                .body("fff")
                                .patch("/{id}", 2)
                                .then()
                                .spec(responseWithBadRequest400Code()).extract().asString()
                );

        step("Check Response", () -> assertThat(response).contains("<title>Error</title>"));
    }

    @Test
    @DisplayName("Verify 204 status code when user was deleted")
    void userSuccessfulDeletionTest() {
        String response =
                step("Make Request", () ->
                        given(requestSpec())
                                .when()
                                .delete("/{id}", 2)
                                .then()
                                .spec(responseWithNoContent204Code()).extract().asString()
                );

        step("Check Response", () -> assertThat(response).isEmpty());
    }

}
