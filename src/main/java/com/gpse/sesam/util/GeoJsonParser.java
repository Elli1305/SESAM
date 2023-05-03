package com.gpse.sesam.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpse.sesam.domain.location.Coordinate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class GeoJsonParser {

	private GeoJsonParser() {
	}

	public static List<List<Coordinate>> parseGeoJson(String content) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(content);
		JsonNode featuresNode = rootNode.get("features");
		List<List<Coordinate>> polygons = new ArrayList<>();

		for (JsonNode featureNode : featuresNode) {
			JsonNode geometryNode = featureNode.get("geometry");
			String geometryType = geometryNode.get("type").asText();

			if (geometryType.equals("Polygon")) {
				JsonNode coordinatesNode = geometryNode.get("coordinates").get(0);
				List<Coordinate> polygonCoordinates = new ArrayList<>();

				for (JsonNode coordinateNode : coordinatesNode) {
					double longitude = coordinateNode.get(0).asDouble();
					double latitude = coordinateNode.get(1).asDouble();
					Coordinate coordinate = new Coordinate(BigDecimal.valueOf(longitude), BigDecimal.valueOf(latitude));
					polygonCoordinates.add(coordinate);
				}

				polygons.add(polygonCoordinates);
			}
		}

		return polygons;
	}
}