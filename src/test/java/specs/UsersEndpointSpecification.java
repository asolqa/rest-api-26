package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;

public class UsersEndpointSpecification {

    public static RequestSpecification usersEndpoint() {
        return with()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .log().headers()
                .contentType(JSON)
                .basePath("/users");
    }


    public static ResponseSpecification userDeletionResponse() {
        return new ResponseSpecBuilder()
            .expectStatusCode(204)
            .log(STATUS)
            .log(BODY)
            .build();
    }

    public static ResponseSpecification nonexistentUserResponse() {
        return new ResponseSpecBuilder()
                .expectStatusCode(404)
                .log(STATUS)
                .log(BODY)
                .build();
    }

    public static ResponseSpecification badRequestResponse() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .log(STATUS)
                .log(BODY)
                .expectContentType(ContentType.HTML)
                .build();
    }

    public static ResponseSpecification userCreated() {
        return new ResponseSpecBuilder()
                .expectStatusCode(201)
                .log(STATUS)
                .log(BODY)
                .build();
    }

}
