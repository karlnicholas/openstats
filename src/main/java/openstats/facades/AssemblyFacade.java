package openstats.facades;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import openstats.data.AssemblyRepository;
import openstats.dbmodel.*;
import openstats.model.Assembly;

//
//The @Stateless annotation eliminates the need for manual transaction demarcation
//I have no idea what that means ...
//
@Stateless
public class AssemblyFacade {

	@Inject
	private EntityManager em;
	
	@Inject
	private AssemblyRepository assemblyRepo;

	public AssemblyFacade() {}
    // for testing
    public AssemblyFacade(EntityManager em) {
    	this.em = em;
    	assemblyRepo = new AssemblyRepository(em);
    }

	
    /**
     * Write (or update?) the DBAssembly referenced by the Assembly
     * 
     * @param Assembly
     * @throws OpenStatsException
     */

    public void writeAssembly(Assembly assembly) throws OpenStatsException {
		DBGroup dbGroup = DBGroupHandler.createDBGroup(assembly.getGroup(), em);
		DBAssembly dbAssembly = assemblyRepo.findByStateSession(assembly.getState(), assembly.getSession());
		dbAssembly.copyGroup(dbGroup, assembly);
		em.merge(dbAssembly);
	}

	/**
     * Delete the DBAssembly referenced by the Assembly
	 * 
	 * @param Assembly
	 * @throws OpenStatsException 
	 */
	public void deleteAssemblyGroup(String groupName, String state, String session) throws OpenStatsException {
		// maybe someday delete the group if nothing references it.
		DBAssembly dbAssembly = assemblyRepo.findByStateSession(state, session);
		dbAssembly.removeGroup(DBGroupHandler.getDBGroup(groupName, em));
		em.merge(dbAssembly);
		
	}
}
