package com.gpse.sesam.domain.user.issuer;

import com.gpse.sesam.domain.credential.credentials.internal.InternalCredential;
import com.gpse.sesam.domain.credential.credentials.internal.CredentialRepository;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.room.RoomRepository;
import com.gpse.sesam.domain.user.SesamUserRole;
import com.gpse.sesam.web.cmd.IssuerResponseCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementierung des IssuerService-Interfaces.
 * Diese Klasse verwaltet Issuer-Objekte und bietet Methoden zur Abfrage, Aktualisierung und Löschung von Issuern.
 */
@Service
@Primary
public class IssuerServiceImpl implements IssuerService {
	private final IssuerRepository issuerRepository;
	private final RoomRepository roomRepository;
	private final CredentialRepository credentialRepository;

	/**
	 * Konstruktor für die IssuerServiceImpl-Klasse.
	 *
	 * @param issuerRepository        das IssuerRepository für den Datenbankzugriff
	 * @param roomRepository          das RoomRepository für den Datenbankzugriff
	 * @param credentialRepository    das CredentialRepository für den Datenbankzugriff
	 */
	@Autowired
	public IssuerServiceImpl(final IssuerRepository issuerRepository,
							 final RoomRepository roomRepository, final CredentialRepository credentialRepository) {
		this.issuerRepository = issuerRepository;
		this.roomRepository = roomRepository;
		this.credentialRepository = credentialRepository;
	}

	/**
	 * Gibt eine Liste aller Issuer zurück.
	 *
	 * @return die Liste der Issuer
	 */
	@Override
	public List<Issuer> getIssuers() {
		final List<Issuer> issuers = new ArrayList<>();
		issuerRepository.findAll().forEach(issuers::add);
		return issuers
				.stream()
				.filter(issuer ->
						issuer
								.getAuthorities()
								.contains(AuthorityUtils.createAuthorityList(
										SesamUserRole.AttainableRole.ISSUER.toString())
										.get(0)))
				.toList();
	}

	/**
	 * Gibt einen Issuer anhand seiner E-Mail-Adresse zurück.
	 *
	 * @param email die E-Mail-Adresse des Issuer
	 * @return der gefundene Issuer oder null, wenn keiner gefunden wurde
	 */
	@Override
	public Issuer getIssuerByMail(final String email) {
		return issuerRepository.findByEmail(email).orElse(null);
	}

	/**
	 * Gibt einen Issuer anhand seiner ID zurück.
	 *
	 * @param id die ID des Issuer
	 * @return der gefundene Issuer oder Optional.empty(), wenn keiner gefunden wurde
	 */
	@Override
	public Optional<Issuer> getIssuer(final Long id) {
		return issuerRepository.findById(String.valueOf(id));
	}

	/**
	 * Löscht einen Issuer.
	 *
	 * @param issuer der zu löschende Issuer
	 */
	@Override
	public void deleteIssuer(final Issuer issuer) {
		issuerRepository.delete(issuer);
	}

	/**
	 * Speichert eine Liste von Issuer.
	 *
	 * @param issuers die zu speichernden Issuer
	 */
	@Override
	public void saveAll(final Iterable<Issuer> issuers) {
		issuerRepository.saveAll(issuers);
	}

	/**
	 * Aktualisiert die Informationen eines Issuer.
	 *
	 * @param cmd die Informationen zur Aktualisierung
	 */
	@Override
	public void updateIssuer(final IssuerResponseCmd cmd) {
		final Optional<Issuer> issuer = getIssuer(cmd.getIssuerId());
		final List<InternalCredential> credentials = new ArrayList<>();
		List<InternalCredential> internalCredentials = (List<InternalCredential>) credentialRepository.findAll();

		if (issuer.isPresent()) {
			final Optional<Room> room = roomRepository.findById(cmd.getRoom());
			room.ifPresent(value -> issuer.get().setRoom(value));
			for (InternalCredential internalCredential: internalCredentials) {
				internalCredential.removeIssuer(issuer.get());
			}
			for (final Long cred : cmd.getCredentials()) {
				final Optional<InternalCredential> credential = credentialRepository.findById(cred);
				credential.ifPresent(credentials::add);
				credential.get().addIssuer(issuer.get());
			}
			issuer.get().setCredentials(credentials);
			issuerRepository.save(issuer.get());
		}
	}

	/**
	 * Speichert einen Issuer.
	 *
	 * @param issuer der zu speichernde Issuer
	 */
	@Override
	public void save(Issuer issuer) {
		issuerRepository.save(issuer);
	}
}
