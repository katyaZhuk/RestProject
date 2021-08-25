package tests;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilsAPI.APISpecification;

import static io.restassured.RestAssured.given;

public class BeforeRequest {
    protected static RequestSpecification requestSpecification
            = APISpecification.getRequestSpecification();
    protected Response response;

    protected Response sendRequest(String URL) {
        response = given()
                .spec(requestSpecification)
                .when()
                .get(URL);
        return response;
    }

}
