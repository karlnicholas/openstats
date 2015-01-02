package openstats.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Root;

import openstats.dbmodel.AggregateResults;
import openstats.dbmodel.ComputationResults;
import openstats.dbmodel.DBAssembly;
import openstats.dbmodel.DBAssemblyHandler;
import openstats.dbmodel.DBDistrict;
import openstats.dbmodel.DBDistricts;
import openstats.dbmodel.DBGroup;
import openstats.dbmodel.DBGroupHandler;
import openstats.dbmodel.DBGroupInfo;
import openstats.dbmodel.OpenStatsException;
import openstats.model.Assembly;

public class AssemblyRepository {
	@Inject
	private EntityManager em;

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
        assembly.fetch("aggregateGroupMap", JoinType.LEFT);
        assembly.fetch("computationGroupMap", JoinType.LEFT);
        
//        assembly.fetch("districts");
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        criteria.select(assembly).where(
        		cb.equal(assembly.get("state"), state), 
        		cb.equal(assembly.get("session"), session)
        	).distinct(true);
        DBAssembly dbAssembly = em.createQuery(criteria).getSingleResult();
		for ( DBGroup key: dbAssembly.getAggregateGroupMap().keySet() ) {
			groups.add( key );
		}
		for ( DBGroup key: dbAssembly.getComputationGroupMap().keySet() ) {
			groups.add( key );
		}
		
		DBDistricts self = dbAssembly.getDistricts();
        CriteriaQuery<DBDistricts> dCriteria = cb.createQuery(DBDistricts.class);
        Root<DBDistricts> districts = dCriteria.from(DBDistricts.class);
        districts.fetch("aggregateGroupMap", JoinType.LEFT);
        districts.fetch("computationGroupMap", JoinType.LEFT);
        
//        assembly.fetch("districts");
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        dCriteria.select(districts).where(cb.equal(districts.get("id"), self.getId())).distinct(true);
        DBDistricts dbDistricts = em.createQuery(dCriteria).getSingleResult();

        for ( DBGroup key: dbDistricts.getAggregateGroupMap().keySet() ) {
			groups.add(key);
		}
		for ( DBGroup key: dbDistricts.getComputationGroupMap().keySet() ) {
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

	/**
	 * Build Assembly for export.
	 * 
	 * @param groupNames
	 * @param state
	 * @param session
	 * @return
	 * @throws OpenStatsException
	 */
	public Assembly buildAssemblyFromGroups(List<DBGroup> groupNames, String state, String session) throws OpenStatsException {

//		DBAssembly dbAssembly = findByStateSession(state, session);
//		Set<DBGroup> groups = new TreeSet<DBGroup>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DBAssembly> criteria = cb.createQuery(DBAssembly.class);
        Root<DBAssembly> root = criteria.from(DBAssembly.class);
        root.fetch("aggregateGroupMap", JoinType.LEFT);
        root.fetch("computationGroupMap", JoinType.LEFT);
        root.fetch("aggregateMap", JoinType.LEFT);
        root.fetch("computationMap", JoinType.LEFT);
//        MapJoin<DBAssembly, DBGroup, AggregateResults> aAggregateMap = root.joinMap("aggregateMap");
//        MapJoin<DBAssembly, DBGroup, ComputationResults> aComputationMap = root.joinMap("computationMap");
//        assembly.fetch("districts");
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        criteria.select(root).where(
        		cb.equal(root.get("state"), state), 
        		cb.equal(root.get("session"), session) 
//        		aAggregateMap.key().in(groupNames), 
//        		aComputationMap.key().in(groupNames) 
        	).distinct(true);
        DBAssembly dbAssembly = em.createQuery(criteria).getSingleResult();
        System.out.println("root");
		
		DBDistricts self = dbAssembly.getDistricts();
        CriteriaQuery<DBDistricts> dCriteria = cb.createQuery(DBDistricts.class);
        Root<DBDistricts> districts = dCriteria.from(DBDistricts.class);
        MapJoin<DBDistrict, DBGroup, AggregateResults> aggregateGroupMap = districts.joinMap("aggregateGroupMap");
//        MapJoin<DBDistrict, DBGroup, ComputationResults> computationGroupMap = districts.joinMap("computationGroupMap");
        districts.fetch("aggregateGroupMap", JoinType.LEFT);
        districts.fetch("computationGroupMap", JoinType.LEFT);
        
//        assembly.fetch("districts");
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        dCriteria.select(districts).where(
        		cb.equal(districts.get("id"), self.getId()), 
        		aggregateGroupMap.key().in(groupNames) 
//        		computationGroupMap.key().in(groupNames) 
        	).distinct(true);
        dbAssembly.setDistricts( em.createQuery(dCriteria).getSingleResult() );
        System.out.println("districts");
        
        CriteriaQuery<DBDistrict> distCriteria = cb.createQuery(DBDistrict.class);
        Root<DBDistrict> districtRoot = distCriteria.from(DBDistrict.class);
//        districtRoot.fetch("aggregateMap", JoinType.LEFT);
//        districtRoot.fetch("computationMap", JoinType.LEFT);
        MapJoin<DBDistrict, DBGroup, AggregateResults> dAggregateMap = districtRoot.joinMap("aggregateMap");
        MapJoin<DBDistrict, DBGroup, ComputationResults> dComputationMap = districtRoot.joinMap("computationMap");

        for(DBDistrict dbDistrict: dbAssembly.getDistricts().getDistrictList() ) {
            distCriteria.select(districtRoot).where(
            		cb.equal(districtRoot.get("id"), dbDistrict.getId()),  
            		dAggregateMap.key().in(groupNames),
            		dComputationMap.key().in(groupNames) 
            	).distinct(true);
            DBDistrict districtFetch = em.createQuery(distCriteria).getSingleResult();
            dbDistrict.setAggregateMap( districtFetch.getAggregateMap() );
            dbDistrict.setComputationMap( districtFetch.getComputationMap() );
        }
        System.out.println("districtList");

		Assembly assembly = new Assembly(dbAssembly);
		for ( DBGroup dbGroup: groupNames ) {
			assembly.copyGroup(dbGroup, dbAssembly);
		}
		return assembly;
	}


}
