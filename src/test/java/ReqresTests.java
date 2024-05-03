
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static io.restassured.http.ContentType.JSON;


@DisplayName("Training API tests on regres.com")
public class ReqresTests {

    @Test
    @DisplayName("Verify user found by ID has data as expected")
    void userSuccessfullyFoundByIDTest() {
        given()
                .log().uri()

        .when()
                .get("https://reqres.in/api/users/{id}", 3)

        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", equalTo(3))
                .body("data.email", equalTo("emma.wong@reqres.in"));
    }

    @Test
    @DisplayName("Verify 404 status code error and empty response when user is not found by ID")
    void userNotFoundByIDTest() {
        given()
                .log().uri()

        .when()
                .get("https://reqres.in/api/users/{id}", 25)

        .then()
                .log().status()
                .log().body()
                .statusCode(404)
                .body(equalTo("{}"));
    }

    @Test
    @DisplayName("Verify \"blue turquoise\" is a color of 2005 by Pantone")
    void pantoneDataTest() {
        given()
                .log().uri()

        .when()
                .get("https://reqres.in/api/unknown")

        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data[5].name", equalTo("blue turquoise"))
                .body("data[5].year", equalTo(2005));
    }

    String regDataTemplate = """
            {
              "email":"%s",
              "password":"%s"
            }
            """;

    @Test
    @DisplayName("Verify only predefined users can pass the registration")
    void unsuccessfulRegistrationTest() {
        String regData = String.format(regDataTemplate,"test.test@reqres.in","testpswd");

        given()
                .body(regData)
                .contentType(JSON)
                .log().uri()

        .when()
                .post("https://reqres.in/api/register")

        .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Note: Only defined users succeed registration"));
    }

    String postDataTemplate = """
            {
              "name":"%s",
              "job":"%s"
            }
            """;

    @Test
    @DisplayName("Verify user with valid data can be successfully created")
    void userSuccessfulCreationTest() {
        String postData = String.format(postDataTemplate,"morpheus","leader");

        given()
                .body(postData)
                .contentType(JSON)
                .log().uri()

        .when()
                .post("https://reqres.in/api/users")

        .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"))
                .body("id", not(empty()));
    }

    @Test
    @DisplayName("Verify 400 status code error when patch data is corrupted")
    void userCorruptedDataPatchTest() {
        String patchData = "fff";

        given()
                .body(patchData)
                .contentType(JSON)
                .log().uri()

        .when()
                .patch("https://reqres.in/api/users/{id}", 2)

        .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }

    @Test
    @DisplayName("Verify 204 status code when user was deleted")
    void userSuccessfulDeletionTest() {

        given()
                .contentType(JSON)
                .log().uri()

        .when()
                .delete("https://reqres.in/api/users/{id}", 2)

        .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }

}
