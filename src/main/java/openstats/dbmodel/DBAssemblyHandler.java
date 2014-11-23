package openstats.dbmodel;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

import openstats.model.Assembly;

public class DBAssemblyHandler {

	private DBAssemblyHandler() {}
	private static class SingletonHelper {
		private static final DBAssemblyHandler INSTANCE = new DBAssemblyHandler();
	}
	private static void checkInit(DBAssemblyHandler handler, EntityManager em) throws OpenStatsException {
		if ( handler.assemblyMap == null ) {
			handler.assemblyMap = new TreeMap<String, Assembly>();
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<DBAssembly> criteria = cb.createQuery(DBAssembly.class);
	        Root<DBAssembly> assemblyRoot = criteria.from(DBAssembly.class);
	        for( DBAssembly dbAssembly: em.createQuery(criteria.select(assemblyRoot)).getResultList() ) {
				String key = dbAssembly.getState()+'-'+dbAssembly.getSession();
				Assembly assembly = new Assembly(dbAssembly);
	        	handler.assemblyMap.put(key, assembly);
	        }
		}
	}

	private TreeMap<String, Assembly> assemblyMap = null;
	
	public static void createAssembly(Assembly assembly, EntityManager em) throws OpenStatsException {
		DBAssemblyHandler handler = SingletonHelper.INSTANCE;
		synchronized(handler) {
			checkInit(handler, em);
			String key = assembly.getState()+'-'+assembly.getSession();
			if ( handler.assemblyMap.containsKey(key) ) throw new OpenStatsException("Assembly already created: " + key);
			DBAssembly dbAssembly = new DBAssembly(assembly);
			em.persist(dbAssembly);
			handler.assemblyMap.put(key, assembly);
		}
	}

	public static Assembly getAssembly(String state, String session, EntityManager em) throws OpenStatsException {
		DBAssemblyHandler handler = SingletonHelper.INSTANCE;
		synchronized(handler) {
			checkInit(handler, em);
			String key = state+'-'+session;
			return handler.assemblyMap.get(key);
		}
	}

}
