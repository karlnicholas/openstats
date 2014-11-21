package openstats.model;

import java.util.*;

import javax.xml.bind.annotation.XmlRootElement;

import openstats.dbmodel.*;

@XmlRootElement
public class Assembly implements Comparable<Assembly> {

	private String state;
	private String session;
	private Group osGroup;
	private Districts osDistricts;
	private GroupInfo aggregateGroupInfo; 
	private GroupInfo computationGroupInfo; 
	private List<Long> aggregateValues;
	private List<Double> computationValues;
	
	public Assembly() {
		this.aggregateGroupInfo = null; 
		this.computationGroupInfo = null; 
		this.aggregateValues = null;
		this.computationValues = null;
	}
	public Assembly(String state, String session, Group osGroup) {
		this.state = state;
		this.session = session;
		this.osGroup = osGroup;
		this.osDistricts = new Districts();
		this.aggregateGroupInfo = null; 
		this.computationGroupInfo = null; 
		this.aggregateValues = null;
		this.computationValues = null;
	}
	
	public Assembly(DBGroup dbGroup, DBAssembly dbAssembly) throws OpenStatsException {
		this.state = dbAssembly.getState();
		this.session = dbAssembly.getSession();
		this.osGroup = new Group(dbGroup.getGroupName(), dbGroup.getGroupDescription());
		this.osDistricts = new Districts(dbGroup, dbAssembly.getDistricts());

		boolean hasResult = false;
		if ( dbAssembly.getAggregateGroupMap().containsKey(dbGroup) || dbAssembly.getAggregateMap().containsKey(dbGroup) ) {
			hasResult = true;
			aggregateGroupInfo = new GroupInfo(dbAssembly.getAggregateGroupMap().get(dbGroup));
			aggregateValues = new ArrayList<Long>(dbAssembly.getAggregateMap().get(dbGroup).getValueList());
		}

		if ( dbAssembly.getComputationGroupMap().containsKey(dbGroup) || dbAssembly.getComputationMap().containsKey(dbGroup) ) {
			hasResult = true;
			computationGroupInfo = new GroupInfo(dbAssembly.getComputationGroupMap().get(dbGroup));
			computationValues = new ArrayList<Double>(dbAssembly.getComputationMap().get(dbGroup).getValueList());
		}
		if ( !hasResult ) throw new OpenStatsException("No results for group " + dbGroup.getGroupName());

	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public Group getOSGroup() {
		return osGroup;
	}
	public void setOSGroup(Group osGroup) {
		this.osGroup = osGroup;
	}
	public Districts getOSDistricts() {
		return osDistricts;
	}
	public void setOSDistricts(Districts osDistricts) {
		this.osDistricts = osDistricts;
	}
	public GroupInfo getAggregateGroupInfo() {
		return aggregateGroupInfo;
	}
	public void setAggregateGroupInfo(GroupInfo aggregateGroupInfo) {
		this.aggregateGroupInfo = aggregateGroupInfo;
	}
	public GroupInfo getComputationGroupInfo() {
		return computationGroupInfo;
	}
	public void setComputationGroupInfo(GroupInfo computationGroupInfo) {
		this.computationGroupInfo = computationGroupInfo;
	}
	public List<Long> getAggregateValues() {
		return aggregateValues;
	}
	public void setAggregateValues(List<Long> aggregateValues) {
		this.aggregateValues = aggregateValues;
	}
	public List<Double> getComputationValues() {
		return computationValues;
	}
	public void setComputationValues(List<Double> computationValues) {
		this.computationValues = computationValues;
	}
	@Override
	public int compareTo(Assembly assembly) {
		int s = state.compareTo(assembly.state);
		if ( s != 0 ) return s;
		return this.session.compareTo(assembly.session);
	}
}
