package com.gpse.sesam.web.cmd;

import com.gpse.sesam.domain.user.SesamUserRole;

import java.util.List;

public class SesamUserCmd {
	private String email;

	private String password;

	private String firstName;

	private String lastName;

	private List<SesamUserRole.AttainableRole> requestedRoles;

	public SesamUserCmd() {
    }

    public SesamUserCmd(final String email, final String password, final String firstName, final String lastName,
						final List<SesamUserRole.AttainableRole> requestedRoles) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.requestedRoles = requestedRoles;
	}

	public String getEmail() {
        return email;
    }

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public List<SesamUserRole.AttainableRole> getRequestedRoles() {
		return requestedRoles;
	}

	public void setRequestedRoles(final List<SesamUserRole.AttainableRole> requestedRoles) {
		this.requestedRoles = requestedRoles;
	}
}
