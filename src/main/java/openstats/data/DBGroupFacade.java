package openstats.data;

import java.util.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import openstats.model.*;

public class DBGroupFacade {

	@Inject
    private EntityManager em;
	
	public DBGroupFacade() {}
    // for testing
	public DBGroupFacade(EntityManager em) {
		this.em = em;
	}
	
	/**
	 * Create an DBGroup given name and description.
	 * 
	 * @param groupName
	 * @param groupDescription
	 * @return
	 * @throws OpenStatsException
	 */
	public DBGroup createDBGroup(String groupName, String groupDescription) throws OpenStatsException {
        // GroupNameHandler.initialize(em);
		DBGroup dbGroup = new DBGroup(groupName, groupDescription); 
        GroupNameHandler.createDBGroup(dbGroup, em);
        return dbGroup;
	}

	/**
	 * Gets a DBGroup by its name.
	 * 
	 * @param groupName
	 * @return {@link} DBGroup
	 * @throws OpenStatsException
	 */
	public DBGroup getDBGroup(String groupName) throws OpenStatsException {
        // GroupNameHandler.initialize(em);
        return GroupNameHandler.getDBGroup(groupName, em);
	}
	
	/**
	 * Allows for the updating of a DBGroup description.
	 * 
	 * @param groupName
	 * @param groupDescription
	 * @throws OpenStatsException
	 */
	public void udpateDBGroup(String groupName, String groupDescription) throws OpenStatsException {
		DBGroup dbGroup = GroupNameHandler.getDBGroup(groupName, em);
		dbGroup.setGroupDescription(groupDescription);
		GroupNameHandler.updateDBGroup(dbGroup, em);
	}

	/**
	 * Looks up and deletes an DBGroup by its name.
	 *  
	 * @param groupName
	 * @throws OpenStatsException
	 */
	public void deleteDBGroup(String groupName) throws OpenStatsException {
		DBGroup dbGroup = GroupNameHandler.getDBGroup(groupName, em);
		GroupNameHandler.deleteDBGroup(dbGroup, em);
	}
		
}
