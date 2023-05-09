package com.gpse.sesam.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpse.sesam.domain.location.Coordinate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class GeoJsonParser {

	private GeoJsonParser() {
	}

	public static List<List<Coordinate>> parsePolygonsFromGeoJson(String content) throws JsonProcessingException {
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
					Coordinate coordinate = new Coordinate(BigDecimal.valueOf(longitude),
							BigDecimal.valueOf(latitude));
					polygonCoordinates.add(coordinate);
				}

				polygons.add(polygonCoordinates);
			}
		}

		return polygons;
	}

	public static List<List<Coordinate>> parseLinesFromGeoJson(String geoJson) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(geoJson);
		List<List<Coordinate>> lines = new ArrayList<>();

		for (JsonNode featureNode : root.get("features")) {
			JsonNode geometryNode = featureNode.get("geometry");
			String geometryType = geometryNode.get("type").asText();
			if (geometryType.equals("LineString")) {
				JsonNode coordinatesNode = geometryNode.get("coordinates");
				List<Coordinate> lineCoordinates = new ArrayList<>();

				for (JsonNode coordinateNode : coordinatesNode) {
					double longitude = coordinateNode.get(0).asDouble();
					double latitude = coordinateNode.get(1).asDouble();
					Coordinate coordinate = new Coordinate(BigDecimal.valueOf(longitude),
							BigDecimal.valueOf(latitude));
					lineCoordinates.add(coordinate);
				}
				lines.add(lineCoordinates);
			}
		}
		return lines;
	}


}