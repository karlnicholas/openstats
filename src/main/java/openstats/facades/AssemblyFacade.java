package openstats.facades;

import java.util.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

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

	public AssemblyFacade() {}
    // for testing
    public AssemblyFacade(EntityManager em) {
    	this.em = em;
    }

    public DBAssembly findById(Long id) {
        return em.find(DBAssembly.class, id);
    }

    public List<DBAssembly> listAllAssemblies() {
    	CriteriaBuilder builder = em.getCriteriaBuilder();
    	CriteriaQuery<DBAssembly> query = builder.createQuery(DBAssembly.class);
    	return em.createQuery(query.select(query.from(DBAssembly.class))).getResultList();
    }
    
    public DBAssembly findByStateSession(String state, String session) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DBAssembly> criteria = cb.createQuery(DBAssembly.class);
        Root<DBAssembly> assembly = criteria.from(DBAssembly.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        criteria.select(assembly).where(
        		cb.equal(assembly.get("state"), state), 
        		cb.equal(assembly.get("session"), session)
        	);
        return em.createQuery(criteria).getSingleResult();
        
    }

    public Long checkByStateSession(String state, String session) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<DBAssembly> assembly = criteria.from(DBAssembly.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        criteria.select(cb.count(assembly)).where(
        		cb.equal(assembly.get("state"), state), 
        		cb.equal(assembly.get("session"), session)
        	);
        return em.createQuery(criteria).getSingleResult();
        
    }

    /**
     * 
     * @param state
     * @param session
     * @return
     */
    public Set<DBGroup> loadGroupsForAssembly(String state, String session) {
		Set<DBGroup> groups = new TreeSet<DBGroup>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DBAssembly> criteria = cb.createQuery(DBAssembly.class);
        Root<DBAssembly> assembly = criteria.from(DBAssembly.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        criteria.select(assembly).where(
        		cb.equal(assembly.get("state"), state), 
        		cb.equal(assembly.get("session"), session)
        	);
        DBAssembly dbAssembly = em.createQuery(criteria).getSingleResult();
		for ( DBGroup key: dbAssembly.getAggregateGroupMap().keySet() ) {
			groups.add( key );
		}
		for ( DBGroup key: dbAssembly.getComputationGroupMap().keySet() ) {
			groups.add( key );
		}
		for ( DBGroup key: dbAssembly.getDistricts().getAggregateGroupMap().keySet() ) {
			groups.add(key);
		}
		for ( DBGroup key: dbAssembly.getDistricts().getComputationGroupMap().keySet() ) {
			groups.add(key);
		}
		return groups;
	}

    /**
	 * Build Assembly object for DBGroup for State/Session.
	 *  
	 * @param dbGroup
	 * @param state
	 * @param session
	 * @return {@link}Assembly
	 */
	public Assembly buildAssembly(DBGroup dbGroup, String state, String session) throws OpenStatsException {
		DBAssembly dbAssembly = findByStateSession(state, session);
		Assembly assembly = new Assembly(dbAssembly);
		assembly.copyGroup(dbGroup, dbAssembly);
		return assembly;
		
	}
		
	/**
	 * Build Assembly object for Group/State/Session.
	 *  
	 * @param groupName
	 * @param state
	 * @param session
	 * @return {@link}Assembly
	 * @throws OpenStatsException 
	 */
	public Assembly buildAssembly(String groupName, String state, String session) throws OpenStatsException {
		DBGroup dbGroup = DBGroupHandler.getDBGroup(groupName, em);
		DBAssembly dbAssembly = findByStateSession(state, session);
		Assembly assembly = new Assembly(dbAssembly);
		assembly.copyGroup(dbGroup, dbAssembly);
		return assembly;
	}
	
    /**
     * Write (or update?) the DBAssembly referenced by the Assembly
     * 
     * @param Assembly
     * @throws OpenStatsException
     */
	public void writeAssembly(Assembly assembly) throws OpenStatsException {
		DBGroup dbGroup = DBGroupHandler.createDBGroup(assembly.getGroup(), em);
		DBAssembly dbAssembly = findByStateSession(assembly.getState(), assembly.getSession());
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
		DBAssembly dbAssembly = findByStateSession(state, session);
		dbAssembly.removeGroup(DBGroupHandler.getDBGroup(groupName, em));
		em.merge(dbAssembly);
		
	}
	public openstats.model.Assembly buildAssembly2(List<DBGroup> groupNames, String state, String session) throws OpenStatsException {
		DBAssembly dbAssembly = findByStateSession(state, session);
		Assembly assembly = new Assembly(dbAssembly);
		for ( DBGroup dbGroup: groupNames ) {
//			DBGroup dbGroup = DBGroupHandler.getDBGroup(groupName, em);
			assembly.copyGroup(dbGroup, dbAssembly);
		}
		return assembly;
	}

}
