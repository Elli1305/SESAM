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
	private BigDecimal longitude;

	@Column
	private BigDecimal latitude;

	public Coordinate(BigDecimal longitude, BigDecimal latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public Coordinate() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal x) {
		this.longitude = x;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal y) {
		this.latitude = y;
	}
}
