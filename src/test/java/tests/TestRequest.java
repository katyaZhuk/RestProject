package tests;

import io.restassured.specification.RequestSpecification;
import model.Category;
import model.Pet;
import org.junit.jupiter.api.*;
import model.Tag;
import utilsAPI.APISpecification;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static endpoints.EndPoint.PET;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestRequest {

    private final static RequestSpecification REQUEST_SPECIFICATION
            = APISpecification.getRequestSpecification();
    private final static File JSON_SCHEMA = new File("src/test/resources/json_schema/petSchema.json");

    Tag tag = new Tag(1123545, "cat");
    Category category = new Category(1123545, "cat");
    Pet pet = new Pet(1123545, category, "cat", new ArrayList<>(),
            new ArrayList<>(Collections.singletonList(tag)), "available");

    Pet updatedPet = new Pet(pet.getId(), category, pet.getName(), new ArrayList<>(),
            new ArrayList<>(Collections.singletonList(tag)), "sold");

    @Test
    @Order(2)
    @Disabled
    public void getPetTest() {
        given()
                .spec(REQUEST_SPECIFICATION)
                .when()
                .get(PET + pet.getId())
                .prettyPeek()
                .then()
                .assertThat()
                .body("name", equalTo("cat"));

    }

    @Test
    @Order(1)
    public void postPetTest() {

        given()
                .spec(REQUEST_SPECIFICATION)
                .when()
                .body(pet)
                .post(PET)
                .then()
                .assertThat()
                .body(matchesJsonSchema(JSON_SCHEMA));

    }

    @Test
    @Order(3)
    public void putPetTest() {

        given()
                .spec(REQUEST_SPECIFICATION)
                .when()
                .body(updatedPet)
                .put(PET)
                .prettyPeek()
                .then()
                .assertThat()
                .body("status", equalTo("sold"));

    }

    @Test
    @Order(4)
    public void deletePetTest() {
        given()
                .spec(REQUEST_SPECIFICATION)
                .when()
                .body(pet)
                .delete(PET + pet.getId())
                .then()
                .assertThat()
                .statusCode(200);
    }

}
