package com.gpse.sesam.domain.location;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Coordinate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private BigDecimal lng;

	@Column
	private BigDecimal lat;

	public Coordinate(BigDecimal lng, BigDecimal lat) {
		this.lng = lng;
		this.lat = lat;
	}

	public Coordinate() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal x) {
		this.lng = x;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal y) {
		this.lat = y;
	}
}
