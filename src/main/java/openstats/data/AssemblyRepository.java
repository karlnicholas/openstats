package openstats.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import openstats.dbmodel.DBAssembly;
import openstats.dbmodel.DBAssemblyHandler;
import openstats.dbmodel.DBDistricts;
import openstats.dbmodel.DBGroup;
import openstats.dbmodel.DBGroupHandler;
import openstats.dbmodel.OpenStatsException;
import openstats.model.Assembly;

@ApplicationScoped
public class AssemblyRepository {
	@Inject
	private EntityManager em;
	
//	private TypedQuery<AggregateMapEntry> districtAggregateMapQuery;
//	private TypedQuery<ComputationMapEntry> districtComputationMapQuery;

	
	public AssemblyRepository() {}
    // for testing
    public AssemblyRepository(EntityManager em) {
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
    
    public Assembly getAssembly(String state, String session) {
		return DBAssemblyHandler.getAssembly( state, session, em );
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
        assembly.fetch("districts");
        assembly.fetch("groupInfoMap", JoinType.LEFT);
        
//        assembly.fetch(1);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        criteria.select(assembly).where(
        		cb.equal(assembly.get("state"), state), 
        		cb.equal(assembly.get("session"), session)
        	).distinct(true);
        DBAssembly dbAssembly = em.createQuery(criteria).getSingleResult();
		for ( DBGroup key: dbAssembly.getGroupInfoMap().keySet() ) {
			groups.add( key );
		}
		
		DBDistricts self = dbAssembly.getDistricts();
        CriteriaQuery<DBDistricts> dCriteria = cb.createQuery(DBDistricts.class);
        Root<DBDistricts> districts = dCriteria.from(DBDistricts.class);
        districts.fetch("groupInfoMap", JoinType.LEFT);
        
//        assembly.fetch(1);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        dCriteria.select(districts).where(cb.equal(districts, self)).distinct(true);
        DBDistricts dbDistricts = em.createQuery(dCriteria).getSingleResult();

        for ( DBGroup key: dbDistricts.getGroupInfoMap().keySet() ) {
			groups.add(key);
		}
		return groups;
	}

	public Assembly buildAssemblyFromNames(List<String> groupNames, String state, String session) throws OpenStatsException {
		List<DBGroup> dbGroupNames = new ArrayList<DBGroup>();		
		for ( String groupName: groupNames ) {
			dbGroupNames.add( DBGroupHandler.getDBGroup(groupName, em) );
		}
		return buildAssemblyFromGroups(dbGroupNames, state, session);
	}

	public Assembly buildAssemblyFromGroups(List<DBGroup> dbGroups, String state, String session) throws OpenStatsException {
		
		DBAssembly dbAssembly = em.createNamedQuery(DBAssembly.getAssemblyGroup, DBAssembly.class)
				.setParameter(1, state)
				.setParameter(2, session)
				.setParameter(3, dbGroups)
				.setParameter(4, dbGroups)
				.getSingleResult();
//        
		dbAssembly.setDistricts( 
				em.createNamedQuery(DBDistricts.districtListQuery, DBDistricts.class )
				.setParameter(1, dbAssembly.getDistricts())
				.setParameter(2, dbGroups)
				.setParameter(3, dbGroups)
				.getSingleResult()
			);        
//
		Assembly assembly = new Assembly(dbAssembly);

		for ( DBGroup dbGroup: dbGroups ) {
			assembly.copyGroup(dbGroup, dbAssembly);
		}

		return assembly;
	}


}
