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

	public static List<List<Coordinate>> parsePolygonsFromGeoJson(final String content) throws JsonProcessingException {
		final ObjectMapper objectMapper = new ObjectMapper();
		final JsonNode rootNode = objectMapper.readTree(content);
		final JsonNode featuresNode = rootNode.get("features");
		final List<List<Coordinate>> polygons = new ArrayList<>();

		for (final JsonNode featureNode : featuresNode) {
			final JsonNode geometryNode = featureNode.get("geometry");
			final String geometryType = geometryNode.get("type").asText();

			if (geometryType.equals("Polygon")) {
				final JsonNode coordinatesNode = geometryNode.get("coordinates").get(0);
				final List<Coordinate> polygonCoordinates = new ArrayList<>();

				for (final JsonNode coordinateNode : coordinatesNode) {
					final double longitude = coordinateNode.get(0).asDouble();
					final double latitude = coordinateNode.get(1).asDouble();
					final Coordinate coordinate = new Coordinate(BigDecimal.valueOf(longitude),
							BigDecimal.valueOf(latitude));
					polygonCoordinates.add(coordinate);
				}

				polygons.add(polygonCoordinates);
			}
		}

		return polygons;
	}

	public static List<List<Coordinate>> parseLinesFromGeoJson(final String geoJson) throws JsonProcessingException {
		final ObjectMapper objectMapper = new ObjectMapper();
		final JsonNode root = objectMapper.readTree(geoJson);
		final List<List<Coordinate>> lines = new ArrayList<>();

		for (final JsonNode featureNode : root.get("features")) {
			final JsonNode geometryNode = featureNode.get("geometry");
			final String geometryType = geometryNode.get("type").asText();
			if (geometryType.equals("LineString")) {
				final JsonNode coordinatesNode = geometryNode.get("coordinates");
				final List<Coordinate> lineCoordinates = new ArrayList<>();

				for (final JsonNode coordinateNode : coordinatesNode) {
					final double longitude = coordinateNode.get(0).asDouble();
					final double latitude = coordinateNode.get(1).asDouble();
					final Coordinate coordinate = new Coordinate(BigDecimal.valueOf(longitude),
							BigDecimal.valueOf(latitude));
					lineCoordinates.add(coordinate);
				}
				lines.add(lineCoordinates);
			}
		}
		return lines;
	}


}