package openstats.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import openstats.dbmodel.DBAssembly;
import openstats.dbmodel.DBAssemblyHandler;
import openstats.dbmodel.DBDistrict;
import openstats.dbmodel.DBDistricts;
import openstats.dbmodel.DBGroup;
import openstats.dbmodel.DBGroupHandler;
import openstats.dbmodel.DBGroupInfo;
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
        assembly.fetch("aggregateGroupMap", JoinType.LEFT);
        assembly.fetch("computationGroupMap", JoinType.LEFT);
        
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
        districts.fetch("aggregateGroupMap", JoinType.LEFT);
        districts.fetch("computationGroupMap", JoinType.LEFT);
        
//        assembly.fetch(1);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        dCriteria.select(districts).where(cb.equal(districts.get("id"), self.getId())).distinct(true);
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
	
	public static class GroupMapEntry {
		public DBGroup key;
		public DBGroupInfo value;
		public GroupMapEntry(DBGroup key, DBGroupInfo value) {
			this.key = key;
			this.value = value;
		}
	}

	private void loadAssemblyGroups(DBAssembly dbAssembly, List<DBGroup> dbGroups) {
/*		
    	TypedQuery<GroupMapEntry> assemblyAggregateGroupMapQuery = em.createNamedQuery(DBAssembly.getAggregateGroupMap, GroupMapEntry.class);
        List<GroupMapEntry> groupMapEntries = assemblyAggregateGroupMapQuery.setParameter(1, dbAssembly).setParameter(2, dbGroups).getResultList();
        Map<DBGroup, DBGroupInfo> aggregateGroupMap = new HashMap<DBGroup, DBGroupInfo>();
        for( GroupMapEntry entry: groupMapEntries) {
        	aggregateGroupMap.put(entry.key, entry.value);
        }
        dbAssembly.setAggregateGroupMap(aggregateGroupMap);

    	TypedQuery<GroupMapEntry> assemblyComputationGroupMapQuery = em.createNamedQuery(DBAssembly.getComputationGroupMap, GroupMapEntry.class);
        groupMapEntries = assemblyComputationGroupMapQuery.setParameter(1, dbAssembly).setParameter(2, dbGroups).getResultList();
        Map<DBGroup, DBGroupInfo> computationGroupMap = new HashMap<DBGroup, DBGroupInfo>();
        for( GroupMapEntry entry: groupMapEntries) {
        	computationGroupMap.put(entry.key, entry.value);
        }
        dbAssembly.setComputationGroupMap(computationGroupMap);
*/      
	}

	private void loadDistrictsGroupMaps(DBDistricts dbDistricts, List<DBGroup> dbGroups) {
/*
        TypedQuery<GroupMapEntry> districtsAggregateGroupMapQuery = em.createNamedQuery( DBDistricts.districtsAggregateGroupMapQuery, GroupMapEntry.class);
        List<GroupMapEntry> groupMapEntries = districtsAggregateGroupMapQuery.setParameter(1, dbDistricts).setParameter(2, dbGroups).getResultList();
        Map<DBGroup, DBGroupInfo> aggregateGroupMap = new HashMap<DBGroup, DBGroupInfo>();
        for( GroupMapEntry entry: groupMapEntries) {
        	aggregateGroupMap.put(entry.key, entry.value);
        }
        dbDistricts.setAggregateGroupMap(aggregateGroupMap);

        TypedQuery<GroupMapEntry> districtsComputationGroupMapQuery = em.createNamedQuery( DBDistricts.districtsComputationGroupMapQuery, GroupMapEntry.class);
        groupMapEntries = districtsComputationGroupMapQuery.setParameter(1, dbDistricts).setParameter(2, dbGroups).getResultList();
        Map<DBGroup, DBGroupInfo> computationGroupMap = new HashMap<DBGroup, DBGroupInfo>();
        for( GroupMapEntry entry: groupMapEntries) {
        	computationGroupMap.put(entry.key, entry.value);
        }
        dbDistricts.setComputationGroupMap(computationGroupMap);
*/        
	}

	public Assembly buildAssemblyFromGroups(List<DBGroup> dbGroups, String state, String session) throws OpenStatsException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DBAssembly> criteria = cb.createQuery(DBAssembly.class);
        Root<DBAssembly> root = criteria.from(DBAssembly.class);
        root.fetch("districts");
        criteria.select(root).where(
        		cb.equal(root.get("state"), state), 
        		cb.equal(root.get("session"), session) 
        	).distinct(true);
        DBAssembly dbAssembly = em.createQuery(criteria).getSingleResult();

        System.out.println("root");
//        
        loadAssemblyGroups(dbAssembly, dbGroups);    
//
        System.out.println("d2istricts groups");
//        
        loadDistrictsGroupMaps(dbAssembly.getDistricts(), dbGroups);
//
        System.out.println("start districtList");

//        List<DBDistrict> districtList = em.createNamedQuery(DBDistricts.districtListQuery, DBDistrict.class).setParameter(1, dbAssembly.getDistricts()).setParameter(2, dbGroups).setParameter(3, dbGroups).getResultList();
//    	dbAssembly.getDistricts().setDistrictList(districtList);

    	System.out.println("end districtList");

		Assembly assembly = new Assembly(dbAssembly);

		System.out.println("being copy");
		for ( DBGroup dbGroup: dbGroups ) {
			assembly.copyGroup(dbGroup, dbAssembly);
		}
		System.out.println("end copy");

		return assembly;
	}


}
