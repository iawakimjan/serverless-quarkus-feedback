package nl.p4c.feedback.api.service;

import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import nl.p4c.feedback.api.model.Invite;
import nl.p4c.feedback.api.repository.InviteRepository;

@ApplicationScoped
public class InviteService {

    @Inject
    InviteRepository inviteRepository;

    public Optional<Invite> findInvite(String inviteCode) {
        return inviteRepository.getInvite(inviteCode);
    }

    public void closeInvite(String inviteCode) {
        inviteRepository.closeInvite(inviteCode);
    }
}
