package openstats.model;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

public class GroupNameHandler {
	
	private static TreeMap<String, GroupName> groupNames = new TreeMap<String, GroupName>();
	private static boolean intialized = false;
	
	public synchronized static GroupName getGroupName(String groupName, EntityManager em) {
		if (!intialized ){
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<GroupName> criteria = cb.createQuery(GroupName.class);
	        Root<GroupName> groupNameRoot = criteria.from(GroupName.class);
	        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
	        // feature in JPA 2.0
	        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
	        
	        for( GroupName groupNameDb: em.createQuery(criteria.select(groupNameRoot)).getResultList() ) {
	        	groupNames.put(groupNameDb.getGroupName(), groupNameDb);
	        }
		}
		if ( !groupNames.containsKey(groupName)) {
			GroupName eGroupName = new GroupName(groupName);
			em.persist(eGroupName);
			groupNames.put(groupName, eGroupName);
		}
		return groupNames.get(groupName);
	}

}
