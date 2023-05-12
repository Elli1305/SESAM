package com.gpse.sesam.domain.user;

import com.gpse.sesam.domain.credential.Credential;
import com.gpse.sesam.domain.location.Room;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Issuer extends SesamUser {
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
     * @param credentials the list of credentials that the issuer is handing out
     */
    public Issuer(String email, String password, String firstName, String lastName, List<SesamUserRole> roles,
                  Room room, List<Credential> credentials) {
        super(email, password, firstName, lastName, roles);
        this.room = room;
        this.credentials = credentials;
    }

    public List<Credential> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }

}
