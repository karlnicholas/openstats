package openstats.data;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import openstats.model.Assembly;

import java.util.List;

@RequestScoped
public class AssemblyListProducer {

    @Inject
    private AssemblyRepository assemblyRepository;

    private List<Assembly> assemblies;

    // @Named provides access the return value via the EL variable name "members" in the UI (e.g.
    // Facelets or JSP view)
    @Produces
    @Named
    public List<Assembly> getAssemblies() {
        return assemblies;
    }

    public void onAssemblyListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Assembly assembly) {
        listAllAssemblies();
    }

    @PostConstruct
    public void listAllAssemblies() {
        assemblies = assemblyRepository.listAllAssemblies();
    }
}
