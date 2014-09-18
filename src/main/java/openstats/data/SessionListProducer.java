package openstats.data;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import openstats.model.Session;

import java.util.List;

@RequestScoped
public class SessionListProducer {

    @Inject
    private SessionRepository sessionRepository;

    private List<Session> sessions;

    // @Named provides access the return value via the EL variable name "members" in the UI (e.g.
    // Facelets or JSP view)
    @Produces
    @Named
    public List<Session> getSessions() {
        return sessions;
    }

    public void onSessionListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Session session) {
        listAllSessions();
    }

    @PostConstruct
    public void listAllSessions() {
        sessions = sessionRepository.listAllSessions();
    }
}
