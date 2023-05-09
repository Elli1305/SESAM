package com.gpse.sesam.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.domain.location.Coordinate;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GeoJsonParserTest {

	@Test
	void parsePolygonsFromGeoJson() throws Exception {
		String content = "{ " +
				"\"type\": \"FeatureCollection\", \"features\": [ { \"type\": \"Feature\", \"geometry\": { \"type\":" +
				" \"Polygon\", \"coordinates\": [ [ [ 30.0, 10.0 ], [ 40.0, 40.0 ], [ 20.0, 40.0 ], [ 10.0, 20.0 ], " +
				"[ 30.0, 10.0 ] ] ] }, \"properties\": {} } ] }";

		List<List<Coordinate>> polygons = GeoJsonParser.parsePolygonsFromGeoJson(content);

		assertEquals(1, polygons.size());
		List<Coordinate> polygonCoordinates = polygons.get(0);
		assertEquals(5, polygonCoordinates.size());

		assertThat(polygonCoordinates.get(0).getLng(), is(BigDecimal.valueOf(30.0)));
		assertThat(polygonCoordinates.get(0).getLat(), is(BigDecimal.valueOf(10.0)));
		assertThat(polygonCoordinates.get(1).getLng(), is(BigDecimal.valueOf(40.0)));
		assertThat(polygonCoordinates.get(1).getLat(), is(BigDecimal.valueOf(40.0)));
		assertThat(polygonCoordinates.get(2).getLng(), is(BigDecimal.valueOf(20.0)));
		assertThat(polygonCoordinates.get(2).getLat(), is(BigDecimal.valueOf(40.0)));
		assertThat(polygonCoordinates.get(3).getLng(), is(BigDecimal.valueOf(10.0)));
		assertThat(polygonCoordinates.get(3).getLat(), is(BigDecimal.valueOf(20.0)));
		assertThat(polygonCoordinates.get(4).getLng(), is(BigDecimal.valueOf(30.0)));
		assertThat(polygonCoordinates.get(4).getLat(), is(BigDecimal.valueOf(10.0)));
	}

	@Test
	void parsePolygonsFromGeoJsonWithNoPolygons() throws JsonProcessingException {
		String content = "{ \"type\": \"FeatureCollection\", \"features\": [] }";

		List<List<Coordinate>> polygons = GeoJsonParser.parsePolygonsFromGeoJson(content);

		assertEquals(0, polygons.size());
	}

	@Test
	void parsePolygonsFromGeoJsonShouldRaiseExceptionIfJsonIsInvalid() {
		String content = "invalidJson";
		assertThrows(JsonParseException.class, () -> GeoJsonParser.parsePolygonsFromGeoJson(content));
	}

	@Test
	void testParseLinesFromGeoJsonWithMultiplePoints() throws Exception {
		String geoJson = "{\"type\":\"FeatureCollection\",\"features\":[{\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[10.0,20.0],[30.0,40.0],[50.0,60.0]]}}]}";


		List<List<Coordinate>> lines = GeoJsonParser.parseLinesFromGeoJson(geoJson);

		assertThat(lines.size(), is(1));

		List<Coordinate> line = lines.get(0);
		assertThat(line.get(0).getLng(), is(BigDecimal.valueOf(10.0)));
		assertThat(line.get(0).getLat(), is(BigDecimal.valueOf(20.0)));
		assertThat(line.get(1).getLng(), is(BigDecimal.valueOf(30.0)));
		assertThat(line.get(1).getLat(), is(BigDecimal.valueOf(40.0)));
		assertThat(line.get(2).getLng(), is(BigDecimal.valueOf(50.0)));
		assertThat(line.get(2).getLat(), is(BigDecimal.valueOf(60.0)));
	}

	@Test
	void testParsePointsFromGeoJsonWithNoPoints() throws Exception {
		String geoJson = "{ \"type\": \"FeatureCollection\", \"features\": [] }";

		List<List<Coordinate>> points = GeoJsonParser.parseLinesFromGeoJson(geoJson);

		assertThat(points.size(), is(0));
	}

	@Test
	void testParsePointsFromGeoJsonWithInvalidJson() {
		String geoJson = "invalidJson";

		assertThrows(JsonParseException.class, () -> GeoJsonParser.parseLinesFromGeoJson(geoJson));
	}
}