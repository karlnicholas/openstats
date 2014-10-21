package openstats.data;

import javax.inject.Inject;
import javax.persistence.*;

import openstats.dbmodel.*;
import openstats.osmodel.OSAssembly;
import openstats.service.AssemblyUpdate;

public class DBGroupFacade {

	@Inject
    private EntityManager em;
	@Inject
	private AssemblyRepository assemblyRepository;
	@Inject
	private AssemblyUpdate assemblyUpdate;
	
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
		assemblyUpdate.writeOSAssembly(osAssembly);
	}
	
	/**
	 * Build OSAssembly object for DBGroup for State/Session.
	 *  
	 * @param dbGroup
	 * @param state
	 * @param session
	 * @return {@link}OSAssembly
	 */
	public OSAssembly buildOSAssembly(DBGroup dbGroup, String state, String session) {
		DBAssembly dbAssembly = assemblyRepository.findByStateSession(state, session);
		return new OSAssembly(dbGroup, dbAssembly);
		
	}
		
	/**
	 * Build OSAssembly object for Group/State/Session.
	 *  
	 * @param groupName
	 * @param state
	 * @param session
	 * @return {@link}OSAssembly
	 * @throws OpenStatsException 
	 */
	public OSAssembly buildOSAssembly(String groupName, String state, String session) throws OpenStatsException {
		DBGroup dbGroup = DBGroupHandler.getDBGroup(groupName, em);
		DBAssembly dbAssembly = assemblyRepository.findByStateSession(state, session);
		return new OSAssembly(dbGroup, dbAssembly);
		
	}
}
