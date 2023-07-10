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

@Service
@Primary
public class IssuerServiceImpl implements IssuerService {
	private final IssuerRepository issuerRepository;
	private final RoomRepository roomRepository;
	private final CredentialRepository credentialRepository;

	@Autowired
	public IssuerServiceImpl(final IssuerRepository issuerRepository,
							 final RoomRepository roomRepository, final CredentialRepository credentialRepository) {
		this.issuerRepository = issuerRepository;
		this.roomRepository = roomRepository;
		this.credentialRepository = credentialRepository;
	}


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

	@Override
	public Issuer getIssuerByMail(final String email) {
		return issuerRepository.findByEmail(email).orElse(null);
	}

	@Override
	public Optional<Issuer> getIssuer(final Long id) {
		return issuerRepository.findById(String.valueOf(id));
	}


	@Override
	public void deleteIssuer(final Issuer issuer) {
		issuerRepository.delete(issuer);
	}

	@Override
	public void saveAll(final Iterable<Issuer> issuers) {
		issuerRepository.saveAll(issuers);
	}

	@Override
	public void updateIssuer(final IssuerResponseCmd cmd) {
		final Optional<Issuer> issuer = getIssuer(cmd.getIssuerId());
		final List<InternalCredential> credentials = new ArrayList<>();
		if (issuer.isPresent()) {
			final Optional<Room> room = roomRepository.findById(cmd.getRoom());
			room.ifPresent(value -> issuer.get().setRoom(value));
			for (final Long cred : cmd.getCredentials()) {
				final Optional<InternalCredential> credential = credentialRepository.findById(cred);
				credential.ifPresent(credentials::add);
			}
			issuer.get().setCredentials(credentials);
			issuerRepository.save(issuer.get());
		}
	}

	@Override
	public void save(Issuer issuer) {
		issuerRepository.save(issuer);
	}
}
