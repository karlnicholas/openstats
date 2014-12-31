package openstats.model;

import java.util.*;

import javax.xml.bind.annotation.XmlRootElement;

import openstats.dbmodel.*;

@XmlRootElement
public class Assembly implements Comparable<Assembly> {

	private String state;
	private String session;
	private Group group;
	private Districts districts;
	private GroupInfo aggregateGroupInfo; 
	private GroupInfo computationGroupInfo; 
	private List<AggregateResult> aggregateResults;
	private List<ComputationResult> computationResults;
	
	public Assembly() {
		this.aggregateGroupInfo = null; 
		this.computationGroupInfo = null; 
		this.aggregateResults = null;
		this.computationResults = null;
	}
	public Assembly(String state, String session) {
		this.state = state;
		this.session = session;
		this.districts = new Districts();
		this.aggregateGroupInfo = null; 
		this.computationGroupInfo = null; 
		this.aggregateResults = null;
		this.computationResults = null;
	}
	
	public Assembly(DBAssembly dbAssembly) {
		this.state = dbAssembly.getState();
		this.session = dbAssembly.getSession();
		this.districts = new Districts(dbAssembly.getDistricts());
	}
	
	public Assembly(Assembly assembly) {
		this.state = assembly.getState();
		this.session = assembly.getSession();
		this.districts = new Districts(assembly.getDistricts());
	}
	public void copyGroup(DBGroup dbGroup, DBAssembly dbAssembly) {

		group = new Group(dbGroup.getGroupName(), dbGroup.getGroupDescription());
		districts.copyGroup(dbGroup, dbAssembly.getDistricts());

		if ( dbAssembly.getAggregateGroupMap().containsKey(dbGroup) || dbAssembly.getAggregateMap().containsKey(dbGroup) ) {
			if ( aggregateGroupInfo == null || aggregateResults == null ) {
				aggregateGroupInfo = new GroupInfo(dbAssembly.getAggregateGroupMap().get(dbGroup));
				aggregateResults = new ArrayList<AggregateResult>(dbAssembly.getAggregateMap().get(dbGroup).getResultList());
			} else {
				aggregateGroupInfo.mergeGroupInfo( dbAssembly.getAggregateGroupMap().get(dbGroup) );
				for ( AggregateResult result: dbAssembly.getAggregateMap().get(dbGroup).getResultList() ) {
					aggregateResults.add(result);	
				} 
			}
		}

		if ( dbAssembly.getComputationGroupMap().containsKey(dbGroup) || dbAssembly.getComputationMap().containsKey(dbGroup) ) {
			if ( computationGroupInfo == null || computationResults == null ) {
				computationGroupInfo = new GroupInfo(dbAssembly.getComputationGroupMap().get(dbGroup));
				computationResults = new ArrayList<ComputationResult>(dbAssembly.getComputationMap().get(dbGroup).getResultList());
			} else {
				computationGroupInfo.mergeGroupInfo(dbAssembly.getComputationGroupMap().get(dbGroup));
				for ( ComputationResult result: dbAssembly.getComputationMap().get(dbGroup).getResultList() ) {
					computationResults.add(result);
				}
			}
		}
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
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public Districts getDistricts() {
		return districts;
	}
	public void setDistricts(Districts districts) {
		this.districts = districts;
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
	public List<AggregateResult> getAggregateResults() {
		return aggregateResults;
	}
	public void setAggregateResults(List<AggregateResult> aggregateResults) {
		this.aggregateResults = aggregateResults;
	}
	public List<ComputationResult> getComputationResults() {
		return computationResults;
	}
	public void setComputationResults(List<ComputationResult> computationResults) {
		this.computationResults = computationResults;
	}
	@Override
	public int compareTo(Assembly assembly) {
		int s = state.compareTo(assembly.state);
		if ( s != 0 ) return s;
		return this.session.compareTo(assembly.session);
	}
}
