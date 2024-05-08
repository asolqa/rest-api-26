package specs;

import io.restassured.specification.RequestSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;

public class UnknownEndpointSpecification {

    public static RequestSpecification unknownEndpoint() {
        return with()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .log().headers()
                .basePath("unknown")
                .contentType(JSON);
    }
}
