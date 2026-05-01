package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.TokenManager;
import config.ConfigManager;

import java.util.*;

public class PricingAPI {

	public static Response calculate(Map<String, String> data) {

		// ✅ Boolean parsing
		boolean xDock = Boolean.parseBoolean(data.get("xDock"));
		boolean stackable = Boolean.parseBoolean(data.get("stackable"));

		// ✅ Number parsing
		int custId = (int) Double.parseDouble(data.get("custAccountId"));
		int skidCount = (int) Double.parseDouble(data.get("skidCount"));
		int pieces = (int) Double.parseDouble(data.get("pieces"));
		int declaredWeight = (int) Double.parseDouble(data.get("declaredWeight"));
		int length = (int) Double.parseDouble(data.get("length"));
		int width = (int) Double.parseDouble(data.get("width"));
		int height = (int) Double.parseDouble(data.get("height"));

		// =========================================================
		// ✅ MULTIPLE COMMODITY HANDLING (FIXED)
		// =========================================================
		String commodityRaw = data.get("CommodityNames");

		String[] parts = commodityRaw.split(",");

		List<String> commodityList = new ArrayList<>();

		for (int i = 0; i < parts.length; i++) {
			// If last element like QC, NB etc → merge with previous
			if (i > 0 && parts[i].trim().length() <= 3) {
				String merged = commodityList.remove(commodityList.size() - 1) + "," + parts[i].trim();
				commodityList.add(merged);
			} else {
				commodityList.add(parts[i].trim());
			}
		}

		// =========================================================
		// ✅ CUFT ITEM DETAILS
		// =========================================================
		Map<String, Object> cuftItem = new HashMap<>();
		cuftItem.put("skidCount", skidCount);
		cuftItem.put("pieces", pieces);
		cuftItem.put("declaredWeight", declaredWeight);
		cuftItem.put("length", length);
		cuftItem.put("width", width);
		cuftItem.put("height", height);
		cuftItem.put("stackable", stackable);
		cuftItem.put("weightUoM", data.get("weightUoM"));
		cuftItem.put("dimUoM", data.get("dimUoM"));

		List<Map<String, Object>> cuftItemDetails = List.of(cuftItem);

		// =========================================================
		// ✅ ACCESSORIALS (DYNAMIC - OPTIONAL SAFE HANDLING)
		// =========================================================
		List<Map<String, String>> accessorials = new ArrayList<>();

		for (int i = 1; i <= 3; i++) {
			String code = data.get("accessorial" + i + "Code");
			String value = data.get("accessorial" + i + "Value");

			if (code != null && value != null) {
				Map<String, String> acc = new HashMap<>();
				acc.put("accessorialCode", code);
				acc.put("parameterValue", value);
				accessorials.add(acc);
			}
		}

		// =========================================================
		// ✅ FINAL PAYLOAD (AUTO JSON)
		// =========================================================
		Map<String, Object> payload = new HashMap<>();

		payload.put("custAccountId", custId);
		payload.put("originCity", data.get("originCity"));
		payload.put("destinationCity", data.get("destinationCity"));
		payload.put("originProvince", data.get("originProvince"));
		payload.put("destinationProvince", data.get("destinationProvince"));
		payload.put("xDock", xDock);
		payload.put("CommodityNames", commodityList);
		payload.put("cuftItemDetails", cuftItemDetails);
		payload.put("accessorials", accessorials);

		// ✅ Debug print (pretty JSON)
		System.out.println("Final Payload:");
		System.out.println(payload);

		// =========================================================
		// ✅ API CALL
		// =========================================================
		return RestAssured.given().header("Authorization", "Bearer " + TokenManager.getToken())
				.contentType("application/json").body(payload).log().all().post(ConfigManager.get("pricing.endpoint"));
	}
}