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
	private List<InfoItem> aggregateInfoItems;
	private List<InfoItem> computeInfoItems;
	private List<AggregateResult> aggregateResults;
	private List<ComputeResult> computeResults;
	
	public Assembly() {
		aggregateInfoItems = new ArrayList<InfoItem>();
		computeInfoItems = new ArrayList<InfoItem>();
		aggregateResults = new ArrayList<AggregateResult>();
		computeResults = new ArrayList<ComputeResult>();
	}
	public Assembly(String state, String session) {
		this.state = state;
		this.session = session;
		this.districts = new Districts();
		aggregateInfoItems = new ArrayList<InfoItem>();
		computeInfoItems = new ArrayList<InfoItem>();
		aggregateResults = new ArrayList<AggregateResult>();
		computeResults = new ArrayList<ComputeResult>();
	}

	// empty, for templates
	public Assembly(Assembly assembly) {
		state = assembly.getState();
		session = assembly.getSession();
		districts = new Districts(assembly.getDistricts());
		aggregateInfoItems = new ArrayList<InfoItem>();
		computeInfoItems = new ArrayList<InfoItem>();
		aggregateResults = new ArrayList<AggregateResult>();
		computeResults = new ArrayList<ComputeResult>();
	}

	
	public Assembly(DBAssembly dbAssembly) {
		state = dbAssembly.getState();
		session = dbAssembly.getSession();
		districts = new Districts(dbAssembly.getDistricts());
		aggregateInfoItems = new ArrayList<InfoItem>();
		computeInfoItems = new ArrayList<InfoItem>();
		aggregateResults = new ArrayList<AggregateResult>();
		computeResults = new ArrayList<ComputeResult>();
	}

	public void copyGroup(DBGroup dbGroup, DBAssembly dbAssembly) {

		group = new Group(dbGroup.getGroupName(), dbGroup.getGroupDescription());
		districts.copyGroup(dbGroup, dbAssembly.getDistricts());

		for( DBInfoItem dbInfoItem: dbAssembly.getGroupInfoMap().get(dbGroup).getAggregateGroupItems() ) {
			aggregateInfoItems.add(new InfoItem(dbInfoItem));
		}
		for( DBInfoItem dbInfoItem: dbAssembly.getGroupInfoMap().get(dbGroup).getComputeGroupItems() ) {
			computeInfoItems.add(new InfoItem(dbInfoItem));
		}
		for ( AggregateResult aggregateResult: dbAssembly.getGroupResultsMap().get(dbGroup).getAggregateResults()) {
			aggregateResults.add(aggregateResult);
		}
		for ( ComputeResult computeResult: dbAssembly.getGroupResultsMap().get(dbGroup).getComputeResults()) {
			computeResults.add(computeResult);
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
	public List<InfoItem> getAggregateInfoItems() {
		return aggregateInfoItems;
	}
	public void setAggregateInfoItems(List<InfoItem> aggregateInfoItems) {
		this.aggregateInfoItems = aggregateInfoItems;
	}
	public List<InfoItem> getComputeInfoItems() {
		return computeInfoItems;
	}
	public void setComputeInfoItems(List<InfoItem> computeInfoItems) {
		this.computeInfoItems = computeInfoItems;
	}
	public List<AggregateResult> getAggregateResults() {
		return aggregateResults;
	}
	public void setAggregateResults(List<AggregateResult> aggregateResults) {
		this.aggregateResults = aggregateResults;
	}
	public List<ComputeResult> getComputeResults() {
		return computeResults;
	}
	public void setComputeResults(List<ComputeResult> computeResults) {
		this.computeResults = computeResults;
	}
	@Override
	public int compareTo(Assembly assembly) {
		int s = state.compareTo(assembly.state);
		if ( s != 0 ) return s;
		return this.session.compareTo(assembly.session);
	}
}
