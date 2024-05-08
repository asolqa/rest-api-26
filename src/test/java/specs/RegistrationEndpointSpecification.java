package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;

public class RegistrationEndpointSpecification {

    public static RequestSpecification registrationEndpoint() {
       return with()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .log().headers()
                .basePath("/register")
                .contentType(JSON);
    }

    public static ResponseSpecification registrationNotAllowed() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .log(STATUS)
                .log(BODY)
                .build();
    }
}
