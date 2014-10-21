package openstats.data;

import javax.inject.Inject;
import javax.persistence.*;

import openstats.dbmodel.*;
import openstats.osmodel.OSAssembly;

public class DBGroupFacade {

	@Inject
    private EntityManager em;
	@Inject
	private AssemblyRepository assemblyRepository;
	
	public DBGroupFacade() {}
    // for testing
	public DBGroupFacade(EntityManager em) {
		this.em = em;
		assemblyRepository = new AssemblyRepository(em);
	}
		
	/**
	 * Write an OSAssembly, deleting old entries if needed.
	 * Check consitency of OSGroup information contained therein.
	 * 
	 * @param osAssembly
	 * @throws OpenStatsException 
	 */
	public void writeOSAssembly(OSAssembly osAssembly) throws OpenStatsException {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		DBGroup dbGroup = DBGroupHandler.getDBGroup(osAssembly.getOSGroup().getGroupName(), em);
		
		if ( dbGroup == null ) {
			// create new DBGroup
			dbGroup = new DBGroup(osAssembly.getOSGroup());
			DBGroupHandler.createDBGroup(dbGroup, em);
		}
		DBAssembly dbAssembly;
		Long count = assemblyRepository.checkByStateSession(osAssembly.getState(), osAssembly.getSession());
		if ( count > 0 ) {
			// update existing one
			dbAssembly = assemblyRepository.findByStateSession(osAssembly.getState(), osAssembly.getSession());
			dbAssembly.update(dbGroup, osAssembly);
			em.merge(dbAssembly);
		} else {
			// create a new one
			dbAssembly = new DBAssembly();
			dbAssembly.update(dbGroup, osAssembly);
			em.persist(dbAssembly);
		}
		tx.commit();
		
	}
	
	/**
	 * Build OSAssembly object for Group for State/Session.
	 *  
	 * @param dbGroup
	 * @param state
	 * @param session
	 * @return
	 */
	public OSAssembly buildOSAssembly(DBGroup dbGroup, String state, String session) {
		DBAssembly dbAssembly = assemblyRepository.findByStateSession(state, session);
		return new OSAssembly(dbGroup, dbAssembly);
		
	}
		
}
