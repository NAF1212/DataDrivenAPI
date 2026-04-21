package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.TokenManager;
import config.ConfigManager;

import java.util.Map;

public class PricingAPI {

	public static Response calculate(Map<String, String> data) {

	    boolean xDock = Boolean.parseBoolean(data.get("xDock").toLowerCase());
	    boolean stackable = Boolean.parseBoolean(data.get("stackable").toLowerCase());
	    String CommodityNames = String.valueOf(data.get("CommodityNames")).toLowerCase();
	    int custId = (int) Double.parseDouble(data.get("custAccountId"));
	    int skidCount = (int) Double.parseDouble(data.get("skidCount"));
	    int pieces = (int) Double.parseDouble(data.get("pieces"));
	    int declaredWeight = (int) Double.parseDouble(data.get("declaredWeight"));
	    int length = (int) Double.parseDouble(data.get("length"));
	    int width = (int) Double.parseDouble(data.get("width"));
	    int height = (int) Double.parseDouble(data.get("height"));
	    
	    //"CommodityNames": ["%s"],

	    String payload = """
	        {
	          "custAccountId": %d,
	          "originCity": "%s",
	          "destinationCity": "%s",
	          "originProvince": "%s",
	          "destinationProvince": "%s",
	          "xDock": %b,
	          "CommodityNames": ["%s"],
	          "cuftItemDetails": [
	            {
	              "skidCount": %d,
	              "pieces": %d,
	              "declaredWeight": %d,
	              "length": %d,
	              "width": %d,
	              "height": %d,
	              "stackable": %b,
	              "weightUoM": "%s",
	              "dimUoM": "%s"
	            }
	          ],
	          "accessorials": [
	            {
	              "accessorialCode": "%s",
	              "parameterValue": "%s"
	            },
	            {
	              "accessorialCode": "%s",
	              "parameterValue": "%s"
	            },
	            {
	              "accessorialCode": "%s",
	              "parameterValue": "%s"
	            }
	          ]
	        }
	        """.formatted(
	            custId,
	            data.get("originCity"),
	            data.get("destinationCity"),
	            data.get("originProvince"),
	            data.get("destinationProvince"),
	            xDock,
	            CommodityNames,
	            skidCount,
	            pieces,
	            declaredWeight,
	            length,
	            width,
	            height,
	            stackable,
	            data.get("weightUoM"),
	            data.get("dimUoM"),
	            data.get("accessorial1Code"),
	            data.get("accessorial1Value"),
	            data.get("accessorial2Code"),
	            data.get("accessorial2Value"),
	            data.get("accessorial3Code"),
	            data.get("accessorial3Value")
	    );

	    System.out.println("Final Payload:\n" + payload);

	    return RestAssured.given()
	            .header("Authorization", "Bearer " + TokenManager.getToken())
	            .contentType("application/json")
	            .body(payload)
	            .post(ConfigManager.get("pricing.endpoint"));
	}
}