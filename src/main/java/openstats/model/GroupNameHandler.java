package openstats.model;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

public class GroupNameHandler {

	private GroupNameHandler() {}
	private static class SingletonHelper {
		private static final GroupNameHandler INSTANCE = new GroupNameHandler();
	}
	private static void checkInit(GroupNameHandler handler, EntityManager em) {
		if ( handler.dbGroup == null ) {
			handler.dbGroup = new TreeMap<String, DBGroup>();
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<DBGroup> criteria = cb.createQuery(DBGroup.class);
	        Root<DBGroup> groupNameRoot = criteria.from(DBGroup.class);
	        for( DBGroup groupNameDb: em.createQuery(criteria.select(groupNameRoot)).getResultList() ) {
	        	handler.dbGroup.put(groupNameDb.getGroupName(), groupNameDb);
	        }
		}
	}

	private TreeMap<String, DBGroup> dbGroup = null;
	
	public static void createDBGroup(DBGroup dbGroup, EntityManager em) throws OpenStatsException {
		GroupNameHandler handler = SingletonHelper.INSTANCE;
		synchronized(handler) {
			checkInit(handler, em);
			if ( handler.dbGroup.containsKey(dbGroup.getGroupName()) ) throw new OpenStatsException("DBGroup already created: " + dbGroup.getGroupName());
			handler.dbGroup.put(dbGroup.getGroupName(), dbGroup);
		}
	}

	public static DBGroup getDBGroup(String groupName, EntityManager em) throws OpenStatsException {
		GroupNameHandler handler = SingletonHelper.INSTANCE;
		synchronized(handler) {
			checkInit(handler, em);
			return handler.dbGroup.get(groupName);
		}
	}

	public static void updateDBGroup(DBGroup dbGroup, EntityManager em) throws OpenStatsException {
		GroupNameHandler handler = SingletonHelper.INSTANCE;
		synchronized(handler) {
			checkInit(handler, em);
			em.persist(dbGroup);
			handler.dbGroup.put(dbGroup.getGroupName(), dbGroup);
		}
	}

	public static void deleteDBGroup(DBGroup dbGroup, EntityManager em) throws OpenStatsException {
		GroupNameHandler handler = SingletonHelper.INSTANCE;
		synchronized(handler) {
			checkInit(handler, em);
			em.remove(dbGroup);
			handler.dbGroup.remove(dbGroup.getGroupName());
		}
	}
}
