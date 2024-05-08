package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;

public class CommonSpecifications {

    public static ResponseSpecification successfulResponse() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(STATUS)
                .log(BODY)
                .build();
    }
}
