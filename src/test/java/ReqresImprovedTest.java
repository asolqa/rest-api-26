import io.restassured.RestAssured;
import models.lombok.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;
import static specs.RegistrationEndpointSpecification.registrationEndpoint;
import static specs.RegistrationEndpointSpecification.registrationNotAllowed;
import static specs.UnknownEndpointSpecification.unknownEndpoint;
import static specs.CommonSpecifications.successfulResponse;
import static specs.UsersEndpointSpecification.*;

@SuppressWarnings("SpellCheckingInspection")
@DisplayName("API tests on regres.in")
class ReqresImprovedTest {

    @BeforeAll
    static void setUpConfig() {
        step("Set Base URI", () -> RestAssured.baseURI = "https://reqres.in/api");
    }

    @Test
    @DisplayName("Verify user found by ID has data as expected")
    void userSuccessfullyFoundByIDTest() {
        UsersResponse response =
                step("Make Request", () ->
                        usersEndpoint()
                                .when()
                                .get("/{id}", 3)
                                .then()
                                .spec(successfulResponse())
                                .and().extract().as(UsersResponse.class)
                );


        step("Check Response", () -> {
                    assertEquals(3, response.getData().getId());
                    assertEquals("emma.wong@reqres.in", response.getData().getEmail());
                }
        );
    }

    @Test
    @DisplayName("Verify 404 status code error and empty response when user is not found by ID")
    void userNotFoundByIDTest() {
        UsersResponse response =
                step("Make Request", () ->
                        usersEndpoint()
                                .when()
                                .get("/{id}", 25)
                                .then()
                                .spec(nonexistentUserResponse())
                                .and().extract().as(UsersResponse.class)
                );

        step("Check Response", () -> {
                    assertNull(response.getData());
                    assertNull(response.getSupport());
                }
        );
    }


    @Test
    @DisplayName("Verify \"blue turquoise\" is a color of 2005 by Pantone")
    void pantoneDataTest() {
        UnknownResponse response =
                step("Make Request", () ->
                        unknownEndpoint()
                                .when()
                                .get()
                                .then()
                                .spec(successfulResponse())
                                .and().extract().as(UnknownResponse.class)
                );


        step("Check Response", () -> {
                    assertEquals(6, response.getData().size());

                    ColorInformation lastItem = response.getData().get(5);
                    assertEquals("blue turquoise", lastItem.getName());
                    assertEquals(2005, lastItem.getYear());
                }
        );
    }

    @Test
    @DisplayName("Verify only predefined users can pass the registration")
    void unsuccessfulRegistrationTest() {
        RegistrationData registrationData = new RegistrationData("test.test@reqres.in", "testpswd");

        RegistrationError response =
                step("Make Request", () ->
                        registrationEndpoint()
                                .when()
                                .body(registrationData)
                                .post()
                                .then()
                                .spec(registrationNotAllowed())
                                .and().extract().as(RegistrationError.class)
                );

        step("Check Response",
                () -> assertEquals("Note: Only defined users succeed registration", response.getError())
        );
    }

    @Test
    @DisplayName("Verify user with valid data can be successfully created")
    void userSuccessfulCreationTest() {
        UserCreationRequest postData = new UserCreationRequest("morpheus", "leader");

        UserCreationResponse response =
                step("Make Request", () ->
                        usersEndpoint()
                                .when()
                                .body(postData)
                                .post("/").then()
                                .spec(userCreated())
                                .and().extract().as(UserCreationResponse.class)
                );


        step("Check Response", () -> {
                    assertEquals("morpheus", response.getName());
                    assertEquals("leader", response.getJob());
                    assertNotNull(response.getId());
                    assertNotNull(response.getCreatedAt());
                }
        );
    }

    @Test
    @DisplayName("Verify 400 status code error when patch data is corrupted")
    void userCorruptedDataPatchTest() {
        String response =
                step("Make Request", () ->
                        usersEndpoint()
                                .when()
                                .body("fff")
                                .patch("/{id}", 2)
                                .then()
                                .spec(badRequestResponse()).extract().asString()
                );

        step("Check Response", () -> assertTrue(response.contains("<title>Error</title>")));
    }

    @Test
    @DisplayName("Verify 204 status code when user was deleted")
    void userSuccessfulDeletionTest() {
        String response =
                step("Make Request", () ->
                        usersEndpoint()
                                .when()
                                .delete("/{id}", 2)
                                .then()
                                .spec(userDeletionResponse()).extract().asString()
                );

        step("Check Response", () -> assertTrue(response.isEmpty()));
    }

}
