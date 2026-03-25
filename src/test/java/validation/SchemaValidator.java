package validation;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import java.io.File;

public class SchemaValidator {

	public static void validate(Response response) {

		String path = System.getProperty("user.dir") + "/src/test/java/resources/pricingSchema.json";
		response
		.then()
		.assertThat()
		.body(JsonSchemaValidator.matchesJsonSchema(new File(path)));
	}
}