package com.gpse.sesam.web.cmd;

import com.gpse.sesam.domain.user.SesamUserRole;

import java.util.List;

public class EditUserCmd {

	private String firstName;

	private String lastName;
	private String username;

	private List<SesamUserRole.AttainableRole> roles;

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUsername() {
		return username;
	}

	public List<SesamUserRole.AttainableRole> getRoles() {
		return roles;
	}


	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public void setRoles(final List<SesamUserRole.AttainableRole> roles) {
		this.roles = roles;
	}

}
