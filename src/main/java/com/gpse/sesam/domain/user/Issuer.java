package com.gpse.sesam.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gpse.sesam.domain.credential.Credential;
import com.gpse.sesam.domain.location.room.Room;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

import java.io.Serial;
import java.util.List;

@Entity
public class Issuer extends SesamUser {
	@Serial
	private static final long serialVersionUID = 215982L;

	@OneToOne(cascade = CascadeType.ALL)
	private Room room;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Credential> credentials;

	protected Issuer() {
	}

	/**
	 * Creates a new {@link SesamUser}
	 *
	 * @param email       the user's email
	 * @param password    the user's password
	 * @param firstName   the user's first name
	 * @param lastName    the user's last name
	 * @param roles       the user's roles
	 * @param room        the issuer's office
	 */
	public Issuer(final String email, final String password, final String firstName, final String lastName,
				  final List<SesamUserRole> roles,
				  final Room room) {
		super(email, password, firstName, lastName, roles);
		this.room = room;
	}

	public List<Credential> getCredentials() {
		return credentials;
	}

	public void setCredentials(final List<Credential> credentials) {
		this.credentials = credentials;
	}

	public void setRoom(final Room room) {
		this.room = room;
	}

	public Room getRoom() {
		return room;
	}

	public void addCredential(final Credential credential) {
		credentials.add(credential);
	}
}
