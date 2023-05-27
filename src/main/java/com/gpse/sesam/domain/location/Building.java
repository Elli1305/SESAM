package com.gpse.sesam.domain.location;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Building {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String name;

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "id", fetch = FetchType.EAGER)
	private List<Floor> floors = new ArrayList<>();

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Location location;

	protected Building() {

	}

    public Building(final String name) {
        this.name = name;
    }

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public List<Floor> getFloors() {
		return floors;
	}

	public void setFloors(final List<Floor> floors) {
		this.floors = floors;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void addFloor(final Floor floor) {
		floors.add(floor);
        floor.setBuilding(this);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void removeFloor(Floor floor) {
        floors.remove(floor);
        floor.setBuilding(null);
    }
}
