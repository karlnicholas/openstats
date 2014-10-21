package openstats.osmodel;

import java.util.*;

import javax.xml.bind.annotation.XmlRootElement;

import openstats.dbmodel.*;

@XmlRootElement
public class OSAssembly implements Comparable<OSAssembly> {

	private String state;
	private String session;
	private OSGroup osGroup;
	private OSDistricts osDistricts;
	private OSGroupInfo aggregateGroupInfo; 
	private OSGroupInfo computationGroupInfo; 
	private List<Long> aggregateValues;
	private List<Double> computationValues;
	
	public OSAssembly() {
		this.aggregateGroupInfo = null; 
		this.computationGroupInfo = null; 
		this.aggregateValues = null;
		this.computationValues = null;
	}
	public OSAssembly(String state, String session, OSGroup osGroup) {
		this.state = state;
		this.session = session;
		this.osGroup = osGroup;
		this.osDistricts = new OSDistricts();
		this.aggregateGroupInfo = null; 
		this.computationGroupInfo = null; 
		this.aggregateValues = null;
		this.computationValues = null;
	}
	
	public OSAssembly(DBGroup dbGroup, DBAssembly dbAssembly) {
		this.state = dbAssembly.getState();
		this.session = dbAssembly.getSession();
		this.osGroup = new OSGroup(dbGroup.getGroupName(), dbGroup.getGroupDescription());
		this.osDistricts = new OSDistricts(dbGroup, dbAssembly.getDistricts());

		if ( dbAssembly.getAggregateGroupMap().containsKey(dbGroup) || dbAssembly.getAggregateMap().containsKey(dbGroup) ) {
			aggregateGroupInfo = new OSGroupInfo(dbAssembly.getAggregateGroupMap().get(dbGroup));
			aggregateValues = new ArrayList<Long>(dbAssembly.getAggregateMap().get(dbGroup).getValueList());
		}

		if ( dbAssembly.getComputationGroupMap().containsKey(dbGroup) || dbAssembly.getComputationMap().containsKey(dbGroup) ) {
			computationGroupInfo = new OSGroupInfo(dbAssembly.getComputationGroupMap().get(dbGroup));
			computationValues = new ArrayList<Double>(dbAssembly.getComputationMap().get(dbGroup).getValueList());
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
	public OSGroup getOSGroup() {
		return osGroup;
	}
	public void setOSGroup(OSGroup osGroup) {
		this.osGroup = osGroup;
	}
	public OSDistricts getOSDistricts() {
		return osDistricts;
	}
	public void setOSDistricts(OSDistricts osDistricts) {
		this.osDistricts = osDistricts;
	}
	public OSGroupInfo getAggregateGroupInfo() {
		return aggregateGroupInfo;
	}
	public void setAggregateGroupInfo(OSGroupInfo aggregateGroupInfo) {
		this.aggregateGroupInfo = aggregateGroupInfo;
	}
	public OSGroupInfo getComputationGroupInfo() {
		return computationGroupInfo;
	}
	public void setComputationGroupInfo(OSGroupInfo computationGroupInfo) {
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
	public int compareTo(OSAssembly assembly) {
		int s = state.compareTo(assembly.state);
		if ( s != 0 ) return s;
		return this.session.compareTo(assembly.session);
	}
}
