package tests;

import io.restassured.response.Response;
import jdk.nashorn.internal.ir.annotations.Ignore;
import model.Category;
import model.Pet;
import org.junit.jupiter.api.*;
import model.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static endpoints.EndPoint.PET;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.equalTo;
import static tests.BeforeRequest.requestSpecification;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestRequest {

    private static File jsonSchema = new File("src/test/resources/json_schema/petSchema.json");

    Tag tag = new Tag(1123545, "cat");
    Category category = new Category(1123545, "cat");
    Pet pet = new Pet(1123545, category, "cat", new ArrayList<>(),
            new ArrayList<>(Collections.singletonList(tag)), "available");

    Pet updatedPet = new Pet(pet.getId(), category, pet.getName(), new ArrayList<>(),
            new ArrayList<>(Collections.singletonList(tag)), "sold");

    @Test
    @Order(2)
    @Ignore
    public void getPetTest() {
        given()
                .spec(requestSpecification)
                .when()
                .get(PET + pet.getId())
                .prettyPeek()
                .then()
                .body("name", equalTo("cat"));

    }

    @Test
    @Order(1)
    public void postPetTest() {

        given()
                .spec(requestSpecification)
                .when()
                .body(pet)
                .post(PET)
                .then()
                .assertThat()
                .body(matchesJsonSchema(jsonSchema));

    }

    @Test
    @Order(3)
    public void putPetTest() {

        given()
                .spec(requestSpecification)
                .when()
                .body(updatedPet)
                .put(PET)
                .prettyPeek()
                .then()
                .body("status", equalTo("sold"));

    }

    @Test
    @Order(4)
    public void deletePetTest() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .body(pet)
                .delete(PET + pet.getId());

        Assertions.assertEquals(200, response.statusCode());

    }

}
