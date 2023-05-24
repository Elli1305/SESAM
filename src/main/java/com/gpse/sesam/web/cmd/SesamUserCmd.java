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

    public SesamUserCmd(String email, String password, String firstName, String lastName,
                        List<SesamUserRole.AttainableRole> requestedRoles) {
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<SesamUserRole.AttainableRole> getRequestedRoles() {
        return requestedRoles;
    }

    public void setRequestedRoles(List<SesamUserRole.AttainableRole> requestedRoles) {
        this.requestedRoles = requestedRoles;
    }
}
