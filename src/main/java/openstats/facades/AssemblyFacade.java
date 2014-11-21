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
		return em.createNamedQuery(DBAssembly.LISTASSEMBLIES, DBAssembly.class).getResultList();
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
	 * Build OSAssembly object for DBGroup for State/Session.
	 *  
	 * @param dbGroup
	 * @param state
	 * @param session
	 * @return {@link}OSAssembly
	 */
	public Assembly buildOSAssembly(DBGroup dbGroup, String state, String session) throws OpenStatsException {
		DBAssembly dbAssembly = findByStateSession(state, session);
		return new Assembly(dbGroup, dbAssembly);
		
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
	public Assembly buildOSAssembly(String groupName, String state, String session) throws OpenStatsException {
		DBGroup dbGroup = DBGroupHandler.getDBGroup(groupName, em);
		DBAssembly dbAssembly = findByStateSession(state, session);
		return new Assembly(dbGroup, dbAssembly);
		
	}
	
    /**
     * Write (or update?) the DBAssembly referenced by the OSAssembly
     * 
     * @param osAssembly
     * @throws OpenStatsException
     */
	public void writeOSAssembly(Assembly osAssembly) throws OpenStatsException {
		DBGroup dbGroup = DBGroupHandler.getDBGroup(osAssembly.getOSGroup().getGroupName(), em);
		
		if ( dbGroup == null ) {
			// create new DBGroup
			dbGroup = new DBGroup(osAssembly.getOSGroup());
			DBGroupHandler.createDBGroup(dbGroup, em);
		}
		DBAssembly dbAssembly;
		Long count = checkByStateSession(osAssembly.getState(), osAssembly.getSession());
		if ( count > 0 ) {
			// update existing one
			dbAssembly = findByStateSession(osAssembly.getState(), osAssembly.getSession());
			dbAssembly.putGroup(dbGroup, osAssembly);
			em.merge(dbAssembly);
		} else {
			// create a new one
			dbAssembly = new DBAssembly();
			dbAssembly.putGroup(dbGroup, osAssembly);
			em.persist(dbAssembly);
		}
	}

	/**
     * Delete the DBAssembly referenced by the OSAssembly
	 * 
	 * @param osAssembly
	 * @throws OpenStatsException 
	 */
	public void deleteAssemblyGroup(String groupName, String state, String session) throws OpenStatsException {
		// maybe someday delete the group if nothing references it.
		DBAssembly dbAssembly = findByStateSession(state, session);
		dbAssembly.removeGroup(DBGroupHandler.getDBGroup(groupName, em));
		em.merge(dbAssembly);
		
	}

}
